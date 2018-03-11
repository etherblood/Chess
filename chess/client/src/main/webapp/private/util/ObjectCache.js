"use strict";
function ObjectCache(provider) {
	let objects = {};

	this.fetchObject = function(id) {
		return this.fetchObjects([id]).then(function(objects) {
			return objects[0];
		});
	}
	
	this.fetchObjects = function(ids) {
		let missingIds = [];
		for (let i = 0; i < ids.length; i++) {
			let id = ids[i];
			if(!objects.hasOwnProperty(id)) {
				missingIds.push(id);
			}
		}
		let promise;
		if(missingIds.length) {
			promise = provider.get(missingIds);
		} else {
			promise = $.Deferred().resolve([]).promise();
		}
		return promise.then(function(missingobjects) {
			for (let i = 0; i < missingobjects.length; i++) {
				let object = missingobjects[i];
				objects[provider.id(object)] = object;
			}
			let result = [];
			for (let i = 0; i < ids.length; i++) {
				let id = ids[i];
				result.push(objects[id]);
			}
			return result;
		});
	}
}
