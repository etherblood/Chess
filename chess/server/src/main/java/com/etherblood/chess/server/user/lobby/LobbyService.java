package com.etherblood.chess.server.user.lobby;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etherblood.chess.server.user.account.model.Account;
import com.etherblood.chess.server.user.authentication.UserContextService;
import com.etherblood.chess.server.user.lobby.model.Lobby;
import com.etherblood.chess.server.user.lobby.model.LobbyMembership;

@Service
public class LobbyService {

	private final static Logger LOG = LoggerFactory.getLogger(LobbyService.class);
	private final LobbyRepository lobbyRepo;
	private final UserContextService userContextService;

	@Autowired
	public LobbyService(LobbyRepository lobbyRepo, UserContextService userContextService) {
		this.lobbyRepo = lobbyRepo;
		this.userContextService = userContextService;
	}

	public List<Lobby> visibleLobbies() {
		UUID currentUserId = userContextService.currentUserId();
		return lobbyRepo.findVisibleLobbies(currentUserId);
	}

	public List<UUID> lobbyAccountIds(UUID lobbyId) {
		return lobbyRepo.lobbyAccountIds(lobbyId);
	}

	public List<Account> lobbyAccounts(UUID lobbyId) {
		return lobbyRepo.lobbyAccounts(lobbyId);
	}

	public boolean isAccountMemberOfLobby(UUID accountId, UUID lobbyId) {
		return lobbyRepo.findMembership(lobbyId, accountId) != null;
	}

	@Transactional
	public Lobby createLobby(String name) {
		Lobby lobby = new Lobby();
		lobby.setId(UUID.randomUUID());
		lobby.setName(name);
		lobby.setPublic(true);
		UUID currentUserId = userContextService.currentUserId();
		Account currentAccount = lobbyRepo.proxyById(Account.class, currentUserId);
		lobby.setOwner(currentAccount);
		lobbyRepo.persist(lobby);
		LOG.info("created lobby {}", lobby);
		createLobbyMembership(currentAccount, lobby);
		return lobby;
	}

	@Transactional
	public void joinLobby(UUID lobbyId) {
		UUID currentUserId = userContextService.currentUserId();
		Account currentAccount = lobbyRepo.proxyById(Account.class, currentUserId);
		Lobby lobby = lobbyRepo.proxyById(Lobby.class, lobbyId);
		createLobbyMembership(currentAccount, lobby);
	}

	private void createLobbyMembership(Account currentAccount, Lobby lobby) {
		LobbyMembership membership = new LobbyMembership();
		membership.setId(UUID.randomUUID());
		membership.setLobby(lobby);
		membership.setAccount(currentAccount);
		lobbyRepo.persist(membership);
		LOG.info("created membership {}", membership);
	}

	@Transactional
	public void leaveLobby(UUID lobbyId) {
		UUID currentUserId = userContextService.currentUserId();
		LobbyMembership membership = lobbyRepo.findMembership(lobbyId, currentUserId);
		lobbyRepo.remove(membership);
	}
}
