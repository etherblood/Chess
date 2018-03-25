"use strict";
function MatchRequestsController(jDiv, requests, matchService) {

	let divMap = {};

	let addListener = function(request) {
		let requestDiv = $("<div/>");
		requestDiv.text("accept " + request.white.name + " vs " + request.black.name);
		requestDiv.click(function() {
			matchService.accept(request.id);
		});
		jDiv.append(requestDiv);
		divMap[request.id] = requestDiv;
	};

	let removeListener = function(request) {
		if (request.id in divMap) {
			divMap[request.id].remove();
			delete divMap[request.id];
		}
	};

	requests.subscribeAddListener(addListener, true);
	requests.subscribeRemoveListener(removeListener);

}