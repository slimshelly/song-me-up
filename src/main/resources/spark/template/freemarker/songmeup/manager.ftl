<!DOCTYPE html>
<html>

<body>
    <script src="https://sdk.scdn.co/spotify-player.js"></script>
    <script>
        window.onSpotifyWebPlaybackSDKReady = () => {
            const token = 'BQC8Pw8153QEwu6Bj4DkDgE6IKiJ5Ff2w-tA0J0sUpy0TiiW3tFHuKTXpKxqFEIvONxz1V7xx78VBmtxg5GEUrrCFo4uZoEim-uQqFU7h5ncjVDt6qpWyk6hLOSQ6m5PF7_d6kT0gQCima5rnNj052FsiI5dq93BhcWK4opMUFqpuwpe7ruiw_ANkQ';
            const player = new Spotify.Player({
                name: 'Web Playback SDK Quick Start Player',
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

        };
    </script>
    <div class="title">Song Me Up</div>
    <div class="buttons">
        <a class="btn" onclick="start_player()">Start Player</a>
        <a class="btn" onclick="start_song()">Start Song</a>
        <a class="btn" onclick="pause_song()">Pause Song</a>
        <a class="btn" onclick="prev_song()">Previous Song</a>
        <a class="btn" onclick="next_song()">Next Song</a>
    </div>
</body>

</html>