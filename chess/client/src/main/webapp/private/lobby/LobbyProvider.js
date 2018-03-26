"use strict";
function LobbyProvider(httpService, playerCache) {

	let convertLobbyDetails = function(json) {
		if(json.details) {
			let details = new LobbyDetailsModel(json.details.id);
			for (let i = 0; i < json.details.memberIds.length; i++) {
				let memberId = json.details.memberIds[i];
				playerCache.fetchObject(memberId).then(details.members.add);
			}
			
			let senders = {};
			let defers = [];
			
			for (let i = 0; i < json.details.messages.length; i++) {
				let jsonMessage = json.details.messages[i];
				defers.push(playerCache.fetchObject(jsonMessage.senderId).then(function(sender) {
					senders[sender.id] = sender;
				}));
			}
			
			$.when.apply($, defers).then(function() {
				for (let i = json.details.messages.length - 1; i >= 0; i--) {
					let jsonMessage = json.details.messages[i];
					let sender = senders[jsonMessage.senderId];
					let message = new MessageModel(jsonMessage.id, sender, jsonMessage.text, jsonMessage.created);
					details.messages.add(message);
				}
			});
			return details;
		}
		return null;
	}
	
	let convertLobby = function(json) {
		let lobby = new LobbyModel(json.id, json.name);
		lobby.details.set(convertLobbyDetails(json));
		return lobby;
	}

	this.get = function(id) {
		return httpService.get("/api/lobby/" + id).then(convertLobby);
	}
}
