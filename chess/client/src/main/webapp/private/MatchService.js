"use strict";
function MatchService(httpService) {

    this.startMatch = function (whitePlayer, blackPlayer) {
        return httpService.post("/api/match/new", {whitePlayer: whitePlayer, blackPlayer: blackPlayer});
    };

    this.getMatch = function (matchId) {
        return httpService.get("/api/match/" + matchId + "/status");
    };

    this.getPieces = function (matchId) {
        return httpService.get("/api/match/" + matchId + "/pieces");
    };

    this.move = function (matchId, from, to, promotion) {
        return httpService.post("/api/match/" + matchId + "/move", {from: from, to: to, promotion: promotion});
    };

    this.moves = function (matchId) {
        return httpService.get("/api/match/" + matchId + "/moves");
    };
}
