
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
    Send message to backend when a user votes on a song
    */

    $(".fa-chevron-circle-up").click(function(e){
    	// color button permanently green, send VOTE message
    	new_vote();
	});

    $(".fa-chevron-circle-down").click(function(e){
		// color button permanently red, send VOTE message
		new_vote();
	});

});

/*
Open socket for web communication and deal with different messages
*/
const MESSAGE_TYPE = {
  VOTESONG: 0,
  ADDSONG: 1,
  REMOVESONG: 2,
  PLAYLIST: 3
};

let conn;
let userId = $(".hidden").innerHTML;
const $playlist = $("#displaySongs");

// Setup the WebSocket connection for live updating of scores.
const setup_live_playlist = () => {
  // TODO Create the WebSocket connection and assign it to `conn`
  conn = new WebSocket("ws://localhost:4567/join");

  conn.onerror = err => {
    console.log('Connection error:', err);
  };

  conn.onmessage = msg => {
    const data = JSON.parse(msg.data);
    console.log(data);
    switch (data.type) {
      default:
        console.log('Unknown message type!', data.type);
        break;

      case MESSAGE_TYPE.VOTESONG; 
      	// update number of votes for a specific song on the playlist

      	break;

      case MESSAGE_TYPE.ADDSONG:
        $playlist.append("<li id='" + userId + "'>" + "</li>");
        $("#" + userId).append("<div id='playlistItem'>" + "</div>");
        // add song information
        $(".playlistItem").append("<div class='track'>" + "</div>");
        $(".track").append("<div class='song'>" + data.payload.song_name + "</div>");
        $(".track").append("<div class='artist'>" + data.payload.song_artist + "</div>");
        // add buttons
        $(".playlistItem").append("<div id='buttons'>" + "</div>");
        $("#buttons").append("<a href='#''><i class='fa fa-chevron-circle-down' id='down'></i></a>");
        $("#buttons").append("<a href='#''><i class='fa fa-chevron-circle-up' id='up'></i></a>");
        break;

      case MESSAGE_TYPE.REMOVESONG:
      	$playlist.remove($("#" + userId)); // removes li of ul, referenced by userId
      	break;

      case MESSAGE_TYPE.PLAYLIST:
        // apend an entire list of li's to the displaySongs ul
        break;
    }
  };
}

/*
Send message to backend when a user votes on a song
*/
const new_vote = songId => {
  // Send a VOTESONG message to the server using `conn`
  console.log(myId);
  let vote = {"type":MESSAGE_TYPE.VOTESONG, "payload": {
        "id":userId, 
        "song_id":songId,
        "vote":$("#")}
      };
  conn.send(JSON.stringify(vote));
}

/*
Send message to backend when a user adds a song
*/
const new_song = songId => {
  // Send a VOTESONG message to the server using `conn`
  console.log(myId);
  let song = {"type":MESSAGE_TYPE.ADDSONG, "payload": {
        "id":userId, 
        "song_id":songId
      };
  conn.send(JSON.stringify(vote));
}


