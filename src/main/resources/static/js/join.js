
$(document).ready(() => {

	/*
	Toggle color for up and down buttons
	*/
	$("#down").click(function () {
		console.log("hiii");
		new_vote(false,"SongId");
		if (document.getElementById("up").classList.contains("upColor")) {
			$("#up").toggleClass("upColor");
		}
		$("#down").toggleClass("downColor");
	});

	$("#up").click(function () {
		new_vote(true,"SongId");
		if (document.getElementById("down").classList.contains("downColor")) {
			$("#down").toggleClass("downColor");
		}
		$("#up").toggleClass("upColor");
	});

	/*
	Generate song suggestions based on user input. Send POST request on each key press inside search bar.
	*/
	// $("#dropdown").hide();
	let $results = $("#dropdown");
  $("#playlist").keyup(event => {

  	let song = document.getElementById('songName').value;
  	console.log(song);

    if (song.length === 0) {
    	$("#dropdown").hide();
    }
    else {
      $("#dropdown").show();
	   	const postParameters = {word: song};
	    console.log(postParameters);
		  $results.empty(); // clear results list

	    // send input to backend to generate song suggestions
	    $.post("/suggestions", postParameters, responseJSON => {

  			const responseObject = JSON.parse(responseJSON);
  			console.log(responseObject);
  			let output = responseObject;

  			for(const sug of output){
  				$results.append("<a href='javascript:;' onclick='new_song(" + sug.song_id + ")'><div class='option'>" + sug.song_name + "</div></a>");
  			}
      });
	  }
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
const $playlist = $("#displaySongs");

// Setup the WebSocket connection for live updating of scores.
const setup_live_playlist = () => {
<<<<<<< HEAD
  // Create the WebSocket connection and assign it to `conn`
  conn = new WebSocket("ws://localhost:4567/join");
=======
  // TODO Create the WebSocket connection and assign it to `conn`
  conn = new WebSocket("ws://localhost:4567/songupdates");
>>>>>>> a7a1dd2712d92ff5ce7958b248a06dcc93295b6e

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

      case MESSAGE_TYPE.VOTESONG:
      	// update number of votes for a specific song on the playlist
      	let song_id = data.payload.song_id;
      	let votes = data.payload.votes; //number of votes the song has
		console.log(votes);
      	break;

      case MESSAGE_TYPE.ADDSONG:
        $playlist.append("<li id='" + $("#user_id").val() + "'>" + "</li>");
        $("#" + $("#user_id").val()).append("<div id='playlistItem'>" + "</div>");
        // add song information
        $(".playlistItem").append("<div class='track'>" + "</div>");
        $(".track").append("<div class='song'>" + data.payload.name + "</div>");
        $(".track").append("<div class='artist'>" + data.payload.artists[0].name + "</div>");
        // add number of votes

        // add buttons
        $(".playlistItem").append("<div id='buttons'>" + "</div>");
        $("#buttons").append("<a href='javascript:;' onclick='new_vote(false, " + data.payload.id + ")'><i class='fa fa-chevron-circle-down' id='down'></i></a>");
        $("#buttons").append("<a href='javascript:;' onclick='new_vote(true, " + data.payload.id + ")'><i class='fa fa-chevron-circle-up' id='up'></i></a>");
        break;

      case MESSAGE_TYPE.REMOVESONG:
      	$playlist.remove($("#" + $("#user_id").val())); // removes li of ul, referenced by userId
      	break;

      case MESSAGE_TYPE.PLAYLIST:
        // apend an entire list of li's to the displaySongs ul
        break;
    }
  };
}


/*
Send message to backend when a user votes on a song - params are boolean vote and song id
*/
function new_vote(vote_boolean, songId){
  // Send a VOTESONG message to the server using `conn`
  let vote = {"type":MESSAGE_TYPE.VOTESONG, "payload": {
        "id":$("#user_id").val(), 
        "song_id":songId,
        "vote":vote_boolean}
      };
  conn.send(JSON.stringify(vote));
}
/*
Send message to backend when a user adds a song
*/
function new_song(songId) {
  // Send a VOTESONG message to the server using `conn`
  console.log($("#user_id").val());
  let song = {"type":MESSAGE_TYPE.ADDSONG, "payload": {
        "id":$("#user_id").val(), 
        "song_id":songId}
      };
  conn.send(JSON.stringify(vote));
}


