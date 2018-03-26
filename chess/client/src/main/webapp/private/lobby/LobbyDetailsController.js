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
	
	let lobbyDetailsChangeListener = function(lobbyDetails, oldDetails) {
		if(oldDetails) {
			oldDetails.members.unsubscribeAddListener(memberAddListener);
			oldDetails.members.unsubscribeRemoveListener(memberRemoveListener, true);
			oldDetails.messages.unsubscribeAddListener(messageAddListener);
			messagesDiv.empty();
		}
		
		if(lobbyDetails) {
			detailsDiv.show();
			joinDiv.hide();

			lobbyDetails.members.subscribeAddListener(memberAddListener, true);
			lobbyDetails.members.subscribeRemoveListener(memberRemoveListener);

			lobbyDetails.messages.subscribeAddListener(messageAddListener, true);
		} else {
			detailsDiv.hide();
			joinDiv.show();
		}
	};
	
	let lobbyChangeListener = function(lobby, oldLobby) {
		if(oldLobby) {
			oldLobby.details.unsubscribeChangeListener(lobbyDetailsChangeListener, true);
		}
		
		if(lobby) {
			lobby.details.subscribeChangeListener(lobbyDetailsChangeListener, true);
		}
	};
	
	selectedLobby.subscribeChangeListener(lobbyChangeListener, true);

}