

let player;
let freshToken;
let temp_uri = 'spotify:track:2Th9BGKvfZG8bKQSACitwG';

function playSong(song_uri) {

    play({
        // takes uri passed in and plays with the default player.
        spotify_uri: [temp_uri],
        playerInstance: player
    });

}

function requestNext() {
    //Sent a REQUEST_NEXT_SONG message to the server using 'con'
    request_next_song();
}


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

/*

1. get fresh token
2. initialize the player with token
3. wait until player connects.
4. after it connects, notify the front end, and tell them you can now play songs. 

*/

$(document).ready(() => {

    window.onSpotifyWebPlaybackSDKReady = () => {

        // get first token
        console.log("hello")
        firstTokenAndInitialize();

    };

});


function firstTokenAndInitialize() {

    console.log("in first token and initialize")

    $.post("./refresh", responseJSON => {

        // parse response
        const responseObject = JSON.parse(responseJSON);
        freshToken = responseObject.access_token;
        console.log("the fresh token is " + freshToken);

        // initialize player after new token
        initializePlayer(freshToken);
    });

}

function initializePlayer(token) {

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

                player.addListener('ready', ({
                    device_id
                }) => {
                    console.log('Ready with Device ID', device_id);
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
        });


            }
        });



}
