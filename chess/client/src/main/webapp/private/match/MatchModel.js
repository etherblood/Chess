"use strict";
function MatchModel(id, white, black, startFen, started) {
	let moves = new TrackableList();

	Object.defineProperties(this, {
		id : {
			get : function() {
				return id;
			}
		},
		moves : {
			get : function() {
				return moves;
			}
		},
		white : {
			get : function() {
				return white;
			}
		},
		black : {
			get : function() {
				return black;
			}
		},
		startFen : {
			get : function() {
				return startFen;
			}
		},
		started : {
			get : function() {
				return started;
			}
		}
	});
}
