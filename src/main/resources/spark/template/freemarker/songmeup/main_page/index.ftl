<!DOCTYPE html>
<html>
	<head>
		<script src="js/jquery-3.1.1.js"></script>
		<script src="js/main_jscript.js"></script>
		<link rel="stylesheet" type="text/css" href="css/main_style.css">
		<link href="https://fonts.googleapis.com/css?family=Raleway:800,500" rel="stylesheet">
	</head>
	<body>
		<div class="nav-bar">
			<p>Hello ${name}!</p>
			<p id="premium" hidden>${premium?c}</p>
			<a class="logout" id="userStatus" href="logout">LOG OUT</a>
			<a id="faqs" href="faq">FAQS</a>
			<a id="home" href="main">HOME</a>
		</div>
		<div class="topSection">
			<div class="title">SongMeUp</div>
			<div class="buttons">
				<a class="btn gray" id="host" href="form">HOST A PARTY</a>
				<a class="btn gray" id="joinbtn">JOIN A PARTY</a>
			</div>

			<!-- The Modal -->
			<div id="modal_query" class="modal">

			  <!-- Modal content -->
			  <div class="modal-content">
				<span class="close" id="close_modal">&times;</span>
				<p>Enter the party code:</p>
				<p id="bad_id" hidden>Party code is 6 letters.</p>
				<input class="textfield" type="text" id="party_id_box">
				<a class="btn" id="proceed" onclick="join_id()">LET'S PARTY</a>
			  </div>

			</div>

			<!-- HOW IT WORKS explanation -->
			<div class="more" id="down">
	    		<a class="arrow" href="#">
	      			<span class="bottom"></span>
	    		</a>
	    	</div>
    	</div>
  		<div class="info1" id="info">
  			<div class="section">
	  			<h1>SongMeUp</h1>is a playlist generating tool that allows partygoers to determine their own music
	  			<h2>How it works</h2>
		  			
		  			<div class="instructions">
			  			<div class="instruction">
			  				<div class="frameWrapper">
			  					<img class="frame1" src="photos/host.png">
			  				</div>
			  				<div class="words">Host a party with Spotify Premium</div>
			  			</div>

			  			<div class="instruction">
			  				<div class="frameWrapper">
			  					<img class="frame2" src="photos/code.png">
			  				</div>
			  				<div class="words">Receive a party code and share it with users</div>
		  				</div>

		  				<div class="instruction">
			  				<div class="frameWrapper">
			  					<img class="frame3" src="photos/party.png">
			  				</div>
			  				<div class="words">Crowdsource your playlist!</div>
		  				</div>
	  				</div>
	  		</div>
  		</div>
	</body>
</html>

<!--When user clicks, "Host a party", app asks if they've been there before?-->

