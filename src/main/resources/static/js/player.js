let player;

$(document).ready(() => {

    window.onSpotifyWebPlaybackSDKReady = () => {
        const token = 'BQC3szSha2Vgv13-lTDauc94N-8c3wJdOjSyOkT8wGBpTG_0mDpXRCs8_rRg6cwu_9b1nXMWHi-AFz002XRN-j9MQt08IPW4djPTHeDehVwFVVgxbExGBzRlTAioOvbA_jCJSoTSZjpkGX-5s7GLV0dHow5dFM9F6kDQGK5IxcGcUdLyz_PgLfPy3Q';
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
            console.log(state);
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
                uris: [spotify_uri]
            }),
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${access_token}`
            },
        });
    });
};


function playSong() {
    play({
        spotify_uri: 'spotify:track:3GCL1PydwsLodcpv0Ll1ch',
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
    player.togglePlay().then(() => {
        console.log('Toggled playback!');
    });
}

function switchToPrevious() {
    player.previousTrack().then(() => {
        console.log('Set to previous track!');
    });
}

function switchToNext() {
    player.nextTrack().then(() => {
        console.log('Skipped to next track!');
    });
}