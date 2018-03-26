"use strict";
function MatchService(httpService, eventService, matches, matchCache, requests, playerCache) {

	let convertRequest = function(jsonRequest) {
		return $.when(matchCache.fetchObject(jsonRequest.matchId), playerCache.fetchObject(jsonRequest.requesterId), playerCache.fetchObject(jsonRequest.receiverId)).then(function(match, requester, receiver) {
			return new MatchRequestModel(match, requester, receiver);
		});
	};
	
	eventService.subscribe("matchStartedEvent", function(matchId) {
		matchCache.fetchObject(matchId).then(matches.add);
		requests.remove(matchId);
	});
	eventService.subscribe("matchMoveEvent", function(moves) {
		for (let matchId in moves) {
			matchCache.fetchObject(matchId).then(function(match) {
				match.moves.add(moves[matchId]);
			});
		}
	});
	eventService.subscribe("matchRequestedEvent", function(request) {
		convertRequest(request).then(function(request) {
			requests.add(request.match.id, request);
		});
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
				convertRequest(result[i]).then(function(request) {
					requests.add(request.match.id, request);
				});
			}
		});
	}
}

