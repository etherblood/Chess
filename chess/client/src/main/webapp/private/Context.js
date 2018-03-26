"use strict";

var context = {};

context.matches = new TrackableList();
context.requests = new TrackableMap();
context.selectedMatch = new TrackableItem();
context.lobbies = new TrackableList();
context.selectedLobby = new TrackableItem();
context.selectedPlayer = new TrackableItem();
context.ownAccount = new TrackableItem();

context.httpService = new HttpService(Cookies.get("XSRF-TOKEN"));
context.eventService = new EventService();
context.pollService = new PollService(context.httpService, context.eventService);

context.playerProvider = new PlayerProvider(context.httpService);
context.playerCache = new ObjectCache(context.playerProvider.get);
context.playerService = new PlayerService(context.httpService, context.eventService, context.ownAccount);

context.lobbyProvider = new LobbyProvider(context.httpService, context.playerCache);
context.lobbyCache = new ObjectCache(context.lobbyProvider.get);
context.lobbyService = new LobbyService(context.httpService, context.eventService, context.lobbies, context.lobbyCache, context.playerCache, context.ownAccount, context.lobbyProvider);

context.matchProvider = new MatchProvider(context.httpService, context.playerCache);
context.matchCache = new ObjectCache(context.matchProvider.getMatch);
context.matchService = new MatchService(context.httpService, context.eventService, context.matches, context.matchCache, context.requests, context.playerCache);

context.playerService.init();
context.lobbyService.init();
context.matchService.init();
context.pollService.init();

context.matchController = new MatchController($("#matchContainer"), context.selectedMatch, context.matchService, context.ownAccount);
context.matchesController = new MatchesController($("#matchList"), context.matches, context.selectedMatch);
context.requestsController = new MatchRequestsController($("#requestList"), context.requests, context.matchService, context.ownAccount);
context.lobbyController = new LobbyController($("#lobbyContainer"), context.selectedLobby, context.selectedPlayer, context.lobbyService);
context.selectedPlayerController = new PlayerController($("#playerDetails"), context.selectedPlayer, context.matchService, context.ownAccount);
context.lobbiesController = new LobbiesController($("#lobbyList"), context.lobbies, context.selectedLobby);

