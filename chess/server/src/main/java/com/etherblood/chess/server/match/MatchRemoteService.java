package com.etherblood.chess.server.match;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.etherblood.chess.api.match.ChessMatchTo;
import com.etherblood.chess.api.match.events.MatchMoveEvent;
import com.etherblood.chess.api.match.events.MatchRequestedEvent;
import com.etherblood.chess.api.match.events.MatchStartedEvent;
import com.etherblood.chess.api.match.moves.ChessMove;
import com.etherblood.chess.server.match.model.ChessMatch;
import com.etherblood.chess.server.match.model.MatchRequest;
import com.etherblood.chess.server.poll.PollService;

/**
 *
 * @author Philipp
 */
@RestController
@RequestMapping("/match")
public class MatchRemoteService {

    private final MatchService matchService;
    private final PollService pollService;

    @Autowired
    public MatchRemoteService(MatchService matchService, PollService pollService) {
        this.matchService = matchService;
        this.pollService = pollService;
    }

//    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
//    @RequestMapping("/{matchId}/status")
//    public MatchData status(@PathVariable("matchId") UUID matchId) {
//        return matchService.getMatch(matchId);
//    }
//
//    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
//    @RequestMapping("/{matchId}/pieces")
//    public Map<ChessSquare, ChessPiece> pieces(@PathVariable("matchId") UUID matchId) {
//        Map<Integer, Integer> map = matchService.matchPieces(matchId);
//        Map<ChessSquare, ChessPiece> result = new EnumMap<>(ChessSquare.class);
//        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
//            result.put(ChessSquare.fromInt(entry.getKey()), ChessModelConverter.convertPiece(entry.getValue()));
//        }
//        return result;
//    }

    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
    @RequestMapping(path = "/{matchId}/move", method = RequestMethod.POST)
    public void move(@PathVariable("matchId") UUID matchId, @RequestBody ChessMove move) {
        matchService.makeMove(matchId, move);
        pollService.sendEvent(new MatchMoveEvent(matchId, move), x -> true);
    }

    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
    @RequestMapping("/{matchId}")
    public ChessMatchTo getMatch(@PathVariable("matchId") UUID matchId) {
    	return makeTo(matchService.getMatch(matchId));
    }

	private ChessMatchTo makeTo(ChessMatch match) {
		ChessMatchTo result = new ChessMatchTo();
    	result.id = match.getId();
    	result.whiteId = match.getWhite().getId();
    	result.blackId = match.getBlack().getId();
    	result.moves = matchService.getMatchMoves(match.getId()).stream().map(ChessModelConverter::convertMove).collect(Collectors.toList());
    	result.startFen = match.getStartFen();
		return result;
	}

    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public void createMatch(@RequestBody ChessMatchTo config) {
        MatchRequest request = matchService.createMatch(config);
        pollService.sendEvent(new MatchRequestedEvent(request.getMatch().getId()), request.getPlayer().getId()::equals);
    }

    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
    @RequestMapping(path = "/list")
    public List<UUID> getActiveMatches() {
        return matchService.activeMatchIds();
    }

    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
    @RequestMapping(path = "/requests")
    public List<UUID> getRequestedMatches() {
        return matchService.requestedMatchIds();
    }

    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
    @RequestMapping(path = "/{matchId}/accept", method = RequestMethod.POST)
    public void acceptMatchRequest(@PathVariable("matchId") UUID matchId) {
        matchService.acceptMatchChallenge(matchId);
        pollService.sendEvent(new MatchStartedEvent(matchId), x -> true);
    }
}
