<!DOCTYPE html>
	<head>
		<script src="js/jquery-3.1.1.js"></script>
		<script src="js/join.js"></script>
		<link href="https://fonts.googleapis.com/css?family=Raleway:800,500" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="css/join.css">
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
	</head>
	<body>
		<div class="nav-bar">
			<a class="login" href="/login">SWITCH USER</a>
			<a id="faqs" href="/faq">FAQS</a>
			<a id="home" href="/main">HOME</a>
		</div>

		<!-- IN PROGRESS - not showing up yet -->
		<a class="hamburger menu-btn" href="javascript:void(0)"><span></span></a>

<!-- 		<div class="title">
			<div class="party">Party Name</div>
		</div> -->
		<div class="title">${hostname}'s Party</div>

		<div class="nowPlaying">
			<div class="imgContainer">
				<img class="albumArt" src="photos/flume.jpg">
				<div class="artistInfo">
					<p>Lose It (feat. Vic Mensa)</p>
					<p>Flume</p>
				</div>
			</div>
		</div>

		<div class="content">
			<div class="playlist" id="playlist">
				<!-- SEARCH BAR -->
				<div class="search">
					<button type="submit"><i class="fa fa-search"></i></button>
					<input type="text" placeholder="Add Songs" name="search" id="songName">
				</div>
				<!-- SUGGESTIONS -->
				<div id="dropdown">
					<div id="message">Lorem ipsum dolor sit amet, quas simul aeterno id qui, ne timeam apeirian electram vim, an alterum detracto mea. Ad pri utamur gubergren scripserit, iudico maluisset hendrerit sea te. Omnium labitur omnesque ea qui. At unum elit eos, propriae persequeris delicatissimi quo ut.</div>
				</div>
				<!-- SONGS -->
				<ul>
					<li>
						<div id="playlistItem">
							<div class="track">
								<div class="song">Song name</div>
								<div class="artist">Artist</div>
							</div>
							<div id="buttons">
		    					<a href="#"><i class="fa fa-chevron-circle-down" id="down"></i></a>
		    					<a href="#"><i class="fa fa-chevron-circle-up" id="up"></i></a>
		    				</div>
		    			</div>
					</li>
					<li>
						<div id="playlistItem">
							<div class="track">
								<div class="song">Song name</div>
								<div class="artist">Artist</div>
							</div>
							<div id="buttons">
		    					<a href="#"><i class="fa fa-chevron-circle-down" id="down"></i></a>
		    					<a href="#"><i class="fa fa-chevron-circle-up" id="up"></i></a>
		    				</div>
		    			</div>
					</li>
					<li>
						<div id="playlistItem">
							<div class="track">
								<div class="song">Song name</div>
								<div class="artist">Artist</div>
							</div>
							<div id="buttons">
		    					<a href="#"><i class="fa fa-chevron-circle-down" id="down"></i></a>
		    					<a href="#"><i class="fa fa-chevron-circle-up" id="up"></i></a>
		    				</div>
		    			</div>
					</li>
					<li>
						<div id="playlistItem">
							<div class="track">
								<div class="song">Song name</div>
								<div class="artist">Artist</div>
							</div>
							<div id="buttons">
		    					<a href="#"><i class="fa fa-chevron-circle-down" id="down"></i></a>
		    					<a href="#"><i class="fa fa-chevron-circle-up" id="up"></i></a>
		    				</div>
		    			</div>
					</li>
					<li>
						<div id="playlistItem">
							<div class="track">
								<div class="song">Song name</div>
								<div class="artist">Artist</div>
							</div>
							<div id="buttons">
		    					<a href="#"><i class="fa fa-chevron-circle-down" id="down"></i></a>
		    					<a href="#"><i class="fa fa-chevron-circle-up" id="up"></i></a>
		    				</div>
		    			</div>
					</li>
				</ul>
			</div>
		</div>
	</body>
</html>
