/* let modal;
let btn;
let span; */
let $modal;
let $joinbtn;
let $close;
let $badid;
let $text;
$(document).ready(() => {
	$modal = $("#modal_query");
	$joinbtn = $("#joinbtn");
	$close = $("#close_modal");
	$badid = $("#bad_id");
	$text = $("#party_id_box");
	/* REMOVE GREY OUT BUTTONS IF USER IS LOGGED IN */
	//let status = document.getElementById("premium").innerHTML;
	if (document.getElementById("premium").innerHTML === "true") {
		let host_button = document.getElementById("host");
    	host_button.classList.remove("gray");
	}
	$joinbtn = $("#joinbtn");
	$joinbtn.toggleClass("gray");

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
	
	// Get the modal
	//let modal = document.getElementById("modal_query");

	// Get the button that opens the modal
	//let btn = document.getElementById("join_btn");
	$joinbtn.on('click', function(){
		console.log("clicked");
		$modal.css("display","block");
		$badid.hide();
	});
	// Get the <span> element that closes the modal
	//let span = document.getElementById("close_modal");
	$close.click(function(){
		//modal.style.display = "none";
		//$modal.style.display = "none";
		$modal.css("display","none");
	});
	let other_modal = document.getElementById("modal_query");
	window.onclick = function(event) {
		if (event.target == other_modal) {
			//modal.style.display = "none";
			//$modal.style.display = "none";
			console.log("haha");
			$modal.css("display","none");
			$text.val("");
		}
	}
	let regex_id = new RegExp('[A-Z]{6}$');
	$(document).keypress(event => {
        // 13 is the key code for the Enter key
        if (event.which === 13) {
			if($modal.css("display")==="block"){
				let currval = $text.val().toUpperCase();
				console.log(currval);
				if(regex_id.test(currval)){
					const postParameters = {"party_id": currval};
					window.location = "./join?" + jQuery.param(postParameters);
				}else{
					$badid.show();
				}
			}
		}
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




