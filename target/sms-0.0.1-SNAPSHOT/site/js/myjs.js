var role=getCookie("role");
var url = location.pathname;
var array = url.split('/');
var path = array[array.length-1];
var inputs;
var message;



(function () {
		if(path=='sendsms.html'){		
			if(role!='staff')
				redirectLogin();
			loadSendSms();
		}
		if(path=='aimodosia.html')
			//if(role!='aimodosia')
			//redirectLogin();
		if(path=='services.html')
			populateDocumentation();
		if(path==""||path=="index.html")
			loadIndexDiv();
		if(path=='mobile.html')
			loadMobileDiv();

		loadNavBar(); 
})();
 
function redirectLogin(){
	setCookie('redirect',path,0.5);
	location.replace("login.html");
}
 

function setCookie(cname,cvalue,exdays) {
	
		var d = new Date();
		d.setTime(d.getTime() + (exdays*24*60*60*1000));
		var expires = "expires=" + d.toGMTString();
		document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
	}
	
function getCookie(cname) {
		var name = cname + "=";
		var decodedCookie = decodeURIComponent(document.cookie);
		var ca = decodedCookie.split(';');
		for(var i = 0; i < ca.length; i++) {
			var c = ca[i];
			while (c.charAt(0) == ' ') {
				c = c.substring(1);
			}
			if (c.indexOf(name) == 0) {
				return c.substring(name.length, c.length);
			}
    }
    return "";
}

