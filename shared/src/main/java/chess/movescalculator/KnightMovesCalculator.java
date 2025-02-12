package chess.movescalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements ChessPiece.PieceMovesCalculator {
    private final ChessPiece mypiece;

    public KnightMovesCalculator(ChessPiece mypiece) {
        this.mypiece = mypiece;
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> knightMoves = new ArrayList<>();
        // Up Left
        if (myPosition.getRow() + 2 <= 8 && myPosition.getColumn()-1 >= 1) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()-1));
            if (piece == null || piece.getTeamColor() != mypiece.getTeamColor()) {
                knightMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()-1), null));
            }
        }
        // Up Right
        if (myPosition.getRow() + 2 <= 8 && myPosition.getColumn()+1 <= 8) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()+1));
            if (piece == null || piece.getTeamColor() != mypiece.getTeamColor()) {
                knightMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()+1), null));
            }
        }
        // Down Left
        if (myPosition.getRow() - 2 >= 1 && myPosition.getColumn()-1 >= 1) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()-1));
            if (piece == null || piece.getTeamColor() != mypiece.getTeamColor()) {
                knightMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()-1), null));
            }
        }
        // Down Right
        if (myPosition.getRow() - 2 >= 1 && myPosition.getColumn()+1 <= 8) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()+1));
            if (piece == null || piece.getTeamColor() != mypiece.getTeamColor()) {
                knightMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()+1), null));
            }
        }
        // Left Up
        if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn()-2 >= 1) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-2));
            if (piece == null || piece.getTeamColor() != mypiece.getTeamColor()) {
                knightMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-2), null));
            }
        }
        // Left Down
        if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn()-2 >= 1) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-2));
            if (piece == null || piece.getTeamColor() != mypiece.getTeamColor()) {
                knightMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-2), null));
            }
        }
        // Right Up
        if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn()+2 <= 8) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+2));
            if (piece == null || piece.getTeamColor() != mypiece.getTeamColor()) {
                knightMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+2), null));
            }
        }
        // Right Down
        if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn()+2 <= 8) {
            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+2));
            if (piece == null || piece.getTeamColor() != mypiece.getTeamColor()) {
                knightMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+2), null));
            }
        }
        return knightMoves;
    }

}