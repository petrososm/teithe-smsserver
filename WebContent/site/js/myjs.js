var role=getCookie("role");
var url = location.pathname;
var array = url.split('/');
var path = array[array.length-1];
var baseurl='http://'+location.host;

//sendsmsVars
var inputs;
var message;




(function () {

		if(path=='sendsms.html'){		
			//if(role!='staff')
				//redirectLogin();
			loadSendSms();
			
		}
		if(path=='aimodosia.html'){
			if(role!='admin')
				redirectLogin();
		}
		if(path=='services.html')
			populateDocumentation();
		if(path==""||path=="index.html")
			loadIndexDiv();
		if(path=='mobile.html'){
			if(role.length==0)
				redirectLogin();
			loadMobileDiv();
		}

		loadNavBar(); 
})();




 
function redirectLogin(){
	if(role.length!=0){
		setCookie('role',"",-1);
		setCookie('token',"",-1);
	}
	setCookie('redirect',path,0.5);
	location.replace("login.html");
}
 

function setCookie(cname,cvalue,hours) {
	
		var d = new Date();
		d.setTime(d.getTime() + (hours*60*60*1000));
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
	else if(role=='admin'){
		html="<h1>Καλωσήρθατε κυριε Διαχειριστα.</h1>"
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
		else if(role=='admin'){
			links[2]={link:"aimodosia.html",value:'ΑΙΜΟΔΟΣΙΑ '};
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
		if(role=='staff')
			$('#verifyPhoneLabel').hide();

		urlBuilder = baseurl+"/sms/service/site/moservices";
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
			url: baseurl+'/sms/service/authentication',
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
				alert("Login Failed");
				
			}
		});
    }
       
    function loadMobileDiv(){
    	$.ajax({
			url : baseurl+'/sms/service/site/mobile',
			type : 'GET',
			dataType : 'text',
			async:false,
			success : function(response) {
		    	$('#mobile').html("Το νουμερό σου είναι "+ response);
			},
			error: function(){
				$('#mobile').html("Δεν έχετε κάποιο κινητό τηλέφωνο καταχωρημένο");
			},
			beforeSend: function(xhr, settings) { xhr.setRequestHeader('Authorization','Bearer ' +getCookie('token')); }
		});
    	
    }
	
    function loadSendSms(){
    	loadCoursesMoodle();
    	loadSmsTemplates('Moodle');
    	loadSmsTemplates('Direct');
    	loadDynamicInput();
    }
    
    function loadCoursesMoodle(){
    	$('#courseSelect').append($(
		'<option>')
		.text('Επιλέξτε μάθημα')
		.attr('value',0));
    	$.ajax({
			url : baseurl+'/sms/service/site/courses',
			type : 'GET',
			dataType : 'json',
			success : function(json) {
				$.each(json,function(i,value) {
									$('#courseSelect').append($(
															'<option>')
															.text(value['courseName'])
															.attr('value',value['courseId']));
								});
			},beforeSend: function(xhr, settings) { xhr.setRequestHeader('Authorization','Bearer ' +getCookie('token'));}
		});
    	
    }
    
    function loadSmsTemplates(type){
    	$('#templateSelect'+type).append($(
		'<option>')
		.text('Επιλέξτε μήνυμα')
		.attr('value',0));
    	$.ajax({
			url : baseurl+'/sms/service/site/mtservices/'+type.toLowerCase(),
			type : 'GET',
			dataType : 'json',
			success : function(json) {
				$.each(json,function(i,value) {
									$('#templateSelect'+type).append($(
															'<option>')
															.text(value['message'])
															.attr('value',value['messageId']));
								});
			},beforeSend: function(xhr, settings) { xhr.setRequestHeader('Authorization','Bearer ' +getCookie('token'));}
		});
    }
    
    function courseSelected(){
    	var course=$( "select#courseSelect option:checked").text();
    	if(course!='Επιλέξτε μάθημα'){
    		var e= $("input[name='inputs']");
    		if(e[0]!=null){
    			e[0].value=course;
    			e[0].size=course.length;
    		}    	
    	}
    	

    }
    
    
    function templateSelected(type){
    	var e = document.getElementById("templateSelect"+type);
    	message= e.options[e.selectedIndex].text;
    	var html="<h3>";
    	var array=message.split('?');
    	inputs=array.length-1;
    	for(i=0;i<inputs;i++){
    		html+=array[i];
    		html+="<input maxlength='50' name='inputs'></input>";
    	}
    	if(message.charAt(message.length - 1)!='?'){
    		html+=array[array.length-1];
    	}
    	
    			
    	html+="</h3>"
    	html+="<br><button class='button' onclick='sendSms(\""+type+"\")'>Αποστολή μηνύματος</button>";

    	$('#message'+type).html(html);
    	if(type=='Moodle'){
    		var course=$( "select#courseSelect option:checked").text();
	    	if(course!='Επιλέξτε μάθημα')
	    		$('#input0').val(course);
    	}
    }
    
    function sendSms(type){
    	if(type=='Moodle')
    		sendSmsMoodle();
    	else if(type=='Direct'){
    		sendSmsDirect();
    	}
    }
    
    function sendSmsMoodle(){
    	
    	var messageToSend=message;
    	
    	var inputCheck=true;
	    $("input[name='inputs']").each(function() {
	    		messageToSend=messageToSend.replace('?',$(this).val());
	        	if($(this).val()==''){
	        		inputCheck=false;
	        	}
	    });
	       
	    if(!inputCheck){
    			$('#alert').show();
    			$('#alertMessage').html("Συμπλήρώστε ολα τα πεδία");
    			return;
	     }
    	
    	
    	var course=$( "select#courseSelect option:checked").text();
    	if(course=='Επιλέξτε μάθημα'){
    		$('#alert').show();
			$('#alertMessage').html("Επιλέξτε μάθημα !");
			return;
    	}
    	
	    	var tem = document.getElementById("templateSelectMoodle");
  	    	if(tem.options[tem.selectedIndex].value==0){
  	    		$('#alert').show();
  				$('#alertMessage').html("Επιλέξτε μήνυμα");
  	    		return;
  	    	}
    	
    	$('#alert').hide();

    	bootbox.confirm('Θα αποσταλεί το μήνυμα <br><i>'+messageToSend+'</i><br>στους εγγεγραμένους φοιτητές του μαθήματος<br><i>'+course, function(result) {
    		  if(result==true){


      	    	
      	    	var dialog = bootbox.dialog({
      	    	    message: '<h1 class="text-center">Τα μηνύματα στέλνονται.Παρακαλώ περιμέντε..</h1>',
      	    	    closeButton: false
      	    	});
              	var data={};
      	    	data.messageId=tem.options[tem.selectedIndex].value;
      	    	data.replacements=$("input[name='inputs']").map(function(){return $(this).val();}).get();;
      	    	data.course=$( "select#courseSelect option:checked").val();	

      	    	$.ajax({
      				url: baseurl+'/sms/service/send',
      				type: 'POST',
      				data: JSON.stringify(data),
      				contentType: 'application/json',
      				dataType: 'json',
      				success: function(response) {
      					dialog.modal('hide');

              	    	bootbox.alert('<h2>Στο μάθημα ειναι εγγεγραμένοι '+response['total']+' μαθητές<br>' +
              	    			'Βρέθηκαν τηλέφωνα για '+response['sent']+' φοιτητές<br>'+
              	    			'Το μήνυμα παραδώθηκε επιτυχώς σε '+response['delivered']+' φοιτητές</h2>'
              	    			,function(){ location.replace("sendsms.html") });;
              	    	
      				},
      				error: function () {
      					dialog.modal('hide');
              	    	bootbox.alert('<h2>Η αποστολή απέτυχε.<br>Επικοινωνήστε με τον διαχειριστή</h2>');
      				},beforeSend: function(xhr, settings) { xhr.setRequestHeader('Authorization','Bearer ' +getCookie('token'));}

      			});	
    		  }
    	}); 
            	
    		
    	
    }
 function sendSmsDirect(){
    	
    	var messageToSend=message;
    	
    	var inputCheck="";
	    $("input[name='inputs']").each(function() {
	    		messageToSend=messageToSend.replace('?',$(this).val());
	        	if($(this).val()==''){
	        		inputCheck='Συμπληρώστε όλα τα πεδία';
	        	}
	    });
	    
	    $("input[name='recipients']").each(function() {
        	if($(this).val()==''||$(this).val().length!=10||!($.isNumeric($(this).val()))){
        		inputCheck='Συμπληρώστε σωστά τα νούμερα των παραληπτών';
        	}
    });
	       
	    if(inputCheck!=""){
    			$('#alert').show();
    			$('#alertMessage').html(inputCheck);
    			return;
	     }
    	
    	
    	var tem = document.getElementById("templateSelectDirect");
	    	if(tem.options[tem.selectedIndex].value==0){
	    		$('#alert').show();
				$('#alertMessage').html("Επιλέξτε μήνυμα");
	    		return;
	    	}
    	$('#alert').hide();
    	
    	
    	bootbox.confirm('Θα αποσταλεί το μήνυμα <br><i>'+messageToSend+'</i><br>στα τηλέφωνα<br><i>'+$("input[name='recipients']").map(function(){return $(this).val();}).get(), function(result) {
    		  if(result==true){
      	    	
      	    	var dialog = bootbox.dialog({
      	    	    message: '<h1 class="text-center">Τα μηνύματα στέλνονται.Παρακαλώ περιμέντε..</h1>',
      	    	    closeButton: false
      	    	});
              	var data={};
      	    	data.messageId=tem.options[tem.selectedIndex].value;
      	    	data.replacements=$("input[name='inputs']").map(function(){return $(this).val();}).get();
      	    	data.recipients=$("input[name='recipients']").map(function(){return $(this).val();}).get();

      	    	$.ajax({
      				url: baseurl+'/sms/service/send/direct',
      				type: 'POST',
      				data: JSON.stringify(data),
      				contentType: 'application/json',
      				dataType: 'json',
      				success: function(response) {
      					dialog.modal('hide');

              	    	bootbox.alert('<h2>Το μήνυμα παραδώθηκε επιτυχώς σε '+response['delivered']+' παραλήπτες</h2>'
              	    			,function(){ location.replace("sendsms.html") });;
              	    	
      				},
      				error: function () {
      					dialog.modal('hide');
              	    	bootbox.alert('<h2>Η αποστολή απέτυχε.<br>Επικοινωνήστε με τον διαχειριστή</h2>');
      				},beforeSend: function(xhr, settings) { xhr.setRequestHeader('Authorization','Bearer ' +getCookie('token'));}

      			});	
    		  }
    	}); 
            	
    		
    	
    }
    
	function loadDynamicInput(){
	    var max_fields      = 10; //maximum input boxes allowed
	    var wrapper         = $(".input_fields_wrap"); //Fields wrapper
	    var add_button      = $(".add_field_button"); //Add button ID
	    var x = 1; //initlal text box count
	    $(add_button).click(function(e){ //on add input button click
	        e.preventDefault();
	        if(x < max_fields){ //max input box allowed
	            x++; //text box increment
	            $(wrapper).append('<div><input type="text" name="recipients"/><a href="#" class="remove_field"> Διαγραφή</a></div>'); //add input box
	        }
	    	

	    });
	    
	    $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
	        e.preventDefault(); $(this).parent('div').remove(); x--;
	    })
	    
	    
	    
		}
    
    
    function sendAimodosia(){       
		var e = document.getElementById('date');
		var date=e.value;
		var r = confirm('ΑΠΟΣΤΟΛΗ  ?');
    	if (r == true) {
    		$.ajax({
        	    type: 'POST',
        	    url: baseurl+'/sms/service/site/send/aimodosia',
        	    data: date,
        	    success: function(msg){
        	    	$('#mainDiv').html("<h1>ΤΟ ΜΗΝΥΜΑ ΣΤΑΛΘΗΚΕ ΜΕ ΕΠΙΤΥΧΙΑ</h1>");
        	    }
        	});
    		

    	} 
    }
    
    function sendMobileConfirmation(){
		var e = document.getElementById('mobileNumber');
		var mobile=e.value;
		if(mobile.length!=10){
			alert("Το νουμερο πρεπει να ειναι 10 ψηφια")
			return;
		}

    	$.ajax({
			url : baseurl+'/sms/service/site/mobile/sendConfirmation/'+mobile,
			type : 'GET',
			dataType : 'text',
			success : function(response) {
		    	$('#mobileDiv').html("<h2>Εισάγετε τον κωδικο που λάβατε στο τηλεφωνό σας<br>" +
		    			"<input maxlength='50' id='verificationCode'></input></h2>" +
		    			"<button class='button' onclick='changeMobile()'>ΑΛΛΑΓΗ</button>");			
			},
			beforeSend: function(xhr, settings) { xhr.setRequestHeader('Authorization','Bearer ' +getCookie('token')); }
		});
    	
    }
    
    function changeMobile(){
    	var e=document.getElementById('verificationCode');
    	var verificationCode=e.value;
    	$.ajax({
    	    type: 'PUT',
    	    url: baseurl+'/sms/service/site/mobile',
    	    data: verificationCode,
    	    success: function(msg){
    	    	$('#mobileDiv').html("<h1>Το νούμερο του κινητού σας άλλαξε</h1>");
    	    },beforeSend: function(xhr, settings) { xhr.setRequestHeader('Authorization','Bearer ' +getCookie('token')); }
    	});
    	
    }
    
    
    

  
	
	
	
	
	
	
	
	
	
	
