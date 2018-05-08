let $dropdown;
let $nowPlaying;
let $playingBlock;
let $suggestionBlock;
let $userId;
let $votingBlock;
let $modal;
let $leavereason;

$(document).ready(() => {
  $dropdown = $("#dropdown");
  $nowPlaying = $(".imgContainer");
  $playingBlock = $("#playing");
  $suggestionBlock = $("#suggestions");
  $userId = $("#user_id").val();
  $votingBlock = $("#voting");
  $modal = $("#modal_query");
  $leavereason = $("#leave_reason");
  /*
  Toggle color for up and down buttons
  */
  $("#down").click(function () {
    console.log("down clicked");
    if (document.getElementById("up").classList.contains("upColor")) {
      $("#up").toggleClass("upColor");
    }
    $("#down").toggleClass("downColor");
  });

  $("#up").click(function () {
    console.log("up clicked");
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
      $dropdown.hide();
    }
    else {
      $dropdown.show();
      const postParameters = {word: song};
      console.log(postParameters);
      // $dropdown.empty();
      // send input to backend to generate song suggestions
      $.post("./suggestions", postParameters, responseJSON => {
        let output = JSON.parse(responseJSON);
        let temp_html = "";
        for(const sug of output){
          temp_html = temp_html
            + "<a href='javascript:;' onclick='new_suggest(\"" + sug.id.toString() + "\");'>"
              + "<div class='option'>" + sug.name + "</div>"
            + "</a>";
        }
        $dropdown.html(temp_html);
      });
    }
  });

  // hide dropdown if user clicks a suggestion on it
  $dropdown.click(function () {
    $dropdown.hide(); // MAKE SMOOTH
  });
});

const MESSAGE_TYPE = {
  CONNECT: 0,
  SUGGEST: 1,
  REFRESH_SUGG: 2,
  VOTESONG: 3,
  REFRESH_VOTE: 4,
  NEXT_SONG: 5,
  REFRESH_PLAY: 6,
  REFRESH_ALL: 7,
  LEAVE_PARTY: 8,
  USER_JOINED: 9,
  USER_LEFT: 10,
  PREV_SONG: 11

};

function isEmpty( el ){
  return !$.trim(el.html())
}

let conn;

// Setup the WebSocket connection for live updating of scores.
const setup_live_playlist = () => {
  let completepath = window.location.host + window.location.pathname;
  let partpath = completepath.substring(0,completepath.lastIndexOf("/"));
  let type = "ws";
  if(window.location.host==="smuapp.com"){
    type = type + "s";
  }
  let name = type + "://"+ partpath + "/songupdates"
  conn = new WebSocket(name);
  conn.onerror = err => {
    console.log('Connection error:', err);
  };

  conn.onmessage = msg => {
    const data = JSON.parse(msg.data);
    console.log("Revieved a message: " + data.type);
    // console.log(data);
    switch (data.type) {
      default:
        console.log('Unknown message type!', data.type);
        break;
      case MESSAGE_TYPE.CONNECT:
        console.log("Recieved CONNECT message");
        new_connect(conn);
        break;
      case MESSAGE_TYPE.SUGGEST:
        console.log("ecieved SUGGEST message");
        add_suggestion(data.payload);
        break;
      case MESSAGE_TYPE.REFRESH_SUGG:
        console.log("Recieved REFRESH_SUGG message (Legal but not used!)");
        refresh_suggestions_block(data.payload);
        break;
      case MESSAGE_TYPE.VOTESONG:
        console.log("Recieved VOTESONG message (ILLEGAL!)");
        break;
      case MESSAGE_TYPE.REFRESH_VOTE:
        console.log("Recieved REFRESH_VOTE message");
        refresh_voting_block(data.payload);
        break;
      case MESSAGE_TYPE.NEXT_SONG:
        console.log("Recieved NEXT_SONG message");
        playNextSong(data.payload.uri);
        refresh_now_playing(data.payload.album_cover, data.payload.song_name, data.payload.artist_names);
        togglePlay(); // toggle the play button
        break;
      case MESSAGE_TYPE.REFRESH_PLAY:
        console.log("Recieved REFRESH_PLAY message");
        refresh_playing_block(data.payload);
        break;
      case MESSAGE_TYPE.REFRESH_ALL:
        console.log("Recieved REFRESH_ALL message");
        refresh_all(data.payload);
        break;
      case MESSAGE_TYPE.LEAVE_PARTY:
        console.log("Recieved LEAVE_PARTY message");
        leave_party("The party has ended. Click below to go back to main or join a new party!");
        break;
      case MESSAGE_TYPE.PREV_SONG:
        console.log("Recieved PREV_SONG message");
        playNextSong(data.payload.uri);
        refresh_now_playing(data.payload.album_cover, data.payload.song_name, data.payload.artist_names);
        togglePlay(); // toggle the play button
        break;
    }
  };
};

function leave_party(info) {
   $leavereason.html(info);
   $modal.css("display","block");
}

