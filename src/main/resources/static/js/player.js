let freshToken = 'BQB3CqWXLu5QDsBpCTeMeVDUnsSW74HOBouH-nkcz20dOIZRbAKCPsstVOrsViQSuivgWDvNMmdk3CuKZw48PoH6NzFudHclaKdj7IqPMUhLlteLNZpXQwbvDNPOFu5Tk8Y4zGxwyDLPaOKYIqfga1T3c00JFuRJg0CUTTULDLCG9qu25X43GUA-wA';
let player;
let song_list = ['spotify:track:2Th9BGKvfZG8bKQSACitwG','spotify:track:4pYd8dIohiqc3KsxSRqf0w'];


function updateSongList() {
    song_list = ['spotify:track:087OBLtoeS3Q6j0k6tMNAI','spotify:track:7E390nZTMqEbrNC1TmHd42'];
        play({
        spotify_uri: song_list,
        playerInstance: player
    });
}

$(document).ready(() => {

    window.onSpotifyWebPlaybackSDKReady = () => {
        const token = freshToken;
        player = new Spotify.Player({
            name: 'Test Player',
            getOAuthToken: cb => {
                cb(token);
            }
        });

        // Connect to the player!
        player.connect();


        player.connect().then(success => {
            if (success) {

                player.addListener('ready', ({
                    device_id
                }) => {
                    console.log('Ready with Device ID', device_id);
                });


            }
        });


        // Error handling
        player.addListener('initialization_error', ({
            message
        }) => {
            console.error(message);
        });
        player.addListener('authentication_error', ({
            message
        }) => {
            console.error(message);
        });
        player.addListener('account_error', ({
            message
        }) => {
            console.error(message);
        });
        player.addListener('playback_error', ({
            message
        }) => {
            console.error(message);
        });

        // Playback status updates
        player.addListener('player_state_changed', state => {
            console.log(state.position);
            console.log(state.duration);

                    if (state.position == state.duration && state.track_window.next_tracks.length) {
            updateSongList();
        }
        });


    };

});



const play = ({
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
            body: JSON.stringify({
                uris: spotify_uri
            }),
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${access_token}`
            },
        });
    });
};


function playSong() {

    startTime = (new Date).getTime();
    // plays song from the very beginning
    play({
        spotify_uri: song_list,
        playerInstance: player
    });

    console.log("done play")
    // if success, then play
}


function pauseSong() {

    player.pause().then(() => {
        console.log('Paused!');
    });

}


function resumeSong() {
    player.resume().then(() => {
        console.log('Resumed!');
    });
}

function togglePlaySong() {

// if song is playing - toggle play song. else, start playing song

    player.togglePlay().then(() => {
        console.log('Toggled playback!');
    });
}


function switchToNext() {
    player.nextTrack().then(() => {
        console.log('Skipped to next track!');
    });
}

function switchToPrevious() {
    player.previousTrack().then(() => {
  console.log('Set to previous track!');
});
}

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

  console.log(state.position);
  console.log(state.track_window.next_tracks.length);
  console.log('Currently Playing', current_track);
  console.log('Playing Next', next_track);

  // if (next_track == null && ) {
  //   updateSongList();
  // } 
});

}


