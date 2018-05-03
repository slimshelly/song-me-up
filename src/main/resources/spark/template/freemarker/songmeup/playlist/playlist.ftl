<ul>
	<#list playlist as track>
		<li>
			<div id="playlistItem">
				<div class="track" id=${track.getId()}>
		      		<div class="song">${track.getName()}</span>
		      		<div class="artist">${track.getArtistIds()}</span>
		    	</div>
		    	</div id="buttons">
		    		<a href="#"><i class="fa fa-chevron-circle-down" id="down"></i></a>
		    		<a href="#"><i class="fa fa-chevron-circle-up" id="up"></i></a>
		    	</div>
	    	</div>
		</li>
	</#list>
</ul>