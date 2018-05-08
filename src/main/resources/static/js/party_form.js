
let $playlists;
let $albumArt;

$(document).ready(() => {

  $playlists = $(".scrolling-wrapper");

  /*
  On page load, send post request to the backend to get the HOST'S PLAYLISTS
  Hosts have the option of starting the party with one of their Spotify playlists
  */
  $.post("./spotifyPlaylists", responseJSON => {
    const responseObject = JSON.parse(responseJSON);
    console.log(responseObject);
    let output = responseObject.playlists;
    console.log(responseObject.playlists);
    
    output.forEach(function(playlist) {
      console.log(playlist);
      $playlists.append("<a href='#' onclick='seed_playlist(\"" + playlist.owner_id + "\",\"" + playlist.id + "\")'><div class='card'>"
        + "<img class='albumArt' src='" + playlist.images[0] + "'>"
        + "<div class='middle' id='" + playlist.id + "'>"
        + "<div class='text'>"
        + "<i class='fas fa-plus fa-3x'></i>"
        + "<br>Add songs"
        + "</div>"
        + "</div>"
        + "</div></a>");
    });
  });

});

/*
Send SEED PLAYLIST to backend to add to playlist
*/
function seed_playlist(owner_id, playlist_id) {
  console.log("in seed playlist");
  const postParameters = {playlist_id: playlist_id, owner_id: owner_id};
  console.log(postParameters);

  $.post("./seedPlaylist", postParameters, responseJSON => {
    const responseObject = JSON.parse(responseJSON);
    console.log("Songs were added!");
    console.log(responseObject);
    let output = responseObject.playlists;
    // if output is received, songs were added to playlist
    // display message that songs were added
    let playlist = document.getElementById(playlist_id);
    $("#" + playlist_id).html("");
    $("#" + playlist_id).css('opacity', 1);
    $("#" + playlist_id).html("<div class='text'>"
        + "Added 10 Songs to Party"
        + "</div>");
    console.log(playlist);


  });
}


