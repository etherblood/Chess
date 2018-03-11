package com.etherblood.chess.server.user.lobby;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etherblood.chess.api.lobby.LobbyTo;
import com.etherblood.chess.api.player.PlayerTo;
import com.etherblood.chess.server.user.account.model.Account;
import com.etherblood.chess.server.user.lobby.model.Lobby;

@RestController
public class LobbyRemoteService {

	private final LobbyService lobbyService;

	public LobbyRemoteService(LobbyService lobbyService) {
		this.lobbyService = lobbyService;
	}

	@RequestMapping("/lobby/list")
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public List<LobbyTo> visibleLobbies() {
		return lobbyService.visibleLobbies().stream().map(this::makeReducedTo).collect(Collectors.toList());
	}

	@RequestMapping("/lobby/lobbies")
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public List<LobbyTo> lobbiesById(List<UUID> ids) {
		return lobbyService.visibleLobbies().stream()
				.filter(lobby -> ids.contains(lobby.getId()))
				.map(this::makeReducedTo)
				.collect(Collectors.toList());
	}

	@RequestMapping(path="/lobby/create", method=RequestMethod.POST)
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public LobbyTo createLobby(@RequestParam String name) {
		Lobby lobby = lobbyService.createLobby(name);
		return makeTo(lobby);
	}

	@RequestMapping(path="/lobby/{lobbyId}/join", method=RequestMethod.POST)
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public void joinLobby(@PathVariable UUID lobbyId) {
		lobbyService.joinLobby(lobbyId);
	}

	@RequestMapping(path="/lobby/{lobbyId}/leave", method=RequestMethod.POST)
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public void leaveLobby(@PathVariable UUID lobbyId) {
		lobbyService.leaveLobby(lobbyId);
	}

	private LobbyTo makeReducedTo(Lobby lobby) {
		LobbyTo to = new LobbyTo();
		to.id = lobby.getId();
		to.name = lobby.getName();
		to.members = null;
		to.messages = null;
		return to;
	}

	private LobbyTo makeTo(Lobby lobby) {
		LobbyTo to = new LobbyTo();
		to.id = lobby.getId();
		to.name = lobby.getName();
		to.members = lobbyService.lobbyAccounts(lobby.getId()).stream().map(this::makeReducedTo).collect(Collectors.toList());
		to.messages = null;
		return to;
	}
	
	private PlayerTo makeReducedTo(Account account) {
		PlayerTo to = new PlayerTo();
		to.id = account.getId();
		to.name = account.getLoginHandle();
		return to;
	}
}
