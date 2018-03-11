"use strict";
function TrackableMap() {
	let items = {};
	let addListeners = new Listeners();
	let removeListeners = new Listeners();
	
	this.subscribeAddListener = function(listener, trigger) {
		addListeners.subscribe(listener);
		if(trigger) {
			for (let key in items) {
				listener(items[key]);
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
		if(trigger) {
			for (let key in items) {
				listener(items[key]);
			}
		}
		addListeners.unsubscribe(listener);
	}
	
	this.add = function(key, item) {
		items.push(item);
		addListeners.handle(item);
	}
	
	this.remove = function(key) {
		if(!items.hasOwnProperty(key)) {
			throw "key " + key + " not contained in map";
		}
		let item = items[key];
		delete items[key];
		removeListeners.handle(item);
	}
	
	this.get = function(key) {
		return items[key];
	}
}
