
let $playlists;

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
        + "<img class='albumArt' src='" + playlist.images[0] + "'>"
        + "<div class='middle'><div class='text'>John Doe</div></div>"
        + "</div>");

    });

  });

});