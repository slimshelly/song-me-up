
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

  // hide dropdown if user clicks a suggestion on it
  $("#dropdown").click(function () {
    $("#dropdown").hide(); // MAKE SMOOTH
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
  //let host = window.location.host;
  //conn = new WebSocket("ws://"+ host + "/songupdates");

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
    console.log("[HOST] Revieved a message: " + data.type);
    // console.log(data);
    switch (data.type) {
      default:
        console.log('Unknown message type!', data.type);
        break;
      case MESSAGE_TYPE.CONNECT:
        console.log("[HOST] Recieved CONNECT message");
        new_connect();
        break;
      case MESSAGE_TYPE.SUGGEST:
        console.log("[HOST] Recieved SUGGEST message");
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
        console.log("[HOST] Recieved REFRESH_SUGG message (Legal but not used!)");
        break;
      case MESSAGE_TYPE.VOTESONG:
        console.log("[HOST] Recieved VOTESONG message (ILLEGAL!)");
        break;
      case MESSAGE_TYPE.REFRESH_VOTE:
        let votingList = data.payload;
        console.log("[HOST] Recieved REFRESH_VOTE message");
        refresh_voting_block(votingList);
        break;
      case MESSAGE_TYPE.NEXT_SONG:
        console.log("[HOST] Recieved NEXT_SONG message");
        // data - json object
        let song = data.payload;
        let song_uri = song.uri;
        let song_cover = song.album_cover;
        let song_name = song.song_name;
        let song_artists = song.artist_names;
        playNextSong(song_uri);
        console.log("the song's name is " + song_name);
        console.log("the song's uri is " + song_uri);
        refresh_now_playing(song_cover, song_name, song_artists);
        break;
      case MESSAGE_TYPE.REFRESH_PLAY:
        console.log("[HOST] Recieved REFRESH_PLAY message");
        let playingList = data.payload;
        refresh_playing_block(playingList);
    case MESSAGE_TYPE.REFRESH_ALL:
      console.log("[HOST] Recieved REFRESH_ALL message");
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

/*
Update currently playing song at top of page.
*/
function refresh_now_playing(song_cover, song_name, song_artists) {
  // show multiple artists!!
  // $nowPlaying.imgContainer.artistInfo.empty();

  $nowPlaying.append("<img class='albumArt' src='" + song_cover + "'>");
  $nowPlaying.append("<div class='artistInfo'>"
    + "<span class='now'>Now Playing</span>"
    + "<span class='trackName'>" + song_name + "</span>"
    + "<span class='artistName'>" + song_artists[0] + "</span>"
    + "</div>"
  );
}

function request_next_song() {
    //Sent a REQUEST_NEXT_SONG message to the server using 'con'

    console.log("i just requested in host join")
    let request = {"type":MESSAGE_TYPE.NEXT_SONG,
                   "payload": { "id": $("#user_id").val() , "song_id": ""}
    };
    conn.send(JSON.stringify(request));
  console.log("[HOST] Sent NEXT_SONG message");
}


function new_connect(){
	  let vote = {"type":MESSAGE_TYPE.CONNECT, "payload": {
        "id":$("#user_id").val()}
      };
  conn.send(JSON.stringify(vote));
  console.log("[HOST] Sent CONNECT message");
}

/*
Send VOTESONG message to backend when a user votes on a song - params are boolean vote and song id
*/
function new_vote(vote_boolean, songId){
  // Send a VOTESONG message to the server using `conn`
  console.log("");
  let vote = {"type":MESSAGE_TYPE.VOTESONG, "payload": {
        "id":$("#user_id").val(), 
        "song_id":songId,
        "vote":vote_boolean}
      };
  conn.send(JSON.stringify(vote));
  console.log("[HOST] Sent VOTESONG message");
}

/*
Send ADDSONG message to backend when a user adds a song
*/
function new_song(songId) {
  // Send a SUGGEST message to the server using `conn`
  console.log("i want to add a song");
  console.log("songId == " + songId);
  console.log("$(\"#user_id\").val() == " + $("#user_id").val());
  let userSuggestion = {"type":MESSAGE_TYPE.SUGGEST, "payload": {
        "id":$("#user_id").val(),
        "song_id":songId}
      };
  conn.send(JSON.stringify(userSuggestion));
  console.log("[HOST] Sent SUGGEST message");
}

/*
Refresh suggestions in the playlist (bottom block)
*/
function refresh_suggestions_block(toSuggest) {
  console.log("[HOST] In function refresh_suggestions_block");
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
  console.log("[HOST] In function refresh_voting_block");
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
  console.log("[HOST] In function refresh_playing_block");
  $playingBlock.empty();
  toPlay.forEach(function(playSong) {
    console.log(playSong);
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

function togglePlay() {
  console.log("in");
  let element = document.getElementById("playPause");
  element.classList.toggle("fa-play");
  element.classList.toggle("fa-pause");
  // element.setAttribute("style", "font-size:55px;");
  // element.classList.toggle("fa-pause");
}

