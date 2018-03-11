"use strict";
function LobbyView(jDiv, lobby) {

	let divMap = {};

	jDiv.load("lobby/lobby.html",
			function() {

				let nameDiv = jDiv.find(".lobbyNameContainer");
				nameDiv.html($.escapeHtml(lobby.name));
				// let joinDiv = jDiv.find(".lobbyJoinContainer");

				let membersDiv = jDiv.find(".lobbyMembersContainer");
				let memberAddListener = function(member) {
					let memberDiv = $("<div>" + $.escapeHtml(member.name) + "</div>");
					divMap[member.id] = memberDiv;
					membersDiv.append(memberDiv);
				};
				lobby.members.subscribeAddListener(memberAddListener, true);
				let memberRemoveListener = function(member) {
					membersDiv.removeChild(divMap[member.id]);
					delete divMap[member.id];
				};
				lobby.members.subscribeRemoveListener(memberRemoveListener);

				let messagesDiv = jDiv.find(".lobbyMessagesContainer");
				let messageAddListener = function(message) {
					let messageDiv = $("<div>" + $.escapeHtml(message.text) + "</div>");
					divMap[message.id] = messageDiv;
					messagesDiv.append(messageDiv);
				};
				lobby.messages.subscribeAddListener(messageAddListener, true);
			});

	// let changeListener = function(newValue, oldValue) {
	// if (oldValue) {
	// oldValue.members.unsubscribeAddListener(memberAddListener);
	// oldValue.members.unsubscribeRemoveListener(memberRemoveListener, true);
	// }
	//
	// if (newValue) {
	// jDiv.css("display", "visible");
	// // membersDiv.empty();
	// // playersDiv.empty();
	// joinDiv.css("display", newValue.members.size() ? "none" : "visible");
	// newValue.members.subscribeAddListener(memberAddListener, true);
	// newValue.members.subscribeRemoveListener(memberRemoveListener);
	// } else {
	// jDiv.css("display", "none");
	// }
	// };
	//
	// lobbies.subscribeChangeListener(changeListener, true);

}