"use strict";
function MatchController(matchService, boardService, eventService) {

    eventService.subscribe("matchMove", function (event) {
        let matchId = event.matchId;
        let move = event.object;
        boardService.movePiece(matchId, move.from, move.to);
        if (move.promotion) {
            boardService.promote(matchId, move.to, move.promotion);
        }
        boardService.setAvailableMoves(matchId, []);
        matchService.moves(matchId).then(function (moves) {
            boardService.setAvailableMoves(matchId, moves);
        });
    });

    eventService.subscribe("matchMoveRequest", function (event) {
        matchService.move(event.matchId, event.move.from, event.move.to, event.move.promotion);
    });
}
