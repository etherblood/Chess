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
import com.etherblood.chess.server.user.lobby.model.MembershipType;

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

	public Lobby visibleLobbyById(UUID lobbyId) {
		UUID currentUserId = userContextService.currentUserId();
		return lobbyRepo.findVisibleLobbyById(lobbyId, currentUserId);
	}

	public List<UUID> lobbyAccountIds(UUID lobbyId) {
		return lobbyRepo.lobbyAccountIds(lobbyId);
	}

	public List<Account> lobbyAccounts(UUID lobbyId) {
		return lobbyRepo.lobbyAccounts(lobbyId);
	}

	public boolean isMemberOfLobby(UUID accountId, UUID lobbyId) {
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
		createLobbyMembership(currentAccount, lobby, MembershipType.MEMBER);
		return lobby;
	}

	@Transactional
	public LobbyMembership inviteToLobby(UUID lobbyId, UUID accountId) {
		Lobby lobby = lobbyRepo.findById(lobbyId);
		UUID currentUserId = userContextService.currentUserId();
		if(lobby.isPublic() || lobby.getOwner().getId().equals(currentUserId)) {
			LobbyMembership membership = lobbyRepo.findMembership(lobbyId, accountId);
			if(membership == null) {
				Account account = lobbyRepo.proxyById(Account.class, accountId);
				membership = createLobbyMembership(account, lobby, MembershipType.INVITED);
			} else if (membership.getType() == MembershipType.REQUESTED) {
				membership.setType(MembershipType.MEMBER);
				LOG.info("updated {} to member", membership);
			}
			return membership;
		}
		throw new IllegalStateException("failed to invite to " + lobby + " as user " + currentUserId);
	}

	@Transactional
	public LobbyMembership joinLobby(UUID lobbyId) {
		UUID currentUserId = userContextService.currentUserId();
		Lobby lobby = lobbyRepo.findById(lobbyId);
		LobbyMembership membership = lobbyRepo.findMembership(lobbyId, currentUserId);
		if (membership == null) {
			Account currentAccount = lobbyRepo.proxyById(Account.class, currentUserId);
			membership = createLobbyMembership(currentAccount, lobby,
					lobby.isPublic() ? MembershipType.MEMBER : MembershipType.REQUESTED);
		} else if (membership.getType() == MembershipType.INVITED) {
			membership.setType(MembershipType.MEMBER);
			LOG.info("updated {} to member", membership);
		} else {
			throw new IllegalStateException("failed to join lobby with " + membership);
		}
		return membership;
	}

	private LobbyMembership createLobbyMembership(Account currentAccount, Lobby lobby, MembershipType type) {
		LobbyMembership membership = new LobbyMembership();
		membership.setId(UUID.randomUUID());
		membership.setLobby(lobby);
		membership.setAccount(currentAccount);
		membership.setType(type);
		lobbyRepo.persist(membership);
		LOG.info("created {}", membership);
		return membership;
	}

	@Transactional
	public void leaveLobby(UUID lobbyId) {
		UUID currentUserId = userContextService.currentUserId();
		LobbyMembership membership = lobbyRepo.findMembership(lobbyId, currentUserId);
		lobbyRepo.remove(membership);
	}
}