function loadIndexDiv(){
	
	var html;
	if(role.length==0){
		html="<h1>Καλωσήρθατε στην υπηρεσία αποστολής SMS του Αλεξανδρειου ΤΕΙ Θεσσαλονίκης<br>Μπορείτε να δείτε τις υπηρεσίες στα δεξία<br>Παρακαλώ κάντε login</h1>"
	}
	else if(role=='staff'){
		html="<h1>Πατώντας το μαζική αποστολή μπορείτε να στείλετε μαζικά sms για ανακοινώσεις στο moodle<h1>";
	}
	else if(role=='stud'){
		html="<h1>Καλωσήρθες χρήστη.Τσέκαρε αν το τηλεφωνό σου είναι σωστο</h1>"
	}
	else if(role=='aimodosia'){
		window.replace("aimodosia.html");
	}
	$("#indexDiv").html(html);
	
	
}
 
 
 
 function loadNavBar(){
	 
        var ul = document.getElementById('navbar');
        var t, tt;
		var links = [
			{link:"./",value:"Home"},
			{link:"services.html",value:"ΥΠΗΡΕΣΙΕΣ"}];
		if(role.length==0)
			links[2]={link:"login.html",value:'ΕΙΣΟΔΟΣ'};
		else if(role=='staff'){
			links[2]={link:"sendsms.html",value:'ΑΠΟΣΤΟΛΗ SMS'};
			links[3]={link:"logout.html",value:'ΑΠΟΣΥΝΔΕΣΗ'};
		}
		else if(role=='aimodosia'){
			links[2]={link:"aimodosia.html",value:'ΑΠΟΣΤΟΛΗ SMS'};
			links[3]={link:"logout.html",value:'ΑΠΟΣΥΝΔΕΣΗ'};
		}
		else if(role=='stud'){
			links[2]={link:"mobile.html",value:'ΑΛΛΑΓΗ ΚΙΝΗΤΟΥ'}
			links[3]={link:"logout.html",value:'ΑΠΟΣΥΝΔΕΣΗ'}
		}
				
		var url = location.pathname;
		var array = url.split('/');
		var path=array[array.length-1];
		links.forEach(renderProductList);
		
        function renderProductList(element, index, arr) {
            var li = document.createElement('li');
			a = document.createElement('a');
			a.href =  element.link;
			a.innerHTML = element.value;
			if(element.link==path){
				li.setAttribute('class','active');
			}
			else if(element.link=="./" && path==""){
				li.setAttribute('class','active');
			}
			
			
			li.appendChild(a);
            ul.appendChild(li);

           
        }
		
    }
	
	function populateDocumentation(){
		urlBuilder = "http://http://localhost:8080/sms/service/site/moservices";
								$.ajax({
									url : urlBuilder,
									type : "GET",
									dataType : "json",
									success : function(response) {
										var trHTML = '';
										$.each(response, function(key, value) {
											trHTML += '<tr><td>'
													+ value.keyword
													+ '</td><td>'
													+ value.description
													+ '</td></tr>';
										});
										$('#records_table > tr').empty();
										$('#records_table').append(trHTML);
									}
								});
   }
   
       function login(){
        var data={};

        data.username=document.getElementById("username").value;
        data.password=document.getElementById("password").value;
		$.ajax({
			url: 'http://localhost:8080/sms/service/authentication',
			type: 'POST',
			data: JSON.stringify(data),
			contentType: 'application/json',
			dataType: 'json',
			success: function(response) {
				setCookie('token',response['token'],2);
				setCookie('role',response['role'],2);
				var redir=getCookie('redirect');
				if(redir.length!=0){
					setCookie('redirect',"",-1);
					location.replace(redir);
				}
				else
					location.replace("./");
			},
			error: function () {
				document.getElementById("loginLabel").innerHTML = "Login Failed";
			}
		});
    }
       
    function loadMobileDiv(){
    	$.ajax({
			url : 'http://localhost:8080/sms/service/site/mobile',
			type : 'GET',
			dataType : 'text',
			success : function(response) {
		    	$('#mobile').html("Το νουμερό σου είναι "+ response);
			},
			beforeSend: function(xhr, settings) { xhr.setRequestHeader('Authorization','Bearer ' +getCookie('token')); }
		});
    	
    }
	
    function loadSendSms(){
    	$.ajax({
			url : 'http://localhost:8080/sms/service/site/mtservices',
			type : 'GET',
			dataType : 'json',
			success : function(json) {
				$.each(json,function(i,value) {
									$('#templateSelect').append($(
															'<option>')
															.text(value['message'])
															.attr('value',value['messageId']));
								});
			}
		});

    }
    
    function leaveChange(){
    	var e = document.getElementById("templateSelect");
    	message= e.options[e.selectedIndex].text;
    	var html="<h3>";
    	var array=message.split('?');
    	inputs=array.length-1;
    	for(i=0;i<inputs;i++){
    		html+=array[i];
    		html+="<input maxlength='50' id='input"+i+"'></input>";
    	}
    	if(message.charAt(message.length - 1)!='?'){
    		html+=array[array.length-1];
    	}
    			
    	html+="</h3>"
    	html+="<br><button class='button' onclick='sendSms()'>Αποστολή μηνύματος</button>";

    	$('#message').html(html);
    }
    
    function sendSms(){

    	for(i=0;i<inputs;i++){
    		var e = document.getElementById("input"+i);
    		message=message.replace('?',e.value);
    	}
    	var r = confirm(message+'\nΑΠΟΣΤΟΛΗ  ?');
    	if (r == true) {
    	    alert('ok');
    	} else {
    		alert('not ok');
    	}	
    }
    
    function sendAimodosia(){       
		var e = document.getElementById('date');
		var date=e.value;
		var r = confirm('ΑΠΟΣΤΟΛΗ  ?');
    	if (r == true) {
    		$.ajax({
        	    type: 'POST',
        	    url: 'http://localhost:8080/sms/service/site/send/aimodosia',
        	    data: date,
        	    success: function(msg){
        	    	$('#mainDiv').html("<h1>ΤΟ ΜΗΝΥΜΑ ΣΤΑΛΘΗΚΕ ΜΕ ΕΠΙΤΥΧΙΑ</h1>");
        	    }
        	});
    		

    	} 
    }
    
    
    

  
	
	
	
	
	
	
	
	
	
	
