"use strict";
function TrackableProperty() {
	let value;
	let listeners = new Listeners();

	this.subscribeChangeListener = function(listener, trigger) {
		listeners.subscribe(listener);
		if (trigger) {
			listener(value, null);
		}
	}

	this.unsubscribeChangeListener = function(listener, trigger) {
		if (trigger) {
			listener(null, value);
		}
		listeners.unsubscribe(listener);
	}

	this.set = function(newValue) {
		let oldValue = value;
		if (oldValue === newValue) {
			return;
		}
		value = newValue;
		listeners.handle(newValue, oldValue);
	}

	this.get = function() {
		return value;
	}
}
