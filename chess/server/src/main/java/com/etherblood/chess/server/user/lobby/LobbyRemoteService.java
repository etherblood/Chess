package com.etherblood.chess.server.user.lobby;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.etherblood.chess.api.chat.ChatMessageTo;
import com.etherblood.chess.api.lobby.LobbyDetailsTo;
import com.etherblood.chess.api.lobby.LobbyTo;
import com.etherblood.chess.api.lobby.events.NewLobbyEvent;
import com.etherblood.chess.api.lobby.events.NewLobbyMemberEvent;
import com.etherblood.chess.server.poll.PollService;
import com.etherblood.chess.server.user.authentication.UserContextService;
import com.etherblood.chess.server.user.chat.ChatService;
import com.etherblood.chess.server.user.chat.model.ChatMessage;
import com.etherblood.chess.server.user.lobby.model.Lobby;
import com.etherblood.chess.server.user.lobby.model.LobbyMembership;
import com.etherblood.chess.server.user.lobby.model.MembershipType;

@RestController
public class LobbyRemoteService {

	private final LobbyService lobbyService;
	private final ChatService chatService;
	private final PollService pollService;
	private final UserContextService userContextService;

	public LobbyRemoteService(LobbyService lobbyService, ChatService chatService, PollService pollService, UserContextService userContextService) {
		this.lobbyService = lobbyService;
		this.chatService = chatService;
		this.pollService = pollService;
		this.userContextService = userContextService;
	}

	@RequestMapping("/lobby/list")
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public List<LobbyTo> visibleLobbies() {
		return lobbyService.visibleLobbies().stream().map(this::makeTo).collect(Collectors.toList());
	}

	@RequestMapping("/lobby/{lobbyId}")
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public LobbyTo lobbyById(@PathVariable UUID lobbyId) {
		return makeTo(lobbyService.visibleLobbyById(lobbyId));
	}

	@RequestMapping(path="/lobby/create", consumes= {MediaType.APPLICATION_JSON_VALUE}, method=RequestMethod.POST)
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public LobbyTo createLobby(@RequestBody LobbyTo to) {
		Lobby lobby = lobbyService.createLobby(to.name);
		LobbyTo result = makeTo(lobby);
		if(lobby.isPublic()) {
			pollService.sendEvent(new NewLobbyEvent(result), x -> true);
		} else {
			pollService.sendEvent(new NewLobbyEvent(result), lobbyService.lobbyAccountIds(lobby.getId()));
		}
		return result;
	}

	@RequestMapping(path="/lobby/{lobbyId}/join", method=RequestMethod.POST)
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public void joinLobby(@PathVariable UUID lobbyId) {
		LobbyMembership membership = lobbyService.joinLobby(lobbyId);
		if(membership.getType() == MembershipType.MEMBER) {
			pollService.sendEvent(new NewLobbyMemberEvent(lobbyId,  membership.getAccount().getId()), lobbyService.lobbyAccountIds(lobbyId));
		}
	}

	@RequestMapping(path="/lobby/{lobbyId}/invite", method=RequestMethod.POST)
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public void inviteToLobby(@PathVariable UUID lobbyId, @RequestBody UUID accountId) {
		lobbyService.inviteToLobby(lobbyId, accountId);
	}

	@RequestMapping(path="/lobby/{lobbyId}/leave", method=RequestMethod.POST)
	@PreAuthorize(value = "hasRole('ROLE_PLAYER')")
	public void leaveLobby(@PathVariable UUID lobbyId) {
		lobbyService.leaveLobby(lobbyId);
	}

	private LobbyTo makeTo(Lobby lobby) {
		LobbyTo to = new LobbyTo();
		to.id = lobby.getId();
		to.name = lobby.getName();
		to.details = makeLobbyDetailsTo(lobby);
		return to;
	}
	
	private LobbyDetailsTo makeLobbyDetailsTo(Lobby lobby) {
		if (!lobbyService.isMemberOfLobby(userContextService.currentUserId(), lobby.getId())) {
			return null;
		}
		LobbyDetailsTo to = new LobbyDetailsTo();
		to.id = lobby.getId();
		to.memberIds = lobbyService.lobbyAccountIds(lobby.getId());
		to.messages = chatService.getLobbyMessages(lobby.getId()).stream().map(this::makeTo).collect(Collectors.toList());
		return to;
	}
	
	private ChatMessageTo makeTo(ChatMessage message) {
		ChatMessageTo to = new ChatMessageTo();
		to.id = message.getId();
		to.created = message.getCreated();
		to.text = message.getText();
		to.senderId = message.getSender().getId();
		return to;
	}
}
