function updateSuggestions($textBox, $optionList){
	acText = $textBox.val();
	$optionList.empty();
	if(acText.length > 0){
		const postParameters = {acString: acText};
		$.post("/mapac", postParameters, responseJSON => {
			const responseObject = JSON.parse(responseJSON);
			const suggestionList = responseObject.suggestionlist;
			for(const sug of suggestionList){
				$optionList.append('<option value="' + sug + '">');
			};
		});
	}
}

// Waits for DOM to load before running
$(document).ready(() => {
	const $acTextBox0 = $("#road0");
	const $acTextBox1 = $("#crossroad0");
	const $acTextBox2 = $("#road1");
	const $acTextBox3 = $("#crossroad1");
	const $options0 = $("#acroad0");
	const $options1 = $("#accrossroad0");
	const $options2 = $("#acroad1");
	const $options3 = $("#accrossroad1");
	
	//$acTextBox.change(updateSuggestions($acTextBox0, $options0));
	//$acTextBox1.change(updateSuggestions($acTextBox1, $options1));
	$(document).keyup(event => {
		updateSuggestions($acTextBox0, $options0);
		updateSuggestions($acTextBox1, $options1);
		updateSuggestions($acTextBox2, $options2);
		updateSuggestions($acTextBox3, $options3);
	});
});