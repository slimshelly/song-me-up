// run below code only on first page load
window.onload = function () {
  if (localStorage.getItem("hasCodeRunBefore") === null) {
    document.getElementById("modal_query").style.display = "normal";
    localStorage.setItem("hasCodeRunBefore", true);
  }
};

$(document).ready(() => {
  $.post("./playlist", responseJSON => {
    const responseObject = JSON.parse(responseJSON);
    console.log(responseObject);
    let output = responseObject;
    refresh_suggestions_block(output.suggest); //output.suggest are all Suggestion objects
    refresh_voting_block(output.vote);
    refresh_playing_block(output.play);
    if (!jQuery.isEmptyObject(output.now_playing)) {
      refresh_now_playing(output.now_playing.album_cover, output.now_playing.song_name, output.now_playing.artist_names);
      setUp();
      playNextSong(output.now_playing.uri);
    }
  });
});

let conn;

// Setup the WebSocket connection for live updating of scores.
const setup_live_playlist = () => {
  let completepath = window.location.host + window.location.pathname;
  let partpath = completepath.substring(0,completepath.lastIndexOf("/"));
  let type = "ws";
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
        add_suggestion(data.payload);
        break;
      case MESSAGE_TYPE.REFRESH_SUGG:
        console.log("[HOST] Recieved REFRESH_SUGG message (Legal but not used!)");
        refresh_suggestions_block(data.payload);
        break;
      case MESSAGE_TYPE.VOTESONG:
        console.log("[HOST] Recieved VOTESONG message (ILLEGAL!)");
        break;
      case MESSAGE_TYPE.REFRESH_VOTE:
        console.log("[HOST] Recieved REFRESH_VOTE message");
        refresh_voting_block(data.payload);
        break;
      case MESSAGE_TYPE.NEXT_SONG:
        console.log("[HOST] Recieved NEXT_SONG message");
        playNextSong(data.payload.uri);
        refresh_now_playing(data.payload.album_cover, data.payload.song_name, data.payload.artist_names);
        break;
      case MESSAGE_TYPE.REFRESH_PLAY:
        console.log("[HOST] Recieved REFRESH_PLAY message");
        refresh_playing_block(data.payload);
        break;
      case MESSAGE_TYPE.REFRESH_ALL:
        console.log("[HOST] Recieved REFRESH_ALL message");
        refresh_all(data.payload);
        break;
    }
  };
};

function request_next_song() {
  console.log("In function request_next_song");
  let request = {
    "type":MESSAGE_TYPE.NEXT_SONG,
    "payload": { "id": $userId , "song_id": ""}
  };
  conn.send(JSON.stringify(request));
  console.log("[HOST] Sent NEXT_SONG message");
}

function togglePlay() {
  console.log("In function togglePlay");
  // do nothing if no song playing
  if ( isEmpty($nowPlaying) ) {
    return;
  }
  // otherwise, toggle
  let element = document.getElementById("playPause");
  element.classList.toggle("fa-play");
  element.classList.toggle("fa-pause");
}

function isEmpty( el ){
  return !$.trim(el.html())
}

/*
MOVE PARTY CODE TO TOP LEFT OF PAGE
*/
function remove(element) {
  let $code = $("#modal_query");

  var op = 1;  // initial opacity
  var timer = setInterval(function () {
    if (op <= 0.1){
      clearInterval(timer);
      element.style.display = 'none';
    }
    element.style.opacity = op;
    element.style.filter = 'alpha(opacity=' + op * 100 + ")";
    op -= op * 0.1;
  }, 50);
}

function move_code() {
  let element = document.getElementById('party_code');
  // element.animate({height: "300px"});
}
