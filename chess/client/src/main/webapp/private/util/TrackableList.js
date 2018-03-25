"use strict";
function TrackableList() {
	let items = [];
	let addListeners = new Listeners();
	let removeListeners = new Listeners();

	this.subscribeAddListener = function(listener, trigger) {
		addListeners.subscribe(listener);
		if (trigger) {
			for (let i = 0; i < items.length; i++) {
				listener(items[i]);
			}
		}
	}

	this.unsubscribeAddListener = function(listener) {
		addListeners.unsubscribe(listener);
	}

	this.subscribeRemoveListener = function(listener) {
		removeListeners.subscribe(listener);
	}

	this.unsubscribeRemoveListener = function(listener, trigger) {
		if (trigger) {
			for (let i = 0; i < items.length; i++) {
				listener(items[i]);
			}
		}
		addListeners.unsubscribe(listener);
	}

	this.add = function(item) {
		items.push(item);
		addListeners.handle(item);
	}

	this.addAll = function(array) {
		for (let i = 0; i < array.length; i++) {
			this.add(array[i]);
		}
	}

	this.remove = function(item) {
		let index = items.indexOf(item);
		if (~index) {
			items.splice(index, 1);
			removeListeners.handle(item);
			return true;
		}
		return false;
	}

	this.size = function() {
		return items.length;
	}

	this.get = function(index) {
		return items[index];
	}

	this.toArray = function() {
		return items.slice();
	}
}
