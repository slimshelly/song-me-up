let $dropdown;
let $nowPlaying;
let $playingBlock;
let $suggestionBlock;
let $userId;
let $votingBlock;

$(document).ready(() => {
  $dropdown = $("#dropdown");
  $nowPlaying = $(".imgContainer");
  $playingBlock = $("#playing");
  $suggestionBlock = $("#suggestions");
  $userId = $("#user_id").val();
  $votingBlock = $("#voting");

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
        const responseObject = JSON.parse(responseJSON);
        console.log(responseObject);
        let output = responseObject;
        console.log(output);
        let temp_html = "";
        for(const sug of output){
          temp_html = temp_html
            + "<a href='javascript:;' onclick='new_suggest(\"" + sug.id.toString() + "\");'>"
              + "<div class='option'>" + sug.name + "</div>"
            + "</a>";
        }
        console.log(temp_html);
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
  REFRESH_ALL: 7
};

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
    $votingBlock.append(
      "<li id='" + $userId + "'>"
      + "<div class='votingItem'>"
      + "<img class='albumCover' src='" + voteSong.album_cover + "'>"
      + "<div class='track'>"
      + "<div class='song'>" + voteSong.song_name + "</div>"
      + "<div class='artist'>" + voteSong.artist_names[0] + "</div>"
      + "<div class='score'>" + voteSong.score + "</div>"
      + "</div>"
      + "<div class='buttons'>"
      + "<a href='javascript:;' onclick='new_vote(false, \"" + voteSong.song_id + "\")'>"
      + "<i class='fa fa-chevron-circle-down' id='down'></i>"
      + "</a>"
      + "<a href='javascript:;' onclick='new_vote(true, \"" + voteSong.song_id + "\")'>"
      + "<i class='fa fa-chevron-circle-up' id='up'></i>"
      + "</a>"
      + "</div>"
      + "</div>"
      + "</li>");
  });
}

function refresh_playing_block(toPlay) {
  console.log("In function refresh_playing_block");
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