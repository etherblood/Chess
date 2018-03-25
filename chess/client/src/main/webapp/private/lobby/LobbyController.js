"use strict";
function LobbyController(jDiv, selectedLobby, selectedPlayer, lobbyService) {

	let nameDiv = $("<div/>");
	jDiv.append(nameDiv);

	let detailsDiv = $("<div/>");
	jDiv.append(detailsDiv);
	
	let detailsController = new LobbyDetailsController(detailsDiv, selectedLobby, selectedPlayer, lobbyService);
	
	let lobbyChangeListener = function(lobby, oldLobby) {
		if(lobby) {
			jDiv.show();
			nameDiv.text(lobby.name);
		} else {
			jDiv.hide();
		}
	};
	
	selectedLobby.subscribeChangeListener(lobbyChangeListener, true);

}