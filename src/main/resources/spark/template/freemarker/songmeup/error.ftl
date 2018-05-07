<!DOCTYPE html>
<html>
	<head>
		<script src="js/jquery-3.1.1.js"></script>
		<link rel="stylesheet" type="text/css" href="css/main_style.css">
		<link href="https://fonts.googleapis.com/css?family=Raleway:800,500" rel="stylesheet">
	</head>
	<body>
		<div class="nav-bar">
			<a class="logout" href="logout">LOG OUT</a>
			<a id="faqs" href="faq">FAQS</a>
			<a id="home" href="main">HOME</a>
		</div>
		<div class="title">Error!</div>
		<div class="section">
			<h1>Something went wrong!</h1>
				<p>${errmsg}</p>
			<#if redirect??><p>Maybe you meant to go here?</p>
			<a class="redirect" href=${redirect}>${link_name}</a></#if>
		</div>
	</body>
</html>