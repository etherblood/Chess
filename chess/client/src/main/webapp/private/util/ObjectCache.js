"use strict";
function ObjectCache(provider) {
	let promises = {};

	this.fetchObject = function(id) {
		let promise = promises[id];
		if(promise) {
			return promise;
		}
		promise = provider(id);
		promises[id] = promise;
		return promise;
	};

}
