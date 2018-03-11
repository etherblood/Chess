"use strict";
function LobbiesView(div, lobbies, selectedLobby) {
	
	let divMap = {};

	let addListener = function(lobby) {
		let lobbyDiv = document.createElement('div');
		lobbyDiv.innerHTML = $.escapeHtml(lobby.name);
		lobbyDiv.addEventListener("click", function(){
			selectedLobby.set(lobby);
		});
		div.appendChild(lobbyDiv);
		divMap[lobby.id] = lobbyDiv;
	};


	let removeListener = function(lobby) {
		let lobbyDiv = divMap[lobby.id];
		div.removeChild(lobbyDiv);
		delete divMap[lobby.id];
	};

	lobbies.subscribeAddListener(addListener, true);
	lobbies.subscribeRemoveListener(removeListener);
	
}