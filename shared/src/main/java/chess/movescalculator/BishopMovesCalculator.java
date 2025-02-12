package chess.movescalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements ChessPiece.PieceMovesCalculator {
    private final ChessPiece mypiece;

    public BishopMovesCalculator(ChessPiece mypiece) {
        this.mypiece = mypiece;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> bishopMoves = new ArrayList<>();
        // Left Up
        for (int i = 1; i < myPosition.getColumn(); i++) {
            if (myPosition.getRow()+i <= 8 && myPosition.getColumn()-i > 0){
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i));
                if (piece != null && piece.getTeamColor() != mypiece.getTeamColor()){
                    bishopMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+i,
                            myPosition.getColumn()-i), null));
                    i = 10;
                }
                else if (piece != null && piece.getTeamColor() == mypiece.getTeamColor()){
                    i = 10;
                }
                else {
                    bishopMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+i,
                            myPosition.getColumn()-i), null));
                }
            }
        }
        // Right Up
        for (int i = 1; i <= 8 - myPosition.getColumn(); i++) {
            if (myPosition.getRow()+i <= 8 && myPosition.getColumn()+i <= 8){
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i));
                if (piece != null && piece.getTeamColor() != mypiece.getTeamColor()){
                    bishopMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+i,
                            myPosition.getColumn()+i), null));
                    i = 10;
                }
                else if (piece != null && piece.getTeamColor() == mypiece.getTeamColor()){
                    i = 10;
                }
                else {
                    bishopMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()+i,
                            myPosition.getColumn()+i), null));
                }
            }
        }
        // Right Down
        for (int i = 1; i <= 8 - myPosition.getColumn(); i++) {
            if (myPosition.getRow()-i > 0 && myPosition.getColumn()+i <= 8){
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i));
                if (piece != null && piece.getTeamColor() != mypiece.getTeamColor()){
                    bishopMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-i,
                            myPosition.getColumn()+i), null));
                    i = 10;
                }
                else if (piece != null && piece.getTeamColor() == mypiece.getTeamColor()){
                    i = 10;
                }
                else {
                    bishopMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-i,
                            myPosition.getColumn()+i), null));
                }
            }
        }
        // Left Down
        for (int i = 1; i < myPosition.getColumn(); i++) {
            if (myPosition.getRow()-i > 0 && myPosition.getColumn()-i > 0){
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i));
                if (piece != null && piece.getTeamColor() != mypiece.getTeamColor()){
                    bishopMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-i,
                            myPosition.getColumn()-i), null));
                    i = 10;
                }
                else if (piece != null && piece.getTeamColor() == mypiece.getTeamColor()){
                    i = 10;
                }
                else {
                    bishopMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow()-i,
                            myPosition.getColumn()-i), null));
                }
            }
        }
        return bishopMoves;
    }
}