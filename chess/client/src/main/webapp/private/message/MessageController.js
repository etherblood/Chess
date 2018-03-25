"use strict";
function MessageController(jDiv, message) {

	jDiv.prop('title', message.created);

	let nameDiv = $("<span/>");
	nameDiv.text(message.sender.name);
	jDiv.append(nameDiv);
	jDiv.append($("<span/>", {
		text : ": "
	}));
	let textDiv = $("<span/>");
	textDiv.text(message.text);
	jDiv.append(textDiv);

	this.remove = function() {
		jDiv.empty();
	}
}
