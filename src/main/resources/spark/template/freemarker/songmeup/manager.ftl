<!DOCTYPE html>
<html>
	<body>
		<script src="https://sdk.scdn.co/spotify-player.js"></script>
		<script>
			window.onSpotifyWebPlaybackSDKReady = () => {
  				// You can now initialize Spotify.Player and use the SDK
  				
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