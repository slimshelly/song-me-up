$(document).ready(() => {
  $.post("./playlist", responseJSON => {
    const responseObject = JSON.parse(responseJSON);
    console.log("./playlist post request");
    console.log(responseObject);
    let output = responseObject;
    refresh_all(output);
    if (!jQuery.isEmptyObject(output.nowPlaying)) {
      refresh_now_playing(output.now_playing.album_cover,
        output.now_playing.song_name,
        output.now_playing.artist_names);
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
    console.log("[GUEST] Revieved a message: " + data.type);
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
        add_suggestion(data.payload);
        break;
      case MESSAGE_TYPE.REFRESH_SUGG:
        console.log("[GUEST] Recieved REFRESH_SUGG message (Legal but not used!)");
        refresh_suggestions_block(data.payload);
        break;
      case MESSAGE_TYPE.VOTESONG:
        console.log("[GUEST] Recieved VOTESONG message (ILLEGAL!)");
        break;
      case MESSAGE_TYPE.REFRESH_VOTE:
        console.log("[GUEST] Recieved REFRESH_VOTE message");
        refresh_voting_block(data.payload);
        break;
      case MESSAGE_TYPE.NEXT_SONG:
        console.log("[GUEST] Recieved NEXT_SONG message");
        refresh_now_playing(data.payload.album_cover, data.payload.song_name, data.payload.artist_names);
        break;
      case MESSAGE_TYPE.REFRESH_PLAY:
        console.log("[GUEST] Recieved REFRESH_PLAY message");
        refresh_playing_block(data.payload);
        break;
      case MESSAGE_TYPE.REFRESH_ALL:
        console.log("[GUEST] Recieved REFRESH_ALL message");
        refresh_all(data.payload);
        break;
    }
  };
};
