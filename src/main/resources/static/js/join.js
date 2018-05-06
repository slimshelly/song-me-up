
let $playlist;
let $votingBlock;
let $playingBlock;
let $results;
let $nowPlaying;

$(document).ready(() => {

  // access playlist to add songs to later
  $playlist = $("#suggestions");
  $votingBlock = $("#voting");
  $playingBlock = $("#playing");
  $results = $("#dropdown");
  $nowPlaying = $(".imgContainer");

  /*
  On page load, send post request to the backend to get CURRENT VERSION OF PLAYLIST
  POST REQUEST IS EMPTY AFTER 2 REFRESHES - this is BAD
  */
  $.post("./playlist", responseJSON => {
    const responseObject = JSON.parse(responseJSON);
    console.log("./playlist post request");
    console.log(responseObject);
    let output = responseObject;
    refresh_suggestions_block(output.suggest); //output.suggest are all Suggestion objects
    refresh_voting_block(output.vote);
    refresh_playing_block(output.play);
  });

	/*
	Toggle color for up and down buttons
	*/
	$("#down").click(function () {
    console.log("down clicked");
		new_vote(false,"SongId"); //TODO: pretty sure this is literally passing the string "SongId" instead of the actual value
		if (document.getElementById("up").classList.contains("upColor")) {
			$("#up").toggleClass("upColor");
		}
		$("#down").toggleClass("downColor");
	});

	$("#up").click(function () {
    console.log("up clicked");
		new_vote(true,"SongId");
		if (document.getElementById("down").classList.contains("downColor")) {
			$("#down").toggleClass("downColor");
		}
		$("#up").toggleClass("upColor");
	});

	/*
	Generate song suggestions based on user input. Send POST request on each key press inside search bar.
	*/
  $("#playlist").keyup(event => {
    	let song = document.getElementById('songName').value;
    	console.log(song);

	    if (song.length === 0) {
	    	$results.hide();
	    }
	    else {
        $results.show();
		   	const postParameters = {word: song};
		    console.log(postParameters);
			  $results.empty();
		    // send input to backend to generate song suggestions
		    $.post("./suggestions", postParameters, responseJSON => {

				const responseObject = JSON.parse(responseJSON);
				console.log(responseObject);
				let output = responseObject;

				for(const sug of output){
          $results.append("<a href='javascript:;' onclick='new_song(\"" + sug.id.toString() + "\");'><div class='option'>" + sug.name + "</div></a>");
        };
		  });
		}
  });

  $results.click(function () {
    $results.hide(); // MAKE SMOOTH
  });


});

/*
Open socket for web communication and deal with different messages
*/
const MESSAGE_TYPE = {
    CONNECT: 0,
    SUGGEST: 1,
    REFRESH_SUGG: 2,
    VOTESONG: 3,
    REFRESH_VOTE: 4,
    NEXT_SONG: 5,
    REFRESH_PLAY: 6,
    REFRESH_ALL: 7
};
let conn;

