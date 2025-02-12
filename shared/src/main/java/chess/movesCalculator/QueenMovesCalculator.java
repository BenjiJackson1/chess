package chess.movesCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public class QueenMovesCalculator implements ChessPiece.PieceMovesCalculator {
    private final ChessPiece mypiece;

    public QueenMovesCalculator(ChessPiece mypiece) {
        this.mypiece = mypiece;
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> queenMoves = new RookMovesCalculator(mypiece).pieceMoves(board, myPosition);
        Collection<ChessMove> bishopMoves = new BishopMovesCalculator(mypiece).pieceMoves(board, myPosition);
        queenMoves.addAll(bishopMoves);
        return queenMoves;
    }
}