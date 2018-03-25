"use strict";
function MatchController(matchDiv, selectedMatch, matchService, ownAccount) {

	let pieceDivs = {};
	let chess = new Chess();
	let colorCss = {
			w:"white",
			b:"black"
	}
	let typeCss = {
			p:"pawn",
			k:"king",
			n:"knight",
			b:"bishop",
			r:"rook",
			q:"queen"
	}
	let upperPlayerDiv = $("<div/>");
	matchDiv.append(upperPlayerDiv);
	let boardDiv = $("<div/>", {
		"class": "chessPanel"
	});
	matchDiv.append(boardDiv);
	let lowerPlayerDiv = $("<div/>");
	matchDiv.append(lowerPlayerDiv);
	
	let updatePieceDivSquare = function(pieceDiv,square) {
		pieceDiv.attr('class',
		           function(i, c){
		              return c? c.replace(/(^|\s)[a-h][1-8]/g, "") : "";
		           });
		pieceDiv.addClass(square);
	};
	
	let updatePieceDivType = function(pieceDiv,piece) {
		pieceDiv.attr('class',
		           function(i, c){
		              return c? c.replace(/(^|\s)(white)|(black)/g, "") : "";
		           });
		pieceDiv.attr('class',
		           function(i, c){
		              return c? c.replace(/(^|\s)(pawn)|(knight)|(king)|(bishop)|(rook)|(queen)/g, "") : "";
		           });
		pieceDiv.addClass(colorCss[piece.color] + " piece " + typeCss[piece.type]);
	};
	
	let addPiece = function(square, piece) {
		let pieceDiv = $("<div/>");
		pieceDiv.attr("draggable", true);
		pieceDivs[square] = pieceDiv;
		boardDiv.append(pieceDiv);
		updatePieceDivType(pieceDiv, piece);
		updatePieceDivSquare(pieceDiv, square);
	};
	
	let removePiece = function(square) {
		let pieceDiv = pieceDivs[square];
		delete pieceDivs[square];
		pieceDiv.remove();
	};
	
	let movePiece = function(from, to) {
		updatePieceDivSquare(pieceDivs[from], to);
		let pieceDiv = pieceDivs[from];
		delete pieceDivs[from];
		pieceDivs[to] = pieceDiv;
	}
	
	let cleanupHighlights = function() {
		boardDiv.find(".move-target").remove();
	}

	let moveAddListener = function(move) {
		let m = {from:move.from,to:move.to};
		switch(move.type) {
		case "PROMOTION_KNIGHT":
			m.promotion = chess.KNIGHT;
			break;
		case "PROMOTION_BISHOP":
			m.promotion = chess.BISHOP;
			break;
		case "PROMOTION_ROOK":
			m.promotion = chess.ROOK;
			break;
		case "PROMOTION_QUEEN":
			m.promotion = chess.QUEEN;
			break;
		default:
			break;
		}
		let result = chess.move(m);
		if(~result.flags.indexOf(chess.FLAGS.EP_CAPTURE)) {
			let enPassantCaptureSquare = m.to.replace("3", "4").replace("6", "5");
			removePiece(enPassantCaptureSquare);
		}
		if(~result.flags.indexOf(chess.FLAGS.CAPTURE)) {
			removePiece(m.to);
		}
		if(~result.flags.indexOf(chess.FLAGS.KSIDE_CASTLE)) {
			movePiece(m.from.replace("e", "h"), m.from.replace("e", "f"));
		}
		if(~result.flags.indexOf(chess.FLAGS.QSIDE_CASTLE)) {
			movePiece(m.from.replace("e", "a"), m.from.replace("e", "d"));
		}
		movePiece(m.from, m.to);
		if(~result.flags.indexOf(chess.FLAGS.PROMOTION)) {
			updatePieceDivType(pieceDivs[m.to], chess.get(m.to));
		}
	};
	
	selectedMatch.subscribeChangeListener(function(match, oldMatch) {
		chess.clear();
		cleanupHighlights();
		for (var square in pieceDivs) {
			removePiece(square);
		}
		if(oldMatch) {
			oldMatch.moves.unsubscribeAddListener(moveAddListener);
		}
		
		if(match) {
			matchDiv.show();
			let ownId = ownAccount.get().id;
			if(match.white.id === ownId || match.black.id !== ownId) {
				boardDiv.removeClass("blackView");
				boardDiv.addClass("whiteView");
				upperPlayerDiv.text(match.black.name);
				lowerPlayerDiv.text(match.white.name);
			} else {
				boardDiv.removeClass("whiteView");
				boardDiv.addClass("blackView");
				upperPlayerDiv.text(match.white.name);
				lowerPlayerDiv.text(match.black.name);
			}
			chess.load(match.startFen);
			
			for (let i = 0; i < chess.SQUARES.length; i++) {
				let square = chess.SQUARES[i];
				let piece = chess.get(square);
				if(piece) {
					addPiece(square, piece);
				}
			}
			
			match.moves.subscribeAddListener(moveAddListener, true);
		} else {
			matchDiv.hide();
		}
	}, true);
	
	let createHighlight = function(move) {
		let highlight = $("<div/>");
        highlight.addClass("move-target");
        updatePieceDivSquare(highlight,move.to)
        highlight.on("drop", function (event) {
        	let jsonMove = {from:move.from,to:move.to};
        	if(~move.flags.indexOf(chess.FLAGS.EP_CAPTURE)) {
    			jsonMove.type = "EN_PASSANT";
    		} else if(~move.flags.indexOf(chess.FLAGS.KSIDE_CASTLE) || ~move.flags.indexOf(chess.FLAGS.QSIDE_CASTLE)) {
    			jsonMove.type = "CASTLING";
    		} else if(~move.flags.indexOf(chess.FLAGS.BIG_PAWN)) {
    			jsonMove.type = "PAWN_JUMP";
    		} else if(~move.flags.indexOf(chess.FLAGS.PROMOTION)) {
    			let promptResult = {
    					n:"PROMOTION_KNIGHT", b:"PROMOTION_BISHOP", r:"PROMOTION_ROOK", q:"PROMOTION_QUEEN",
    					knight:"PROMOTION_KNIGHT", bishop:"PROMOTION_BISHOP", rook:"PROMOTION_ROOK", queen:"PROMOTION_QUEEN"};
    			let promotion;
    			while(!promotion) {
    				promotion = promptResult[prompt("please enter the letter corresponding to your promotion piece:\nn: knight\nb: bishop\nr: rook\nq: queen")];
    			}
    			jsonMove.type = promotion;
    		} else {
    			jsonMove.type = "SIMPLE";
    		}
        	matchService.move(selectedMatch.get().id, jsonMove);
        });
        highlight.on("dragover", function (event) {
            event.preventDefault();
        });

        boardDiv.append(highlight);
	};
	
	boardDiv.on("dragend", function (event) {
        cleanupHighlights();
    });
	boardDiv.on("dragstart", function (event) {
        for (let from in pieceDivs) {
            if (pieceDivs[from][0] === event.originalEvent.srcElement) {
            	let moves = chess.moves({square: from, verbose: true });

                for (let i = 0; i < moves.length; i++) {
                    let move = moves[i];
                    if (!move.promotion || move.promotion === chess.QUEEN) {
                        createHighlight(move);
                    }
                }

                break;
            }
        }
	});

}