/**
/* Stores the username in a cookie
/* It reads the username from html select input, this is proof of concept, should be changed in production site (for ex. getting hash via ajax)
*/

function setUsername(){
	var username = document.getElementById("epistemeuser").options[document.getElementById("epistemeuser").selectedIndex].value;
	setCookie("epistemeuser",username,7);
	document.location.reload(true);
}

/**
/* Loads username at the beginning
/*
*/
function loadUsername(){	
	var val = getCookie('epistemeuser');
	//If cookie does not exist (for ex. first time visiting)
	if (typeof val === 'undefined'){
		setUsername();	
	}else{
		//If cookie already exists, select the correct username in the select input
		var sel = document.getElementById('epistemeuser');
		for(var i, j = 0; i = sel.options[j]; j++) {
			if(i.value == val) {
			    sel.selectedIndex = j;
			    break;
	     	 	}
	   	 }
   	 }
}
