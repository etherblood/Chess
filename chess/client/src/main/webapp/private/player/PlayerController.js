"use strict";
function PlayerController(jDiv, selectedPlayer, matchService, ownAccount) {
	let nameDiv = $("<div/>");
	jDiv.append(nameDiv);

	let challengeButton = $("<div/>");
	challengeButton.text("challenge");
	challengeButton.click(function() {
		matchService.createMatch(ownAccount.get().id, selectedPlayer.get().id);
	});
	jDiv.append(challengeButton);
	selectedPlayer.subscribeChangeListener(function(player, oldPlayer) {
		if(player) {
			nameDiv.text(player.name);
			jDiv.show();
		} else {
			jDiv.hide();
		}
	}, true);
}