"use strict";
function LobbyDetailsModel(id) {
	let members = new TrackableList();
	let messages = new TrackableList();

	Object.defineProperties(this, {
		id : {
			get : function() {
				return id;
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
