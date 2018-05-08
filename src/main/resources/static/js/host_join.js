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
    refresh_all(output);
    if (!jQuery.isEmptyObject(output.now_playing)) {
      refresh_now_playing(output.now_playing.album_cover, output.now_playing.song_name, output.now_playing.artist_names);
      setUp();
      playNextSong(output.now_playing.uri);
    }
  });
});

function togglePlay() {
  console.log("In function togglePlay");
  // do nothing if no song playing
  if (isEmpty($nowPlaying) ) {
    return;
  }
  // otherwise, toggle
  let element = document.getElementById("playPause");
  element.classList.toggle("fa-play");
  element.classList.toggle("fa-pause");
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
