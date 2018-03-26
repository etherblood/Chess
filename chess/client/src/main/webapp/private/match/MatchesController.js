"use strict";
function MatchesController(jDiv, matches, selectedMatch) {
	
	let divMap = {};

	let addListener = function(match) {
		let matchDiv = $("<div/>");
		matchDiv.append($("<span/>", {
			text: match.started
		}));
		matchDiv.append($("<span/>", {
			text: " - "
		}));
		matchDiv.append($("<span/>", {
			text: match.white.name
		}));
		matchDiv.append($("<span/>", {
			text: " vs "
		}));
		matchDiv.append($("<span/>", {
			text: match.black.name
		}));
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