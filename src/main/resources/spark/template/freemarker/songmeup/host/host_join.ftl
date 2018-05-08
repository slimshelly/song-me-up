<!DOCTYPE html>
	<head>
		<script src="js/jquery-3.1.1.js"></script>
		<script src="js/player.js"></script>
		<script src="js/host_join.js"></script>
        <script src="js/join_functions.js"></script>
		<link rel="stylesheet" type="text/css" href="css/party_code.css">
		<link href="https://fonts.googleapis.com/css?family=Raleway:800,500" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="css/join.css">
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
		<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.10/css/all.css" integrity="sha384-+d0P83n9kaQMCwj8F4RJB66tzIwOKmrdb46+porD/OvrJ+37WqIM7UoBtwHO6Nlg" crossorigin="anonymous">
		<link href="https://fonts.googleapis.com/css?family=Lato" rel="stylesheet">
	</head>
	<body>

		<div id = "hello" style="font-size: 30px; font-family: 'Raleway', sans-serif; position:absolute;top:30px;left:35px " > Party ID: ${party_id} </div>
    <script src="https://sdk.scdn.co/spotify-player.js"></script>
		<input type="hidden" id="user_id" value="${user_id}">
		<nav role="navigation" id="navvy">
			<div id="menuToggle">
				<input type="checkbox" />
				<span></span>
				<span></span>
				<span></span>
				<ul id="menu">
					<a href="main"><li>Home</li></a>
					<a onclick="show_listeners()"><li>Listeners</li></a>
					<a href="main?leave=true"><li>End Party</li></a>
					<a href="logout"><li>Log Out</li></a>
				</ul>
			</div>
		</nav>
		<div class="title">${hostname}'s Party</div>

		<!-- The Modal -->
		<div id="modal_query" class="modal">

		  <!-- Modal content -->
		  <div id="modal_cont" class="modal-content">
			<p style="text-align: center;">Here's the code for your party:</p>
			<p id="party_code" style="text-align: center; font-weight: bold; font-size: 24px;">${party_id}</p>
			<p style="text-align: center;">Share this code at any time so that people can suggest and vote on songs!</p>
			<a class="btn" href="#" onclick="remove(document.getElementById('modal_query'));move_code();">GOT IT</a>
		  </div>

		</div>
		
		<!-- The Modal -->
			<div id="modal_users" class="modal">

			  <!-- Modal content -->
			  <div class="modal-content2">
			     <span class="close" id="close_modal">&times;</span>
				 <div id="user_data"></div>
			  </div>

			</div>


		<div class="nowPlaying">
			<div class="imgContainer">
			</div>
		</div>

		<div class="musicControls">
			<i class="fas fa-backward fa-4x" onclick="request_prev_song()"></i>
			<i id="playPause" class="fas fa-play fa-3x" onclick="playSomething();togglePlay();" style="font-size: 70px;"></i>
			<i class="fas fa-forward fa-4x" onclick="request_next_song()"></i>
		</div>

		<div class="content">
			<div class="playlist" id="playlist">
				<!-- SEARCH BAR -->
				<div class="search">
					<button type="submit"><i class="fas fa-plus"></i></button>
					<input type="text" placeholder="Add Songs" name="search" id="songName">
				</div>
				<!-- SUGGESTIONS -->
				<div id="dropdown">
				</div>
				<!-- SONGS -->
				<ul id="displaySongs">
					<div id="playing">
					</div>
					<div id="voting">
					</div>
					<div id="suggestions">
					</div>
				</ul>
			</div>
		</div>
		
	</body>
</html>

<script>
	$(document).ready(function() {
	  setup_live_playlist();
	});
</script>
