<!DOCTYPE html>
<html>

<body>
    <script src="js/jquery-3.1.1.js"></script>
    <script src="https://sdk.scdn.co/spotify-player.js"></script>
    <script src="js/player.js"></script>

    <div class="title">Song Me Up</div>
    <br>
    <div class="buttons">
        <a class="btn" onclick="playSong()">Start Song</a> <br>
        <a class="btn" onclick="pauseSong()">Pause Song</a><br>
        <a class="btn" onclick="resumeSong()">Resume Song</a><br>
        <a class="btn" onclick="togglePlaySong()">Toggle Play Song</a><br>
        <a class="btn" onclick="switchToPrevious()">Previous Song</a><br>
        <a class="btn" onclick="switchToNext()">Next Song</a><br>
        <a class="btn" onclick="seekPosition()">Move to a minute</a><br>
        <a class="btn" onclick="updateSongList()">UpdateList</a><br>
        <a class="btn" onclick="showCurrentState()">showState</a><br>
    </div>
</body>

</html>