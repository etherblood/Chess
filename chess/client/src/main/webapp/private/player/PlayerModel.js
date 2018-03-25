"use strict";
function PlayerModel(id, name) {

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
