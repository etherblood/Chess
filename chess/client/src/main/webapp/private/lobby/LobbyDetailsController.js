"use strict";
function LobbyDetailsController(jDiv, selectedLobby, selectedPlayer, lobbyService) {

	let divMap = {};

	let detailsDiv = $("<div/>");
	jDiv.append(detailsDiv);
	
	let membersDiv = $("<div/>");
	detailsDiv.append(membersDiv);

	let messagesDiv = $("<div/>");
	detailsDiv.append(messagesDiv);
	
	let sendMessageForm = $('<form/>');
	let messageText = $('<input type="text" placeholder="message text"/>');
	sendMessageForm.append(messageText);
	sendMessageForm.append($('<input type="submit" value="send"/>'));
	sendMessageForm.submit(function(e) {
		e.preventDefault();
		lobbyService.sendMessage(selectedLobby.get().id, messageText[0].value);
		messageText[0].value = "";
	});
	detailsDiv.append(sendMessageForm);
	
	let joinDiv = $("<div/>");
	joinDiv.text("join request");
	joinDiv.click(function() {
		lobbyService.join(selectedLobby.get().id)
	});
	jDiv.append(joinDiv);
	
	let memberAddListener = function(member) {
		let memberDiv = $("<div/>");
		memberDiv.text(member.name);
		memberDiv.click(function() {
			selectedPlayer.set(member);
		});
		divMap[member.id] = memberDiv;
		membersDiv.append(memberDiv);
	};
	let memberRemoveListener = function(member) {
		divMap[member.id].remove();
		delete divMap[member.id];
	};

	let messageAddListener = function(message) {
		let messageDiv = $("<div/>");
		new MessageController(messageDiv, message);
		messagesDiv.append(messageDiv);
	};
	
	let lobbyChangeListener = function(lobby, oldLobby) {
		if(oldLobby && oldLobby.details) {
			oldLobby.details.members.unsubscribeAddListener(memberAddListener);
			oldLobby.details.members.unsubscribeRemoveListener(memberRemoveListener, true);
			oldLobby.details.messages.unsubscribeAddListener(messageAddListener);
			messagesDiv.empty();
		}
		
		if(lobby && lobby.details) {
			detailsDiv.show();
			joinDiv.hide();

			lobby.details.members.subscribeAddListener(memberAddListener, true);
			lobby.details.members.subscribeRemoveListener(memberRemoveListener);

			lobby.details.messages.subscribeAddListener(messageAddListener, true);
		} else {
			detailsDiv.hide();
			joinDiv.show();
		}
	};
	
	selectedLobby.subscribeChangeListener(lobbyChangeListener, true);

}