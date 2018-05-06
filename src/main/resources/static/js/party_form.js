

$(document).ready(() => {

  /*
  On page load, send post request to the backend to get the HOST'S PLAYLISTS
  Hosts have the option (?) of starting the party with one of their Spotify playlists
  */
  $.post("./spotifyPlaylists", responseJSON => {
    const responseObject = JSON.parse(responseJSON);
    console.log(responseObject);
    let output = responseObject;
    console.log(responseObject.playlists);


  });

});