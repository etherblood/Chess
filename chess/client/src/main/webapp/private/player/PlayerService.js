"use strict";
function PlayerService(httpService, eventService, ownAccount) {

	this.init = function() {
		httpService.get("/api/account/self").then(function(player) {
			ownAccount.set(new PlayerModel(player.id, player.name));
		});
	}
}
