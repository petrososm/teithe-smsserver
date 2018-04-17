var role=getCookie("role");
var url = location.pathname;
var array = url.split('/');
var path = array[array.length-1];
var baseurl='http://'+location.host;

//sendsmsVar
var message;




$( document ).ready(function () {

		if(path=='sendsms.html'){		
			loadSendSms();
			
		}
		else if(path=='services.html')
			loadServicesDocumentation();
		else if(path==""||path=="index.html")
			loadIndexDiv();
		else if(path=='mobile.html'){
			loadMobileDiv();
		}
		loadNavBar(); 
});


function cookieExpired(){
	bootbox.alert("Η συνεδρία έληξε,παρακαλώ συνδεθείτε ξανά",function(result){
		if(role.length!=0){
			setCookie('role',"",-1);
			setCookie('token',"",-1);
		}
		setCookie('redirect',path,0.1);
		location.replace("login.html");
	});	
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
	    if(getCookie('ldapLogin').length!=0){
			$('#alert').show();
			$('#alertMessage').html("Παρατηρήθηκε οτι κάνατε είσοδο στο σύστημα μέσω του λογαριασμού LDAP.\n" +
					"Εαν είστε καθηγήτης και θέλετε να στείλετε μαζικά μηνύματα,θα πρέπει να κάνετε είσοδο με τα στοιχεία της πυθίας.");
	    }
	}
	else if(role=='admin'){
		links[2]={link:"sendsms.html",value:'ΑΠΟΣΤΟΛΗ SMS'};
		links[3]={link:"aimodosia.html",value:'ΑΙΜΟΔΟΣΙΑ '};
		links[4]={link:"logout.html",value:'ΑΠΟΣΥΝΔΕΣΗ'};
	}
	else if(role=='stud'){
		links[2]={link:"mobile.html",value:'ΑΛΛΑΓΗ ΚΙΝΗΤΟΥ'}
		links[3]={link:"logout.html",value:'ΑΠΟΣΥΝΔΕΣΗ'}
	}
			
	var url = location.pathname;
	var array = url.split('/');
	var path=array[array.length-1];
	links.forEach(renderList);
	
    function renderList(element, index, arr) {
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

//------------------------------------------------------------------------------------------------
//INDEX.HTML
//------------------------------------------------------------------------------------------------
function loadIndexDiv(){
	
	var html;
	if(role.length==0){
		html="<h1>Καλωσήρθατε στην υπηρεσία αποστολής SMS του Αλεξανδρειου ΤΕΙ Θεσσαλονίκης<br>Μπορείτε να δείτε τις υπηρεσίες στα δεξία<br>Παρακαλώ κάντε login</h1>"
	}
	else if(role=='staff'){
		html="<h1>Πατώντας το μαζική αποστολή μπορείτε να στείλετε μαζικά sms για ανακοινώσεις στο moodle<h1>";
	}
	else if(role=='stud'){
		html="<h1>Καλωσήρθες χρήστη.<br>Στην καρτέλα 'ΥΠΗΡΕΣΙΕΣ' μπορεις να δεις τις διαθεσιμες υπηρεσιες που παρέχονται απο το ιδρύμα<br>" +
				"Για την σωστή λειτουργία της υπηρεσίας επιβεβαίωσε το τηλεφωνό σου</h1>"
	}
	else if(role=='admin'){
		html="<h1>Καλωσήρθατε κυριε Διαχειριστα.</h1>"
	}
	$("#indexDiv").html(html);
	


	
	
}
 

//------------------------------------------------------------------------------------------------
//SERVICES.HTML
//------------------------------------------------------------------------------------------------
	function loadServicesDocumentation(){
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
   
//  ------------------------------------------------------------------------------------------------
//  LOGIN.HTML
//  ------------------------------------------------------------------------------------------------
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
				if(response['ldapLogin']==1 && response['role']=='staff')
					setCookie('ldapLogin',true,2);
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
				bootbox.alert("Η ταυτοποίηση των στοιχείων σας απέτυχε");
				
			}
		});
    }
       
