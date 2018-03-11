"use strict";
function LobbyProvider(httpService) {
	
	this.get = function(ids) {
		return httpService.get("/api/lobby/lobbies", ids).then(function(lobbies) {
			let result = [];
			for (let i = 0; i < lobbies.length; i++) {
				let lobby = lobbies[i];
				result.push(new LobbyModel(lobby.name));
			}
			return result;
		});
	}
	
	this.id = function(lobby) {
		return lobby.id;
	}
}
