<div class="form">
	<form id="mapForm">
		<p>Pick a start and end point on the map:</p>
		<div class="button">
			<input type="button" onclick="findRoutePoint()" value="Find Route" class="mapbutton"></button>
		</div>
	</form>
	<form id="pathForm">
		<div class="inputone">
			Start Road: <input list="acroad0" name="road0" id="road0" required><br>
			<datalist id="acroad0">
			</datalist>
			Start Crossroad: <input list="accrossroad0" name="crossroad0" id="crossroad0" required><br>
			<datalist id="accrossroad0">
			</datalist>
		
			End Road: <input list="acroad1" name="road1" id="road1" required><br>
			<datalist id="acroad1">
			</datalist>
			End Crossroad: <input list="accrossroad1" name="crossroad1" id="crossroad1" required><br>
			<datalist id="accrossroad1">
			</datalist>
		</div>
		<div class="buttonone">
			<input type="button" onclick="findRouteName()" value="Find Route" class="routebutton"></button>
		</div>
	</form>
	<form id="loadForm">
		<div class="inputtwo">
			Filename: <input type="text" name="filename" id="filename" title="Filename" pattern="(([a-zA-Z]:)?(\/?[a-zA-Z0-9_.-]+)+)" required><br>
		</div>
		<div class="buttontwo">
			<input type="button" onclick="loadMap()" value="Load" class="loadbutton"></button>
		</div>
	</form><br><br>
	<div class="buttonthree">
		<input type="button" onclick="helpBox()" value="Need help?" class="helpbutton"></button>
	</div>
	
</div>