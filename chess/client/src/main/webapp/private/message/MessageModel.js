"use strict";
function MessageModel(sender, text, created) {
	this.getSender = function() {
		return sender;
	}
	this.getText = function() {
		return text;
	}
	this.getCreated = function() {
		return created;
	}
}
