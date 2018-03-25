"use strict";
function Listeners() {
	let listeners = [];

	this.subscribe = function(listener) {
		listeners.push(listener);
	}

	this.unsubscribe = function(listener) {
		let index = listeners.indexOf(listener);
		if (~index) {
			listeners.splice(index, 1);
		} else {
			throw "invalid index " + index;
		}
	}

	this.handle = function() {
		for (let i = 0; i < listeners.length; i++) {
			listeners[i].apply(null, arguments);
		}
	}
}
