"use strict";
function MessageModel(id, sender, text, created) {

	Object.defineProperties(this, {
		id : {
			get : function() {
				return id;
			}
		},
		sender : {
			get : function() {
				return sender;
			}
		},
		text : {
			get : function() {
				return text;
			}
		},
		created : {
			get : function() {
				return created;
			}
		}
	});
}
