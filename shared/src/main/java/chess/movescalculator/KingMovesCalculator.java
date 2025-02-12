package chess.movescalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements ChessPiece.PieceMovesCalculator {
    private final ChessPiece mypiece;

    public KingMovesCalculator(ChessPiece mypiece) {
        this.mypiece = mypiece;
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> kingMoves = new ArrayList<>();
        // Up
        if (myPosition.getRow() + 1 <= 8) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()));
            if (piece == null || piece.getTeamColor() != mypiece.getTeamColor()) {
                kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()), null));
            }
        }
        // Up Left
        if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn()-1 >= 1) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1));
            if (piece == null || piece.getTeamColor() != mypiece.getTeamColor()) {
                kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1), null));
            }
        }
        // Up Right
        if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn()+1 <= 8) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1));
            if (piece == null || piece.getTeamColor() != mypiece.getTeamColor()) {
                kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1), null));
            }
        }
        // Down Left
        if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn()-1 >= 1) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1));
            if (piece == null || piece.getTeamColor() != mypiece.getTeamColor()) {
                kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1), null));
            }
        }
        // Down Right
        if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn()+1 <= 8) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1));
            if (piece == null || piece.getTeamColor() != mypiece.getTeamColor()) {
                kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1), null));
            }
        }
        // Down
        if (myPosition.getRow() - 1 >= 1) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()));
            if (piece == null || piece.getTeamColor() != mypiece.getTeamColor()) {
                kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()), null));
            }
        }
        // Left
        if (myPosition.getColumn()-1 >= 1) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1));
            if (piece == null || piece.getTeamColor() != mypiece.getTeamColor()) {
                kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1), null));
            }
        }
        // Right
        if (myPosition.getColumn()+1 <= 8) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1));
            if (piece == null || piece.getTeamColor() != mypiece.getTeamColor()) {
                kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1), null));
            }
        }
        return kingMoves;
    }
}