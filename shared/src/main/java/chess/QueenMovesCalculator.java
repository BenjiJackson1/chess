package chess;

import java.util.Collection;

public class QueenMovesCalculator implements ChessPiece.PieceMovesCalculator {
    private final ChessPiece mypiece;

    public QueenMovesCalculator(ChessPiece mypiece) {
        this.mypiece = mypiece;
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> queenMoves = new chess.RookMovesCalculator(mypiece).pieceMoves(board, myPosition);
        Collection<ChessMove> bishopMoves = new chess.BishopMovesCalculator(mypiece).pieceMoves(board, myPosition);
        queenMoves.addAll(bishopMoves);
        return queenMoves;
    }
}