function request_prev_song() {
  console.log("In function request_prev_song");
  let request = {
    "type":MESSAGE_TYPE.PREV_SONG,
    "payload": { "id": $userId , "song_id": ""}
  };
  conn.send(JSON.stringify(request));
  console.log("Sent PREV_SONG message");
}

function request_next_song() {
  console.log("In function request_next_song");
  let request = {
    "type":MESSAGE_TYPE.NEXT_SONG,
    "payload": { "id": $userId , "song_id": ""}
  };
  conn.send(JSON.stringify(request));
  console.log("Sent NEXT_SONG message");
}

function new_connect(){
  let connect = {
    "type": MESSAGE_TYPE.CONNECT,
    "payload": {"id": $userId}
  };
  conn.send(JSON.stringify(connect));
  console.log("Sent CONNECT message");
}

function new_vote(vote_boolean, songId){
  let vote = {
    "type": MESSAGE_TYPE.VOTESONG,
    "payload": {"id": $userId, "song_id": songId, "vote": vote_boolean}
  };
  conn.send(JSON.stringify(vote));
  console.log("Sent VOTESONG message");
}

function new_suggest(songId) {
  let userSuggestion = {
    "type": MESSAGE_TYPE.SUGGEST,
    "payload": {"id": $userId, "song_id":songId}
  };
  conn.send(JSON.stringify(userSuggestion));
  console.log("Sent SUGGEST message");
}

function add_suggestion(sugg) {
  console.log("In function add_suggestion");
  $suggestionBlock.append(
    "<li id='" + $userId + "'>"
    + "<div class='suggestingItem'>"
    + "<img class='albumCover' src='" + sugg.album_cover + "'>"
    + "<div class='track'>"
    + "<div class='song'>" + sugg.song_name + "</div>"
    + "<div class='artist'>" + sugg.artist_names[0] + "</div>"
    + "</div>"
    + "<div class='buttons'>"
    + "<a href='javascript:;' ><i class='fa fa-chevron-circle-down' id='down_disabled'></i></a>"
    + "<a href='javascript:;' ><i class='fa fa-chevron-circle-up' id='up_disabled'></i></a>"
    + "</div>"
    + "</div>"
    + "</li>");
}

function refresh_now_playing(song_cover, song_name, song_artists) {
  $nowPlaying.empty();
  $nowPlaying.append("<img class='albumArt' src='" + song_cover + "'>");
  $nowPlaying.append(
    "<div class='artistInfo'>"
    + "<span class='now'>Now Playing</span>"
    + "<span class='trackName'>" + song_name + "</span>"
    + "<span class='artistName'>" + song_artists[0] + "</span>"
    + "</div>"
  );
}

function refresh_suggestions_block(toSuggest) {
  console.log("In function refresh_suggestions_block");
  $suggestionBlock.empty();
  toSuggest.forEach(function(suggestion) {
    $suggestionBlock.append(
      "<li id='" + $userId + "'>"
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

function refresh_voting_block(toVote) {
  console.log("In function refresh_voting_block");
  $votingBlock.empty();
  toVote.forEach(function(voteSong) {
    upColorClass = "";
    downColorClass = "";
    if (voteSong.user_vote_status == 1) {
      upColorClass = " upColor"; 
    } else if(voteSong.user_vote_status == -1) {
      downColorClass = " downColor";
    }
    $votingBlock.append(
      "<li id='" + $userId + "'>"

      + "<div class='votingItem'>"
      + "<img class='albumCover' src='" + voteSong.album_cover + "'>"
      
      + "<div class='track'>"
      + "<div class='song'>" + voteSong.song_name + "</div>"
      + "<div class='artist'>" + voteSong.artist_names[0] + "</div>"
      + "</div>"

      + "<div class='buttons'>"
      + "<div class='score'>" + voteSong.score + "</div>"
      + "<a href='javascript:;' class='downbtn' onclick='new_vote(false, \"" + voteSong.song_id + "\")'><i class='fa fa-chevron-circle-down"+ downColorClass + "' id='down'></i></a>"
      + "<a href='javascript:;' class='upbtn' onclick='new_vote(true, \"" + voteSong.song_id + "\")'><i class='fa fa-chevron-circle-up"+ upColorClass + "' id='up'></i></a>"
      + "</a>"
      + "</div>"

      + "</div>"
      + "</li>");
  });
}

function refresh_playing_block(toPlay) {
  console.log("In function refresh_playing_block");
  $playingBlock.empty();
  toPlay.forEach(function(playSong) {
    $playingBlock.append(
      "<li id='" + $userId + "'>"
      + "<div class='playingItem'>"
      + "<img class='albumCover' src='" + playSong.album_cover + "'>"
      + "<div class='track'>"
      + "<div class='song'>" + playSong.song_name + "</div>"
      + "<div class='artist'>" + playSong.artist_names[0] + "</div>"
      + "</div>"
      + "</div>"
      + "</li>");
  });
}

function refresh_all(allBlocks) {
  console.log("In function refresh_all");
  refresh_suggestions_block(allBlocks.sugg);
  refresh_voting_block(allBlocks.vote);
  refresh_playing_block(allBlocks.play);
}