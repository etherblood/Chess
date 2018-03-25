"use strict";
function LobbiesController(jDiv, lobbies, selectedLobby) {
	
	let divMap = {};

	let addListener = function(lobby) {
		let lobbyDiv = $("<div/>");
		lobbyDiv.text(lobby.name);
		lobbyDiv.click(function(){
			selectedLobby.set(lobby);
		});
		jDiv.append(lobbyDiv);
		divMap[lobby.id] = lobbyDiv;
	};


	let removeListener = function(lobby) {
		divMap[lobby.id].remove();
		delete divMap[lobby.id];
	};

	lobbies.subscribeAddListener(addListener, true);
	lobbies.subscribeRemoveListener(removeListener);
	
}