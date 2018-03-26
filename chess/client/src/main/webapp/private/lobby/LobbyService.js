"use strict";
function LobbyService(httpService, eventService, lobbies, lobbyCache, playerCache, ownAccount, lobbyProvider) {

	eventService.subscribe("newLobbyChatMessageEvent", function(event) {
		$.when(lobbyCache.fetchObject(event.lobbyId), playerCache.fetchObject(event.message.senderId)).then(function(lobby, sender) {
			if(lobby.details.get()) {
				lobby.details.get().messages.add(new MessageModel(event.message.id, sender, event.message.text, event.message.created));
			}
		});
	});
	eventService.subscribe("newLobbyEvent", function(event) {
		lobbyCache.fetchObject(event.id).then(function(lobby) {
			lobbies.add(lobby);
		});
	});
	eventService.subscribe("newLobbyMemberEvent", function(event) {
		$.when(lobbyCache.fetchObject(event.lobbyId), playerCache.fetchObject(event.memberId)).then(function(lobby, member) {
			if(lobby.details.get()) {
				playerCache.fetchObject(event.memberId).then(lobby.details.get().members.add);
			} else if(ownAccount.get().id === member.id) {
				lobbyProvider.get(lobby.id).then(function(lobbyDetailed) {
					lobby.details.set(lobbyDetailed.details.get());
				});
			}
		});
	});

	this.init = function() {
		httpService.get("/api/lobby/list").then(function(result) {
			for (var i = 0; i < result.length; i++) {
				lobbyCache.fetchObject(result[i].id).then(lobbies.add);
			}
		});
	}

	this.createLobby = function(name) {
		httpService.post("/api/lobby/create", {name:name});
	}

	this.sendMessage = function(lobbyId, text) {
		httpService.post("/api/lobby/" + lobbyId+ "/send", {text:text});
	}

	this.join = function(lobbyId) {
		httpService.post("/api/lobby/" + lobbyId+ "/join");
	}
}
