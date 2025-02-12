package chess.movescalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements ChessPiece.PieceMovesCalculator {
    private final ChessPiece mypiece;

    public RookMovesCalculator(ChessPiece mypiece) {
        this.mypiece = mypiece;
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> rookMoves = new ArrayList<>();
        // Up
        for (int i = 1; i <= 8 - myPosition.getRow(); i++) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn()));
            if (piece != null && piece.getTeamColor() == mypiece.getTeamColor()){
                i = 10;
            }
            else if (piece != null) {
                rookMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow() + i, myPosition.getColumn()), null));
                i = 10;
            }
            else{
                rookMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow() + i, myPosition.getColumn()), null));
            }
        }
        // Down
        for (int i = 1; i < myPosition.getRow(); i++) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn()));
            if (piece != null && piece.getTeamColor() == mypiece.getTeamColor()){
                i = 10;
            }
            else if (piece != null) {
                rookMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow() - i, myPosition.getColumn()), null));
                i = 10;
            }
            else {
                rookMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow() - i, myPosition.getColumn()), null));
            }
        }
        // Right
        for (int i = 1; i <= 8 - myPosition.getColumn(); i++) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()+i));
            if (piece != null && piece.getTeamColor() == mypiece.getTeamColor()){
                i = 10;
            }
            else if (piece != null) {
                rookMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow(), myPosition.getColumn()+i), null));
                i = 10;
            }
            else {
                rookMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i), null));
            }
        }
        // Left
        for (int i = 1; i < myPosition.getColumn(); i++) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()-i));
            if (piece != null && piece.getTeamColor() == mypiece.getTeamColor()){
                i = 10;
            }
            else if (piece != null) {
                rookMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow(), myPosition.getColumn()-i), null));
                i = 10;
            }
            else {
                rookMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i), null));
            }
        }
        return rookMoves;
    }
}