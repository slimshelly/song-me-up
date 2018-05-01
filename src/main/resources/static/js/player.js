

	console.log("setting up player");
// initialize new player

	let player = new Spotify.Player({
  name: 'Host Player',
  getOAuthToken: callback => {
    // Run code to get a fresh access token

    callback('BQC8Pw8153QEwu6Bj4DkDgE6IKiJ5Ff2w-tA0J0sUpy0TiiW3tFHuKTXpKxqFEIvONxz1V7xx78VBmtxg5GEUrrCFo4uZoEim-uQqFU7h5ncjVDt6qpWyk6hLOSQ6m5PF7_d6kT0gQCima5rnNj052FsiI5dq93BhcWK4opMUFqpuwpe7ruiw_ANkQ');
  },
  volume: 0.5
});


	console.log("done setting up player");
	console.log()