//package com.etherblood.chess.server.matches;
//
//import com.etherblood.chess.server.matches.model.MatchConfig;
//import com.etherblood.chess.server.matches.model.MatchData;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// *
// * @author Philipp
// */
//@RestController
//@RequestMapping("/match")
//public class MatchRemoteService {
//
//    private final MatchService matchService;
//
//    @Autowired
//    public MatchRemoteService(MatchService matchService) {
//        this.matchService = matchService;
//    }
//
//    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
//    @RequestMapping("/{matchId}/status")
//    public MatchData status(@PathVariable("matchId") UUID matchId) {
//        return matchService.getMatch(matchId);
//    }
//
////    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
////    @RequestMapping("/{matchId}/pieces")
////    public List<ChessPiece> pieces(@PathVariable("matchId") UUID matchId) {
////        return matchService.matchPieces(matchId);
////    }
////
////    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
////    @RequestMapping(path = "/{matchId}/move", method = RequestMethod.POST)
////    public void move(@PathVariable("matchId") UUID matchId, @RequestBody CandidateMove move) {
////        matchService.move(matchId, move);
////    }
////
////    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
////    @RequestMapping("/{matchId}/moves")
////    public List<CandidateMove> moves(@PathVariable("matchId") UUID matchId) {
////        return matchService.moves(matchId);
////    }
//
//    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
//    @RequestMapping(path = "/new", method = RequestMethod.POST)
//    public UUID createMatch(@RequestBody MatchConfig config) {
//        return matchService.createMatch(config).getId();
//    }
//
//    @PreAuthorize(value = "hasRole('ROLE_PLAYER')")
//    @RequestMapping(path = "/list")
//    public List<UUID> getAllMatches() {
//        return matchService.getAllMatches().stream().map(MatchData::getId).collect(Collectors.toList());
//    }
//}