// Setup the WebSocket connection for live updating of scores.
const setup_live_playlist = () => {
  // TODO Create the WebSocket connection and assign it to `conn`
  let completepath = window.location.host + window.location.pathname;
  let partpath = completepath.substring(0,completepath.lastIndexOf("/"));
  let type = "ws"
  if(window.location.host==="cs.hiram.edu"){
	 type = type + "s";
  }

  conn = new WebSocket(type + "://"+ partpath + "/songupdates");

  conn.onerror = err => {
    console.log('Connection error:', err);
  };

  conn.onmessage = msg => {
    const data = JSON.parse(msg.data);
    console.log("[GUEST] Revieved a message: " + data.type);
    // console.log(data);
    switch (data.type) {
      default:
                console.log('Unknown message type!', data.type);
        break;
      case MESSAGE_TYPE.CONNECT:
        console.log("[GUEST] Recieved CONNECT message");
        new_connect();
        break;
      case MESSAGE_TYPE.SUGGEST:
        console.log("[GUEST] Recieved SUGGEST message");

        // Check if there is a song currently playing, if not, start playing the added song
        // This should ONLY happen when there is nothing in the playlist and a song is added
        // Otherwise, the javascript should know to call getNextSong() before a song finishes to trigger the next song

        // if ($(".imgContainer").find(".artistInfo").length === 0){
        //   $nowPlaying.append("<img class='albumArt' src='" + data.payload.album_cover + "'>");
        //   $nowPlaying.append("<div class='artistInfo'>"
        //     + "<span class='now'>Now Playing</span>"
        //     + "<span class='trackName'>" + data.payload.song_name + "</span>"
        //     + "<span class='artistName'>" + data.payload.artist_names[0] + "</span>"
        //     + "</div>"
        //     );
        //   break; // don't put song in playlist - are we sure about this?
        // }

        // otherwise, if a song is playing, add song to suggestions list
        $playlist.append("<li id='" + $("#user_id").val() + "'>"
          + "<div class='suggestingItem'>"
          + "<img class='albumCover' src='" + data.payload.album_cover + "'>"
          + "<div class='track'>"
          + "<div class='song'>" + data.payload.song_name + "</div>"
          + "<div class='artist'>" + data.payload.artist_names[0] + "</div>"
          + "</div>"
          + "<div class='buttons'>"
          + "<a href='javascript:;' ><i class='fa fa-chevron-circle-down' id='down_disabled'></i></a>"
          + "<a href='javascript:;' ><i class='fa fa-chevron-circle-up' id='up_disabled'></i></a>"
          + "</div>"
          + "</div>"
          + "</li>");
        break;
      case MESSAGE_TYPE.REFRESH_SUGG:
        console.log("[GUEST] Recieved REFRESH_SUGG message (Legal but not used!)");
        break;
      case MESSAGE_TYPE.VOTESONG:
        console.log("[GUEST] Recieved VOTESONG message (ILLEGAL!)");
        break;
      case MESSAGE_TYPE.REFRESH_VOTE:
        console.log("[GUEST] Recieved REFRESH_VOTE message");
        let votingList = data.payload;
        console.log(votingList);
        refresh_voting_block(votingList);
        break;
      case MESSAGE_TYPE.NEXT_SONG:
        console.log("[GUEST] Recieved NEXT_SONG message");
        let song = data.payload;
        let song_uri = song.uri;
        let song_cover = song.album_cover;
        let song_name = song.song_name;
        let song_artists = artist_names;
        playSong(song_uri);
        refresh_now_playing(song_cover, song_name, song_artists);
        break;
      case MESSAGE_TYPE.REFRESH_PLAY:
        console.log("[GUEST] Recieved REFRESH_PLAY message");
        let playingList = data.payload;
        // // loop through json objects in payload
        // playingList.forEach(function(suggestion) {
        //   $playingBlock.append("<li id='" + $("#user_id").val() + "'>"
        //     + "<div class='playingItem'>"
        //     + "<img class='albumCover' src='" + data.payload.album_cover + "'>"
        //     + "<div class='track'>"
        //     + "<div class='song'>" + suggestion.song_name + "</div>"
        //     + "<div class='artist'>" + suggestion.artist_names[0] + "</div>"
        //     + "</div>"
        //     + "<div class='buttons'>"
        //     + "<a href='javascript:;' ><i class='fa fa-chevron-circle-down' id='down_disabled'></i></a>"
        //     + "<a href='javascript:;' ><i class='fa fa-chevron-circle-up' id='up_disabled'></i></a>"
        //     + "</div>"
        //     + "</div>"
        //     + "</li>");
        // });
        refresh_playing_block(playingList);
        break;
      case MESSAGE_TYPE.REFRESH_ALL:
        console.log("[GUEST] Recieved REFRESH_ALL message");
        let toPlay = data.payload.play;
        let toVote = data.payload.vote;
        let toSugg = data.payload.sugg;
        refresh_playing_block(toPlay);
        refresh_voting_block(toVote);
        refresh_suggestions_block(toSugg);
        break;
    }
  };
}


function new_connect(){
	  let vote = {"type":MESSAGE_TYPE.CONNECT, "payload": {
        "id":$("#user_id").val()}
      };
  conn.send(JSON.stringify(vote));
  console.log("[GUEST] Sent CONNECT message");
}

/*
Send VOTESONG message to backend when a user votes on a song - params are boolean vote and song id
*/
function new_vote(vote_boolean, songId){
  // Send a VOTESONG message to the server using `conn`
  let vote = {"type": MESSAGE_TYPE.VOTESONG, "payload": {
        "id":$("#user_id").val(), 
        "song_id":songId,
        "vote":vote_boolean}
      };
  conn.send(JSON.stringify(vote));
  console.log("[GUEST] Sent VOTESONG message");
}

/*
Send ADDSONG message to backend when a user adds a song
*/
function new_song(songId) {
  // Send a ADDSONG message to the server using `conn`
  // console.log("i want to add a song");
  // console.log(songId);
  // console.log($("#user_id").val());
  let userSuggestion = {"type": MESSAGE_TYPE.SUGGEST, "payload": {
        "id":$("#user_id").val(), 
        "song_id":songId}
      };
  console.log(JSON.stringify(userSuggestion));
  conn.send(JSON.stringify(userSuggestion));
  console.log("[GUEST] Sent SUGGEST message");
}

