"use strict";
function PlayerProvider(httpService) {
	
	this.get = function(ids) {
		promise = httpService.get("/api/account/players", ids).then(function(players) {
			let result = [];
			for (let i = 0; i < players.length; i++) {
				let player = players[i];
				result.push(new PlayerModel(player.id, player.name));
			}
			return result;
		});
	}
	
	this.id = function(player) {
		return player.id;
	}
}
