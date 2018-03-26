"use strict";
function LobbyModel(id, name) {
	let details = new TrackableItem();

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
		details : {
			get : function() {
				return details;
			}
		}
	});
}