//     ------------------------------------------------------------------------------------------------
//     SENDSMS.HTML
//     ------------------------------------------------------------------------------------------------
	
    function loadSendSms(){
    	loadCoursesMoodle();
    	loadSmsTemplates('Moodle');
    	loadSmsTemplates('Direct');
    	loadDirectSms();
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
			},beforeSend: function(xhr, settings) { xhr.setRequestHeader('Authorization','Bearer ' +getCookie('token'));
			},complete: function(xhr, textStatus) {
			        if(xhr.status==401||xhr.status==403){
  			        	cookieExpired();
  			        }
  			 } 
			
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
			},beforeSend: function(xhr, settings) { xhr.setRequestHeader('Authorization','Bearer ' +getCookie('token'));
			},complete: function(xhr, textStatus) {
			        if(xhr.status==401||xhr.status==403){
  			        	cookieExpired();
  			        }
  			 } 
			
		});
    }
    
    function courseSelected(){
    	changeInputToCourse();
    }
    
    function changeInputToCourse(){
    	var course=$( "select#courseSelect option:checked").text();
    	if(course!='Επιλέξτε μάθημα'){
    		var e= $("input[name='inputs']");
    		if(e[0]!=null){
    			e[0].value=course;
    		}    	
    	}
    	else{
    		var e= $("input[name='inputs']");
    		if(e[0]!=null){
    			e[0].value="";
    		}    	
    	}
    }
    
    
    function templateSelected(type){
    	var e = document.getElementById("templateSelect"+type);
    	if(e.options[e.selectedIndex].value=='0'){
    		$('#message'+type).hide();
    		return;
    	}
    		
    	message= e.options[e.selectedIndex].text;
    	var html="";
    	var array=message.split('?');
    	inputs=array.length-1;
    	for(i=0;i<inputs;i++){
    		html+="<h4>"+array[i]+"</h4>";
    		html+="<input type=\"text\" class=\"form-control\" name='inputs'></input>";
    	}
    	if(message.charAt(message.length - 1)!='?'){
    		html+="<h4>"+array[array.length-1]+"</h4>";	
    	}
    	
    			
    	html+="<br><button class='button' onclick='sendSms(\""+type+"\")'>Αποστολή μηνύματος</button>";
    	
    	
    	$('#message'+type).html(html);
    	$('input[name=\'inputs\'').css('width', '70%');
    	$('#message'+type).show();
    	
    	if(type=='Moodle'){
    		changeInputToCourse();
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
      				url: baseurl+'/sms/service/send/moodle',
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
      				,complete: function(xhr, textStatus) {
      			        if(xhr.status==401||xhr.status==403){
      			        	cookieExpired();
      			        }
      			    } 

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
      				,complete: function(xhr, textStatus) {
      			        if(xhr.status==401||xhr.status==403){
      			        	cookieExpired();
      			        }
      			    } 

      			});	
    		  }
    	}); 
            	
    		
    	
    }
    
	function loadDirectSms(){
	    var max_fields      = 10; //maximum input boxes allowed
	    var wrapper         = $(".input_fields_wrap"); //Fields wrapper
	    var add_button      = $(".add_field_button"); //Add button ID
	    var x = 1; //initlal text box count
	    $(add_button).click(function(e){ //on add input button click
	        e.preventDefault();
	        if(x < max_fields){ //max input box allowed
	            x++; //text box increment
	            $(wrapper).append('<div><input type="text" maxlength=\'10\' name="recipients"/><a href="#" class="remove_field"> Διαγραφή</a></div>'); //add input box
	        }
	    });
	    
	    $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
	        e.preventDefault(); $(this).parent('div').remove(); x--;
	    })
	    
		
	    
		$('#panel1').on('shown.bs.collapse', function (e) {
    			$('#templateSelectDirect').val('0').change();
   	
		})
		
		$('#panel2').on('shown.bs.collapse', function (e) {
    			$('#templateSelectMoodle').val('0').change();
    		    	
		})
		
	    
	    
		}
	
	

    
//  ------------------------------------------------------------------------------------------------
//  AIMODOSIA.HTML
//  ------------------------------------------------------------------------------------------------
    function sendAimodosia(){       
		var e = document.getElementById('date');
		var date=e.value;
    
  	    	var dialog = bootbox.dialog({
  	    	    message: '<h1 class="text-center">Τα μηνύματα στέλνονται.Παρακαλώ περιμέντε..</h1>',
  	    	    closeButton: false
  	    	});
    		$.ajax({
        	    type: 'POST',
        	    url: baseurl+'/sms/service/send/aimodosia',
        	    data: date,
        	    success: function(response){
        	    	dialog.modal('hide');
        	    	$('#mainDiv').html("<h2>'Σταλθηκε σε "+response['sent']+" αιμοδότες<br>"+
              	    			"Το μήνυμα παραδώθηκε επιτυχώς σε "+response['delivered']+" αιμοδότες</h2>");
        	    },error: function () {
  					dialog.modal('hide');
          	    	bootbox.alert('<h2>Η αποστολή απέτυχε.<br>Ενδο-επικοινωνήστε με τον διαχειριστή</h2>');
  				},beforeSend: function(xhr, settings) { xhr.setRequestHeader('Authorization','Bearer ' +getCookie('token'));},
  				complete: function(xhr, textStatus) {
  			        if(xhr.status==401||xhr.status==403){
  			        	cookieExpired();
  			        }
  				}

  				
        	});
    		


    }
    
    
//    ------------------------------------------------------------------------------------------------
//    MOBILE.HTML
//    ------------------------------------------------------------------------------------------------
    
    function loadMobileDiv(){
    	$.ajax({
			url : baseurl+'/sms/service/site/mobile',
			type : 'GET',
			dataType : 'text',
			async:false,
			success : function(response) {
		    	$('#mobile').html("Το νουμερό σας είναι "+ response);
			},
			error: function(){
				$('#mobile').html("Δεν έχετε κάποιο κινητό τηλέφωνο καταχωρημένο");
			},complete: function(xhr, textStatus) {
  			        if(xhr.status==401||xhr.status==403){
  			        	cookieExpired();
  			        }
			},
			beforeSend: function(xhr, settings) { xhr.setRequestHeader('Authorization','Bearer ' +getCookie('token')); }

		});
    	
    }
    
   
    
    function sendMobileConfirmation(){
		var e = document.getElementById('mobileNumber');
		var mobile=e.value;
		if(mobile.length!=10||!($.isNumeric(mobile))){
			bootbox.alert("Το νουμερο πρεπει να ειναι 10 ψηφια");
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
			beforeSend: function(xhr, settings) { xhr.setRequestHeader('Authorization','Bearer ' +getCookie('token')); },
			complete: function(xhr, textStatus) {
			        if(xhr.status==401||xhr.status==403){
			        	cookieExpired();
			        }
			}
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
    	    },error: function () {
          	    	bootbox.alert('<h2>Ο κωδικός επιβεβαίωσης είναι λάθος</h2>');
  			},beforeSend: function(xhr, settings) { xhr.setRequestHeader('Authorization','Bearer ' +getCookie('token')); 
  			},complete: function(xhr, textStatus) {
			        if(xhr.status==401||xhr.status==403){
			        	cookieExpired();
			        }
			}
    	});
    	
    }
    
    
    

  
	
	
	
	
	
	
	
	
	
	
