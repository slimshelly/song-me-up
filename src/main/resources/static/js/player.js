$(document).ready(() => {

    window.onSpotifyWebPlaybackSDKReady = () => {
        const token = 'BQBZKtICVB-txVBfM7vWR4myRLO1F9f1PfA0GBIr_EeuN-C3sY8s4FBEd64e0ESKauK0wA7e-A9LI-D9sQVycnttz5hWL6gquChBpNF0KxEPkUu490vT4dxZMvOBcvqS36ObqgRxAJGoz2IR-EKTSbM5Wky9BufbXBUmYe1RYltm5V0ruXMSrwKkNg';
        const player = new Spotify.Player({
            name: 'Test Player',
            getOAuthToken: cb => {
                cb(token);
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

        // Ready
        player.addListener('ready', ({
            device_id
        }) => {
            console.log('Ready with Device ID', device_id);
        });

        // Connect to the player!
        player.connect();

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
                spotify_uri: "spotify:track:7xGfFoTpQ2E7fRF5lN10tr",
                playerInstance: player
            });
            // if success, then play

        }

        function successfulConnect() {
            player.connect().then(success => {
                if (success) {
                    console.log('Trying to play song!');

                }
            })
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


    };



});