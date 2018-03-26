"use strict";
function MatchRequestModel(match, requester, receiver) {
	let moves = new TrackableList();

	Object.defineProperties(this, {
		match : {
			get : function() {
				return match;
			}
		},
		requester : {
			get : function() {
				return requester;
			}
		},
		receiver : {
			get : function() {
				return receiver;
			}
		}
	});
}
