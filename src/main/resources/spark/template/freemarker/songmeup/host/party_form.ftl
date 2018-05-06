<!DOCTYPE html>
<html>
	<head>
		<script src="js/jquery-3.1.1.js"></script>
		<script src="js/party_form.js"></script>
		<link rel="stylesheet" type="text/css" href="css/main_style.css">
		<link rel="stylesheet" type="text/css" href="css/party_form.css">
		<link href="https://fonts.googleapis.com/css?family=Raleway:800,500" rel="stylesheet">
	</head>
	<body>
		<div class="nav-bar">
			<a class="logout" href="logout">LOG OUT</a>
			<a id="faqs" href="faq">FAQS</a>
			<a id="home" href="main">HOME</a>
		</div>
		<div class="title">Choose a playlist to start your party or have guests pick songs right away!</div>
		<form action="/action_page.php">
		  First name:<br>
		  <input type="text" name="firstname" value="Mickey">
		  <br>
		  Last name:<br>
		  <input type="text" name="lastname" value="Mouse">
		</form>
		<div class="buttons">
			<a class="btn" href="./host">GO TO PARTY</a>
		</div>	

		<!-- PLAYLIST OPTIONS -->
		<section>
			<div class="module"></div>
	  		<div class="module"></div>
	  		<div class="module"></div>
	  		<div class="module"></div>
	  		<div class="module"></div>
  		</section>

	</body>
</html>