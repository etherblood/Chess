"use strict";
function LobbyModel(id, name) {
	this.details = null;

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
		}
	});
}
