"use strict";
function MatchesController(jDiv, matches, selectedMatch) {
	
	let divMap = {};

	let addListener = function(match) {
		let matchDiv = $("<div/>");
		matchDiv.text(match.id);
		matchDiv.click(function(){
			selectedMatch.set(match);
		});
		jDiv.append(matchDiv);
		divMap[match.id] = matchDiv;
	};


	let removeListener = function(match) {
		divMap[match.id].remove();
		delete divMap[match.id];
	};

	matches.subscribeAddListener(addListener, true);
	matches.subscribeRemoveListener(removeListener);
	
}