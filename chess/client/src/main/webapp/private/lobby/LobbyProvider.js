"use strict";
function LobbyProvider(httpService, playerCache) {

	let convertLobby = function(json) {
		let lobby = new LobbyModel(json.id, json.name);
		if(json.details) {
			lobby.details = new LobbyDetailsModel(json.details.id);
			for (let i = 0; i < json.details.memberIds.length; i++) {
				let memberId = json.details.memberIds[i];
				playerCache.fetchObject(memberId).then(lobby.details.members.add);
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
					lobby.details.messages.add(message);
				}
			});
		}
		// if (json.members) {
		// for (var i = 0; i < json.members.length; i++) {
		// let jsonMember = json.members[i];
		// let member = new PlayerModel(jsonMember.id, jsonMember.name);
		// lobby.members.add(member);
		// }
		// }
		// if (json.messages) {
		// for (var i = 0; i < json.messages.length; i++) {
		// let jsonMessage = json.messages[i];
		// let message = new MessageModel(jsonMessage.id, jsonMessage.senderId,
		// jsonMessage.text, jsonMessage.created);
		// lobby.messages.add(message);
		// }
		// }
		return lobby;
	}

	this.get = function(id) {
		return httpService.get("/api/lobby/" + id).then(convertLobby);
	}
}
