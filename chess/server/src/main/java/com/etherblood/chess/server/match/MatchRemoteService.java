package com.etherblood.chess.server.match;

import com.etherblood.chess.api.match.ChessPiece;
import com.etherblood.chess.api.match.ChessSquare;
import com.etherblood.chess.api.match.moves.ChessMove;
import com.etherblood.chess.server.match.model.MatchConfig;
import com.etherblood.chess.server.match.model.MatchData;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Philipp
 */
@RestController
@RequestMapping("/match")
public class MatchRemoteService {

    private final MatchService matchService;

    @Autowired
    public MatchRemoteService(MatchService matchService) {
        this.matchService = matchService;
    }

    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
    @RequestMapping("/{matchId}/status")
    public MatchData status(@PathVariable("matchId") UUID matchId) {
        return matchService.getMatch(matchId);
    }

    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
    @RequestMapping("/{matchId}/pieces")
    public Map<ChessSquare, ChessPiece> pieces(@PathVariable("matchId") UUID matchId) {
        Map<Integer, Integer> map = matchService.matchPieces(matchId);
        Map<ChessSquare, ChessPiece> result = new EnumMap<>(ChessSquare.class);
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            result.put(ChessSquare.fromInt(entry.getKey()), ChessModelConverter.convertPiece(entry.getValue()));
        }
        return result;
    }

    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
    @RequestMapping(path = "/{matchId}/move", method = RequestMethod.POST)
    public void move(@PathVariable("matchId") UUID matchId, @RequestBody ChessMove move) {
        matchService.move(matchId, ChessModelConverter.convertMove(move));
    }

    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
    @RequestMapping("/{matchId}/moves")
    public List<ChessMove> moves(@PathVariable("matchId") UUID matchId) {
        return matchService.moves(matchId).stream().map(ChessModelConverter::convertMove).collect(Collectors.toList());
    }

    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
    @RequestMapping(path = "/new", method = RequestMethod.POST)
    public UUID createMatch(@RequestBody MatchConfig config) {
        return matchService.createMatch(config).getId();
    }

    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
    @RequestMapping(path = "/list")
    public List<UUID> getAllMatches() {
        return matchService.getAllMatches().stream().map(MatchData::getId).collect(Collectors.toList());
    }
}
