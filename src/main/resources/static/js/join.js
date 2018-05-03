
let $playlist;
let $votingBlock;
let $playingBlock;

$(document).ready(() => {

  // access playlist to add songs to later
  $playlist = $("#suggestions");
  $votingBlock = $("#voting");
  $playingBlock = $("#playing");

	/*
	Toggle color for up and down buttons
	*/
	$("#down").click(function () {
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
			  $results.empty();
		    // send input to backend to generate song suggestions
		    $.post("/suggestions", postParameters, responseJSON => {

				const responseObject = JSON.parse(responseJSON);
				console.log(responseObject);
				let output = responseObject;

				for(const sug of output){
          $results.append("<a href='javascript:;' onclick='new_song(\"" + sug.id.toString() + "\")'><div class='option'>" + sug.name + "</div></a>");
        };
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

// Setup the WebSocket connection for live updating of scores.
const setup_live_playlist = () => {
  // TODO Create the WebSocket connection and assign it to `conn`
  console.log("ASLBDLKFA");
  conn = new WebSocket("ws://localhost:4567/songupdates");


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
        let votingList = data.payload;
        // loop through json objects in payload
        // display number of votes for given song_id
        votingList.forEach(function(suggestion) {
        $votingBlock.append("<li id='" + $("#user_id").val() + "'>" 
          + "<div class='votingItem'>"
          + "<div class='track'>" 
          + "<div class='song'>" + data.payload.song_name + "</div>"
          + "<div class='artist'>" + data.payload.artist_ids[0] + "</div>"
          + "</div>"
          + "<div class='buttons'>"
          + "<a href='javascript:;' onclick='new_vote(false, " + data.payload.song_id + ")'><i class='fa fa-chevron-circle-down' id='down'></i></a>"
          + "<a href='javascript:;' onclick='new_vote(true, " + data.payload.song_id + ")'><i class='fa fa-chevron-circle-up' id='up'></i></a>"
          + "</div>"
          + "</div>"

          + "</li>");
        });

      	break;

      case MESSAGE_TYPE.ADDSONG:
        console.log("Addsonging");
        $playlist.append("<li id='" + $("#user_id").val() + "'>" 
          + "<div class='playlistItem'>"
          + "<div class='track'>" 
          + "<div class='song'>" + data.payload.song_name + "</div>"
          + "<div class='artist'>" + data.payload.artist_ids[0] + "</div>"
          + "</div>"
          + "<div class='buttons'>"
          + "<a href='javascript:;' onclick='new_vote(false, " + data.payload.song_id + ")'><i class='fa fa-chevron-circle-down' id='down'></i></a>"
          + "<a href='javascript:;' onclick='new_vote(true, " + data.payload.song_id + ")'><i class='fa fa-chevron-circle-up' id='up'></i></a>"
          + "</div>"
          + "</div>"

          + "</li>");
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
Send VOTESONG message to backend when a user votes on a song - params are boolean vote and song id
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
Send ADDSONG message to backend when a user adds a song
*/
function new_song(songId) {
  // Send a ADDSONG message to the server using `conn`
  console.log("i want to add a song");
  console.log(songId);
  console.log($("#user_id").val());
  let userSuggestion = {"type":MESSAGE_TYPE.ADDSONG, "payload": {
        "id":$("#user_id").val(), 
        "song_id":songId}
      };
  conn.send(JSON.stringify(userSuggestion));
}

/*
Send PLAYLIST message to backend when the last song in the playing block is 10 seconds from finishing
*/
function get_playlist(songId) {
  // Send a PLAYLIST message to the server using `conn`
  let playlistRequest = {"type":MESSAGE_TYPE.PLAYLIST, "payload": {
        "id":$("#user_id").val()}
      };
  conn.send(JSON.stringify(playlistRequest));
}


