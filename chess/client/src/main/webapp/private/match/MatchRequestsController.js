"use strict";
function MatchRequestsController(jDiv, requests, matchService, ownAccount) {

	let divMap = {};

	let addListener = function(request) {
		let requestDiv = $("<div/>");
		if(request.receiver.id == ownAccount.get().id) {
			requestDiv.text("accept " + request.match.white.name + " vs " + request.match.black.name);
			requestDiv.click(function() {
				matchService.accept(request.match.id);
			});
		} else {
			requestDiv.text("pending " + request.match.white.name + " vs " + request.match.black.name);
		}
		jDiv.append(requestDiv);
		divMap[request.match.id] = requestDiv;
	};

	let removeListener = function(request) {
		if (request.match.id in divMap) {
			divMap[request.match.id].remove();
			delete divMap[request.match.id];
		}
	};

	requests.subscribeAddListener(addListener, true);
	requests.subscribeRemoveListener(removeListener);

}