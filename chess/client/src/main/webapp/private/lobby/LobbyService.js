"use strict";
function LobbyService(httpService, eventService, lobbies) {

	let convertLobby = function(json) {
		let lobby = new LobbyModel(json.id, json.name);
		if (json.members) {
			for (var i = 0; i < json.members.length; i++) {
				let member = new PlayerModel(json.members[i].id,
						json.members[i].name);
				lobby.members.add(member);
			}
		}
		if (json.messages) {
			for (var i = 0; i < json.messages.length; i++) {
				let message = new PlayerModel(json.messages[i].sender,
						json.messages[i].text, json.messages[i].created);
				lobby.messages.add(message);
			}
		}
		return lobby;
	}

	this.run = function() {
		httpService.get("/api/lobby/list").then(function(result) {
			for (var i = 0; i < result.length; i++) {
				let lobby = convertLobby(result[i]);
				lobbies.add(lobby);
			}
		});
	}

	this.createLobby = function(name) {
		httpService.post("/api/lobby/create", name).then(function(result) {
			let lobby = convertLobby(result);
			lobbies.add(lobby);
		});
	}
}
