"use strict";

let context = {};
context.httpService = new HttpService(Cookies.get("XSRF-TOKEN"));
context.eventService = new EventService();
context.lobbyProvider = new LobbyProvider(context.httpService);
context.lobbyCache = new ObjectCache(context.lobbyProvider);
context.lobbies = new TrackableList();
context.selectedLobby = new TrackableProperty();
context.lobbyService = new LobbyService(context.httpService, context.eventService, context.lobbies);
context.pollService = new PollService(context.httpService, context.eventService);

for (var i = 0; i < 3; i++) {//TODO: remove this
	context.httpService.post("/api/lobby/create?name=testLobby" + i);
}

context.lobbyService.run();
context.pollService.run();
