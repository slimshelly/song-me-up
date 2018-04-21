

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