function request_next_song() {
  //Sent a REQUEST_NEXT_SONG message to the server using 'con'
  console.log("requesting next song");
  let request = {"type": MESSAGE_TYPE.NEXT_SONG,
    "payload": { "id": "", "song_id": ""}
  };
  conn.send(JSON.stringify(request));
  console.log("[GUEST] Sent NEXT_SONG message");
}

/*
Refresh suggestions in the playlist (bottom block)
*/
function refresh_suggestions_block(toSuggest) {
  console.log("[GUEST] In function refresh_suggestions_block");
  $playlist.empty();
  toSuggest.forEach(function(suggestion) {
    console.log(suggestion);
      $playlist.append("<li id='" + $("#user_id").val() + "'>"
          + "<div class='suggestingItem'>"
          + "<img class='albumCover' src='" + suggestion.album_cover + "'>"
          + "<div class='track'>"
          + "<div class='song'>" + suggestion.song_name + "</div>"
          + "<div class='artist'>" + suggestion.artist_names[0] + "</div>"

          + "</div>"
          + "<div class='buttons'>"
          + "<a href='javascript:;' ><i class='fa fa-chevron-circle-down' id='down_disabled'></i></a>"
          + "<a href='javascript:;' ><i class='fa fa-chevron-circle-up' id='up_disabled'></i></a>"
          + "</div>"
          + "</div>"

          + "</li>");
  });
}

/*
Refresh songs being voted on in the playlist (middle block)
*/
function refresh_voting_block(toVote) {
  console.log("[GUEST] In function refresh_voting_block");
  $votingBlock.empty();
  toVote.forEach(function(voteSong) {
    $votingBlock.append("<li id='" + $("#user_id").val() + "'>"
      + "<div class='votingItem'>"
      + "<img class='albumCover' src='" + voteSong.album_cover + "'>"
      + "<div class='track'>"
      + "<div class='song'>" + voteSong.song_name + "</div>"
      + "<div class='artist'>" + voteSong.artist_names[0] + "</div>"
      + "<div class='score'>" + voteSong.score + "</div>"

      + "</div>"
      + "<div class='buttons'>"
      + "<a href='javascript:;' onclick='new_vote(false, \"" + voteSong.song_id + "\")'><i class='fa fa-chevron-circle-down' id='down'></i></a>"
      + "<a href='javascript:;' onclick='new_vote(true, \"" + voteSong.song_id + "\")'><i class='fa fa-chevron-circle-up' id='up'></i></a>"
      + "</div>"
      + "</div>"

      + "</li>");
  });
}

/*
Refresh songs being played in the playlist (top block)
*/
function refresh_playing_block(toPlay) {
  console.log("[GUEST] In function refresh_playing_block");
    $playingBlock.empty();
  // put top song in toPlay in now playing block
  console.log(toPlay);
  console.log(toPlay[0]);
  // if ($(".imgContainer").find(".artistInfo").length === 0){
  //   $nowPlaying.append("<img class='albumArt' src='" + data.payload.album_cover + "'>");
  //   $nowPlaying.append("<div class='artistInfo'>"
  //     + "<span class='now'>Now Playing</span>"
  //     + "<span class='trackName'>" + data.payload.song_name + "</span>"
  //     + "<span class='artistName'>" + data.payload.artist_names[0] + "</span>"
  //     + "</div>"
  //     );
  //   break; // don't put song in playlist - are we sure about this?
  // }

  // put rest of songs at top of playlist
  toPlay.forEach(function(playSong) {
    $playingBlock.append("<li id='" + $("#user_id").val() + "'>"
      + "<div class='playingItem'>"
      + "<img class='albumCover' src='" + playSong.album_cover + "'>"
      + "<div class='track'>"
      + "<div class='song'>" + playSong.song_name + "</div>"
      + "<div class='artist'>" + playSong.artist_names[0] + "</div>"

      + "</div>"
      + "<div class='buttons'>"
      + "<a href='javascript:;' ><i class='fa fa-chevron-circle-down' id='down_disabled'></i></a>"
      + "<a href='javascript:;' ><i class='fa fa-chevron-circle-up' id='up_disabled'></i></a>"
      + "</div>"
      + "</div>"

      + "</li>");
  });
}

function isEmpty( el ){
    return !$.trim(el.html())
}

/*
Update currently playing song at top of page.
*/
function refresh_now_playing(song_cover, song_name, song_artists) {
  // show multiple artists!!
  console.log(toSuggest);
  $nowPlaying.append("<img class='albumArt' src='" + song_cover + "'>");
  $nowPlaying.append("<div class='artistInfo'>"
    + "<span class='now'>Now Playing</span>"
    + "<span class='trackName'>" + song_name + "</span>"
    + "<span class='artistName'>" + song_artists[0] + "</span>"
    + "</div>"
    );
}


