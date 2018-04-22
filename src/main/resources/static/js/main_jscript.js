const root_dir = "http://localhost:4567"

$(document).ready(() => {

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

	$("#down").click(function() {
		console.log("hi bis");
	    $('html,body').animate({
	        scrollTop: $("#info").offset().top},
	        'slow');
	});

});

function join_id(){
	let party_id = prompt("Enter the party code:", "######");
	const postParameters = {"party_id": party_id};
	window.location = root_dir + "/join?" + jQuery.param(postParameters);
}




