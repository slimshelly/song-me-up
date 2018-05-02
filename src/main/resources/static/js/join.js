
$(document).ready(() => {

	/*
	Generate song suggestions based on user input. Send POST request on each key press inside search bar.
	*/
	$("#dropdown").hide();
    $("#playlist").keyup(event => {
    	let song = document.getElementById('songName').value;
    	console.log(song);

	    if (song.length === 0) {
	    	$("#dropdown").hide();
	    }
	    else {
		   	const postParameters = {word: song};
		    console.log(postParameters);

		    // Make a POST request to the "/suggestions" endpoint with the word information
		    $.post("/suggestions", postParameters, responseJSON => {

		        // Parse the JSON response into a JavaScript object.
		        const responseObject = JSON.parse(responseJSON);
		        console.log(responseObject);
		        
		        let output = responseObject.suggestions;
		   		
		   		// set width of dropdown to be same width as input text box
		   		// var dropdown = document.getElementById("dropdown").style.width = searchBarWidth + "px";
		   		$("#dropdown").show();

		   		let suggestions = "";
		        for (let i = 0; i <= output.length - 1; i++) {
		        	suggestions += output[i] + "<br />";
		        }
		        $message.html(suggestions);

		        // NEED TO MAKE SUGGESTIONS CLICKABLE
		    });
		}
    });

    /*
	Retrieve current list of coming up songs from backend to display on page.
    */
    
});
