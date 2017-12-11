function BoardService(eventService) {
    let matchDivs = {};
    let matchMoves = {};
    let matchHighlights = {};

    let setDivSquareTranslation = function (div, square) {
        let x = square % 8;
        let y = 7 - Math.floor(square / 8);
        div.style.transform = "translate(" + 100 * x + "%, " + 100 * y + "%)";
    };

    let cleanupHighlights = function (matchId) {
        let highlights = matchHighlights[matchId];
        if (highlights) {
            for (let i = 0; i < highlights.length; i++) {
                matchDivs[matchId].boardPanel.removeChild(highlights[i]);
            }
            delete matchHighlights[matchId];
        }
    };

    this.initMatch = function (matchId, div, pieces) {
        let divs = {};
//        div.id = matchId;
        div.className = "chessPanel";
        div.addEventListener("dragend", function (event) {
            cleanupHighlights(matchId);
        });
        div.addEventListener("dragstart", function (event) {
            let moves = matchMoves[matchId];
            if (!moves) {
                return;
            }
//            event.dataTransfer.setData('text/plain', "drag&drop");//firefox
            for (let from in divs) {
                if (divs[from] === event.srcElement) {
//                    event.dataTransfer.setData('text/plain', key);
//                    event.dataTransfer.effectAllowed = 'copy';
                    cleanupHighlights(matchId);
                    let highlights = [];
                    matchHighlights[matchId] = highlights;

                    for (let i = 0; i < moves.length; i++) {
                        let move = moves[i];
                        if (move.from === from) {
                            let highlight = document.createElement("div");
                            highlight.className = "move-target";
                            setDivSquareTranslation(highlight, move.to);
                            highlight.addEventListener("drop", function (event) {
                                eventService.broadcast("matchMoveRequest", {matchId: matchId, move: move});
//                                    console.log(move);
//                                console.log(event.dataTransfer.getData('text/plain'));
                            });
                            highlight.addEventListener("dragover", function (event) {
                                event.preventDefault();
                            });

                            div.appendChild(highlight);
                            highlights.push(highlight);
                        }
                    }

                    break;
                }
            }
        });

        divs.boardPanel = div;
        matchDivs[matchId] = divs;

        for (let i = 0; i < pieces.length; i++) {
            let piece = pieces[i];
            this.addPiece(matchId, piece);
        }
    };

    this.setAvailableMoves = function (matchId, moves) {
        matchMoves[matchId] = moves;
    };

    this.removeMatch = function (matchId) {
        delete matchDivs[matchId];
        delete matchMoves[matchId];
        delete matchHighlights[matchId];
    };

    this.removePiece = function (matchId, square) {
        let divs = matchDivs[matchId];
        divs.boardPanel.removeChild(divs[square]);
        delete divs[square];
    };

    this.addPiece = function (matchId, piece) {
        let divs = matchDivs[matchId];
        if (divs.hasOwnProperty(piece.square)) {
            throw "cannot add piece to occupied square " + piece.square;
        }
        let pieceDiv = document.createElement("div");
        divs[piece.square] = pieceDiv;
        this.setPieceTypeAndColor(matchId, piece);
        pieceDiv.className = "piece " + piece.type + " " + piece.color;
        pieceDiv.draggable = true;
        setDivSquareTranslation(pieceDiv, piece.square);
        divs.boardPanel.appendChild(pieceDiv);
    };

    this.setPieceTypeAndColor = function (matchId, piece) {
        let divs = matchDivs[matchId];
        divs[piece.square].className = "piece " + piece.type + " " + piece.color;
    };

    this.promote = function (matchId, square, promotion) {
        let divs = matchDivs[matchId];
        divs[square].classList.remove("pawn");
        divs[square].classList.add(promotion);
    };

    this.movePiece = function (matchId, from, to) {
        let divs = matchDivs[matchId];
        if (divs.hasOwnProperty(to)) {
            this.removePiece(matchId, to);
//            throw "cannot move to occupied square " + to;
        }

        divs[to] = divs[from];
        delete divs[from];
        setDivSquareTranslation(divs[to], to);
    };
}
