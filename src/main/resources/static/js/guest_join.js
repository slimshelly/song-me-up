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

function playNextSong(songUri) {
  console.log("doing nothing!");
}

function leave_party(info) {
   $leavereason.html(info);
   $modal.css("display","block");
}