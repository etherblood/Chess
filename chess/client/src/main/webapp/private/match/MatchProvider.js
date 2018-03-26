"use strict";
function MatchProvider(httpService, playerCache) {

	let convertMatch = function(json, white, black) {
		let match = new MatchModel(json.id, white, black, json.startFen, json.started);
		for (let i = 0; i < json.moves.length; i++) {
			let move = json.moves[i];
			match.moves.add(move);
		}
		return match;
	}

	this.getMatch = function(id) {
		let whiteDefer = $.Deferred();
		let blackDefer = $.Deferred();
		let jsonDefer = httpService.get("/api/match/" + id);
		jsonDefer.then(function(json) {
			playerCache.fetchObject(json.whiteId).then(whiteDefer.resolve);
			playerCache.fetchObject(json.blackId).then(blackDefer.resolve);
		})

		return $.when(jsonDefer, whiteDefer, blackDefer).then(convertMatch);
	}
}
