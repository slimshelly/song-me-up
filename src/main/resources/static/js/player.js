$(document).ready(() => {





});

// initialize new player
function new_player() {
	let player = new Spotify.Player({
  name: 'Host Player',
  getOAuthToken: callback => {
    // Run code to get a fresh access token

    callback('access token here');
  },
  volume: 0.5
});
}

////////////////////////////////

// play a URL
const play_song = ({
  spotify_uri,
  playerInstance: {
    _options: {
      getOAuthToken,
      id
    }
  }
}) => {
  getOAuthToken(access_token => {
    fetch(`https://api.spotify.com/v1/me/player/play?device_id=${id}`, {
      method: 'PUT',
      // I need access token here
      body: JSON.stringify({ uris: [spotify_uri] }),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${access_token}`
      },
    });
  });
};


play({
  playerInstance: new Spotify.Player({ name: "..." }),
  spotify_uri: 'spotify:track:7xGfFoTpQ2E7fRF5lN10tr',
});

//////////////////////////////


  player.connect().then(success => {
  if (success) {
    console.log('The Web Playback SDK successfully connected to Spotify!');
  }
})

//////////////////////////

  player.disconnect()

  player.addListener('ready', ({ device_id }) => {
  console.log('The Web Playback SDK is ready to play music!');
  console.log('Device ID', device_id);
})

  // Removes all "ready" events
player.removeListener('ready');
// Remove a specific "ready" listener
player.removeListener('ready', yourCallback)


player.getCurrentState().then(state => {
  if (!state) {
    console.error('User is not playing music through the Web Playback SDK');
    return;
  }

  let {
    current_track,
    next_tracks: [next_track]
  } = state.track_window;

  console.log('Currently Playing', current_track);
  console.log('Playing Next', next_track);
});


player.getVolume().then(volume => {
  let volume_percentage = volume * 100;
  console.log(`The volume of the player is ${volume_percentage}%`);
});

player.setVolume(0.5).then(() => {
  console.log('Volume updated!');
});

player.pause().then(() => {
  console.log('Paused!');
});

player.resume().then(() => {
  console.log('Resumed!');
});

player.togglePlay().then(() => {
  console.log('Toggled playback!');
});

// Seek to a minute into the track
player.seek(60 * 1000).then(() => {
  console.log('Changed position!');
});

player.previousTrack().then(() => {
  console.log('Set to previous track!');
});

player.nextTrack().then(() => {
  console.log('Skipped to next track!');
});

player.addListener('ready', ({ device_id }) => {
  console.log('Connected with Device ID', device_id);
});

player.addListener('player_state_changed', ({
  position,
  duration,
  track_window: { current_track }
}) => {
  console.log('Currently Playing', current_track);
  console.log('Position in Song', position);
  console.log('Duration of Song', duration);
});