var role=getCookie("role");
var url = location.pathname;
var array = url.split('/');
var path = array[array.length-1];



(function () {
		if(path=='sendSms.html')
			if(role!='staff')
				location.replace("./");
		if(path=='aimodosia.html')
			if(role!='aimodosia')
				location.replace("./");
		if(path=='services.html')
			populateDocumentation();
			
		loadNavBar(); 
})();
 

 

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
 
 
 
 function loadNavBar(){
	 
        var ul = document.getElementById('navbar');
        var t, tt;
		var links = [
			{link:"index.html",value:"Home"},
			{link:"services.html",value:"ΥΠΗΡΕΣΙΕΣ"}];
		if(role.length==0)
			links[2]={link:"login.html",value:'ΕΙΣΟΔΟΣ'};
		else if(role=='staff'){
			links[2]={link:"sendSms.html",value:'ΑΠΟΣΤΟΛΗ SMS'};
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
			li.appendChild(a);
            ul.appendChild(li);

           
        }
		
    }
	
	function populateDocumentation(){
		urlBuilder = "http://localhost:8080/sms/service/site";
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
				location.replace("./");
			},
			error: function () {
				document.getElementById("loginLabel").innerHTML = "Login Failed";
			}
		});
    }
	
	
	
	
	
	
	
	
	
	
	
	
