"use strict";
function PlayerProvider(httpService) {
	
	this.get = function(id) {
		return httpService.get("/api/account/" + id).then(function(player) {
			return new PlayerModel(player.id, player.name);
		});
	}
}
