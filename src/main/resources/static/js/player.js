let player;
let token;
let freshToken;
let temp_uri = 'spotify:track:2Th9BGKvfZG8bKQSACitwG';
let paused = false;
let started = false;

let nextSongInterval = null;

function showCurrentState() {
  player.getCurrentState().then(state => {
    if (!state) {
      console.error('User is not playing music through the Web Playback SDK');
      return;
    }
    let {
      current_track,
      next_tracks: [next_track]
    } = state.track_window;
    let currPos = state.position;
    let currLength = state.track_window.current_track.duration_ms;
    // console.log(state.position);
    // console.log(state.track_window.current_track.duration_ms);
    if (currLength - currPos <= 1000) {
      console.log("end of song");
      started = false;
      paused = false;
      requestNext();
    }
  });
}

function checkState() {
  showCurrentState();
}

function playSomething() {
  console.log("play song called")
  if (started === false && paused === false) {
    // no song is played, request next song
    console.log("no current song, request next song");
    requestNext();
  }
  if (started === true ) {
    if (paused === true) {
      resumeSong();
    } else {
      pauseSong();
    }
  }
}

function playSong(song_uri) {
  console.log("tryna play this song")
  if (nextSongInterval != null) {
    window.clearInterval(nextSongInterval);
  }
  nextSongInterval = window.setInterval(checkState, 1000);
  if (started === false) {
    play({
      // takes uri passed in and plays with the default player.
      spotify_uri: [song_uri],
      playerInstance: player
    });
    started = true;
  } else {
    // started, determine if paused or playing
    if (paused === true) {
      resumeSong();
    } else {
      pauseSong();
    }
  }
}

function playNextSong(songUri) {
  console.log("the paused state is: " + paused);
  console.log("the started state is" + started);
  // reset conditions
  paused = false;
  started = false;
  console.log("playing next");
  playSong(songUri);
}

function requestNext() {
  //Sent a REQUEST_NEXT_SONG message to the server using 'con'
  request_next_song();
}

const play = ({spotify_uri, playerInstance: {_options: {getOAuthToken, id}}}) => {
  getOAuthToken(access_token => {
    fetch(`https://api.spotify.com/v1/me/player/play?device_id=${id}`, {
      method: 'PUT',
      body: JSON.stringify({uris: spotify_uri}),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${access_token}`
      },
    });
  });
};

$(document).ready(() => {
  window.onSpotifyWebPlaybackSDKReady = () => {
    // get first token
    console.log("hello")
    setUp();
  };
});

function setUp() {
  $.post("./refresh", responseJSON => {
    // parse response
    const responseObject = JSON.parse(responseJSON);
    freshToken = responseObject.access_token;
    // initialize player after new token
    token = freshToken;
    player = new Spotify.Player({
      name: 'Host Player',
      getOAuthToken: cb => {
        cb(token);
      }
    });
    // Connect to the player!
    player.connect();
    player.connect().then(success => {
      if (success) {
        player.addListener('ready', ({device_id}) => {
          console.log('Ready with Device ID', device_id);
        });
        // Error handling
        player.addListener('initialization_error', ({message}) => {
          console.error(message);
        });
        player.addListener('authentication_error', ({message}) => {
          console.error(message);
        });
        player.addListener('account_error', ({message}) => {
          console.error(message);
        });
        player.addListener('playback_error', ({message}) => {
          console.error(message);
        });
      }
    });
  });
}

function seek() {
  player.seek(240000).then(() => {
    console.log('Changed position!');
  });
}

function resumeSong() {
  player.resume().then(() => {
    console.log('Resumed!');
    paused = false;
  });
}

function pauseSong() {
  player.pause().then(() => {
    console.log('Paused!');
    paused = true;
  });
}
