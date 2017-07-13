var role=getCookie("role");
var url = location.pathname;
var array = url.split('/');
var path = array[array.length-1];



(function() {
		if(path=='sendsms.html'){		
			if(role!='staff'&& role!='admin')
				redirectLogin(false);	
		}
		if(path=='aimodosia.html'){
			if(role!='admin')
				redirectLogin(false);
		}
		if(path=='mobile.html'){
			if(role!='stud')
				redirectLogin(true);
		}
		if(path=='login.htlm'){
			if(role.length!=0)
				window.location('./')
		}
})();






function redirectLogin(redir){
	
		if(role.length!=0){
			setCookie('role',"",-1);
			setCookie('token',"",-1);
		}
		if(redir)
			setCookie('redirect',path,60);
		location.replace("login.html");
		
}

function setCookie(cname,cvalue,seconds) {
	
		var d = new Date();
		d.setTime(d.getTime() + (15*1000));
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




