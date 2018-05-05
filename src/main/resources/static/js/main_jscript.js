
$(document).ready(() => {

	/* REMOVE GREY OUT BUTTONS IF USER IS LOGGED IN */
	//let status = document.getElementById("premium").innerHTML;
	if (document.getElementById("premium").innerHTML === "true") {
		let host_button = document.getElementById("host");
    	host_button.classList.remove("gray");
	}
	let join_button = document.getElementById("join");
	join_button.classList.remove("gray");

	/* NAV BAR */
	let $nav = $("nav-bar");
	console.log($nav);
	console.log($nav.height);

	let home = document.getElementById("home");
	home.addEventListener("click", color);

	function color() {
		console.log("here");
		home.classList.add("color");
	}

	let totalHeight = document.body.scrollHeight;
	console.log(totalHeight);

	/* PAN DOWN TO INFO */
	$("#down").click(function() {
	    $('html,body').animate({
	        scrollTop: $("#info").offset().top},
	        'slow');
	});

});

function join_id(){
	let party_id = prompt("Enter the party code:", "######");
	const postParameters = {"party_id": party_id};
	/* let completepath = window.location.host + window.location.pathname;
	let partpath = completepath.substring(0,completepath.lastIndexOf("/"));
	let newPath = partpath + "/join?" + jQuery.param(postParameters);
	console.log(newPath); */
	window.location = "./join?" + jQuery.param(postParameters);
}




