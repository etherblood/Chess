"use strict";
function LobbyModel(id, name) {
	let members = new TrackableList();
	let messages = new TrackableList();

	Object.defineProperties(this, {
		id : {
			get : function() {
				return id;
			}
		},
		name : {
			get : function() {
				return name;
			}
		},
		members : {
			get : function() {
				return members;
			}
		},
		messages : {
			get : function() {
				return messages;
			}
		}
	});
}
