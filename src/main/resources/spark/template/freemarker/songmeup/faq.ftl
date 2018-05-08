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
         <a class="logout" id="userStatus" href="logout">LOG OUT</a>
         <a id="faqs" href="faq">FAQS</a>
         <a id="home" href="main">HOME</a>
      </div>
      <div class="topSection">
         <div class="title">FAQ</div>

         <button class="accordion">Who can host parties? </button>

         <div class="panel">
            <ab>Only Spotify Premium users.</ab>
         </div>
         <button class="accordion">Who can join parties?</button>
         <div class="panel">
            <ab>Anyone! If you have a Spotify account.</ab>
         </div>
         <button class="accordion">How many parties can I be in at once? </button>
         <div class="panel">
            <ab>One. </ab>
         </div>

         <button class="accordion">Who can add songs? </button>
         <div class="panel">
            <ab>Anyone in the party. </ab>
         </div>
         <button class="accordion">Who can PLAY, PAUSE, and SKIP? </button>
         <div class="panel">
            <ab>Only the host. </ab>
         </div>


         <button class="accordion">How many times can I vote/downvote a song?</button>
         <div class="panel">
            <ab>Only once. </ab>
         </div>
         <button class="accordion">How many parties can I be in at once? </button>
         <div class="panel">
            <ab>One. </ab>
         </div>

         <button class="accordion">How many songs can I request? </button>
         <div class="panel">
            <ab>As many as you’d like!</ab>
         </div>
         <button class="accordion">How are songs prioritised?</button>
         <div class="panel">
            <ab>It's a secret. </ab>
                     <ab> <br>
               Kidding, songs are sorted according to both their votes, as well as similarities between their their audio features. <br>
            </ab>
         </div>


         <script>
            var acc = document.getElementsByClassName("accordion");
            var i;
            
            for (i = 0; i < acc.length; i++) {
                acc[i].addEventListener("click", function() {
                    this.classList.toggle("active");
                    var panel = this.nextElementSibling;
                    if (panel.style.display === "block") {
                        panel.style.display = "none";
                    } else {
                        panel.style.display = "block";
                    }
                });
            }
         </script>



         <div class="more" id="down">
            <a class="arrow" href="#">
            <span class="bottom"></span>
            </a>
         </div>
      </div>
      <div class="info1" id="info">
         <div class="section">
            <h1>SongMeUp</h1>
            <p>is a playlist generating tool that allows partygoers to determine their own music</p>
            <h2>How it works</h2>
            <p>Join a party by entering a party code</p>
            <p>Host a party by logging into Spotify</p>
            <h2>Features</h2>
            <p>Users decide the playlist</p>
            <p>Suggested playlists to get you started</p>
            <p>Save your party to use again</p>
         </div>
      </div>
   </body>
</html>
<!--When user clicks, "Host a party", app asks if they've been there before?-->