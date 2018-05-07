
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
      $playlists.append("<div class='card'>"
        + "<img class='albumArt' id='" + playlist.id + "' src='" + playlist.images[0] + "' onclick='seed_playlist(\"" + playlist.id + "\")'>"
        + "<div class='middle'><div class='text'>John Doe</div></div>"
        + "</div>");
    });
  });

});

/*
Send SEED PLAYLIST to backend to add to playlist
*/
function seed_playlist(playlist_id) {

  const postParameters = {id: playlist_id};
  console.log(postParameters);

  $.post("./seedPlaylist", postParameters, responseJSON => {
    const responseObject = JSON.parse(responseJSON);
    console.log(responseObject);
    let output = responseObject.playlists;
    console.log(responseObject.playlists);
  });
}

