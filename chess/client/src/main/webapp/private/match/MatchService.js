"use strict";
function MatchService(httpService, eventService, matches, matchCache, requests) {

	eventService.subscribe("matchStartedEvent", function(matchId) {
		matchCache.fetchObject(matchId).then(matches.add);
		matchCache.fetchObject(matchId).then(requests.remove);
	});
	eventService.subscribe("matchMoveEvent", function(moves) {
		for (let matchId in moves) {
			matchCache.fetchObject(matchId).then(function(match) {
				match.moves.add(moves[matchId]);
			});
		}
	});
	eventService.subscribe("matchRequestedEvent", function(matchId) {
		matchCache.fetchObject(matchId).then(requests.add);
	});

	this.createMatch = function(white, black) {
		httpService.post("/api/match/create", {whiteId:white, blackId:black});
	}

	this.move = function(matchId, move) {
		httpService.post("/api/match/" + matchId+ "/move", move);
	}

	this.accept = function(matchId) {
		httpService.post("/api/match/" + matchId+ "/accept");
	}

	this.init = function() {
		httpService.get("/api/match/list").then(function(result) {
			for (let i = 0; i < result.length; i++) {
				matchCache.fetchObject(result[i]).then(matches.add);
			}
		});

		httpService.get("/api/match/requests").then(function(result) {
			for (let i = 0; i < result.length; i++) {
				matchCache.fetchObject(result[i]).then(requests.add);
			}
		});
	}
}

