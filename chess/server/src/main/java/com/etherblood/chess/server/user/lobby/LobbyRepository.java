package com.etherblood.chess.server.user.lobby;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.etherblood.chess.server.persistence.AbstractRepository;
import com.etherblood.chess.server.user.account.model.Account;
import com.etherblood.chess.server.user.lobby.model.Lobby;
import com.etherblood.chess.server.user.lobby.model.LobbyMembership;
import com.etherblood.chess.server.user.lobby.model.MembershipType;
import com.etherblood.chess.server.user.lobby.model.QLobby;
import com.etherblood.chess.server.user.lobby.model.QLobbyMembership;
import com.mysema.query.jpa.JPASubQuery;

@Repository
public class LobbyRepository extends AbstractRepository {

	private static final QLobby qLobby = QLobby.lobby;
	private static final QLobbyMembership qMembership = QLobbyMembership.lobbyMembership;

	public List<Lobby> findVisibleLobbies(UUID accountId) {
		JPASubQuery membership = new JPASubQuery().from(qMembership)
				.where(qMembership.lobby.id.eq(qLobby.id))
				.where(qMembership.account.id.eq(accountId))
				.where(qMembership.membershipType.ne(MembershipType.BANNED));
		return from(qLobby)
				.where(qLobby.isPublic.isTrue().or(membership.exists()))
				.orderBy(qLobby.created.asc())
				.list(qLobby);
	}

	public Lobby findVisibleLobbyById(UUID lobbyId, UUID accountId) {
		JPASubQuery membership = new JPASubQuery().from(qMembership)
				.where(qMembership.lobby.id.eq(lobbyId))
				.where(qMembership.account.id.eq(accountId))
				.where(qMembership.membershipType.ne(MembershipType.BANNED));
		return from(qLobby)
				.where(qLobby.id.eq(lobbyId))
				.where(qLobby.isPublic.isTrue().or(membership.exists()))
				.uniqueResult(qLobby);
	}

	public Lobby findById(UUID lobbyId) {
		return from(qLobby)
				.where(qLobby.id.eq(lobbyId))
				.uniqueResult(qLobby);
	}

	public Lobby findByName(String lobbyName) {
		return from(qLobby)
				.where(qLobby.name.eq(lobbyName))
				.uniqueResult(qLobby);
	}

	public List<Account> lobbyAccounts(UUID lobbyId) {
		return from(qMembership)
				.where(qMembership.lobby.id.eq(lobbyId))
				.where(qMembership.membershipType.eq(MembershipType.MEMBER))
				.list(qMembership.account);
	}

	public List<UUID> lobbyAccountIds(UUID lobbyId) {
		return from(qMembership)
				.where(qMembership.lobby.id.eq(lobbyId))
				.where(qMembership.membershipType.eq(MembershipType.MEMBER))
				.list(qMembership.account.id);
	}
	
	public LobbyMembership findMembership(UUID lobbyId, UUID accountId) {
		return from(qMembership)
				.where(qMembership.lobby.id.eq(lobbyId))
				.where(qMembership.account.id.eq(accountId))
				.uniqueResult(qMembership);
	}
}
