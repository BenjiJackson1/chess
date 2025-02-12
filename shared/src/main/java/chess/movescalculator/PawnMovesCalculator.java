package chess.movescalculator;

import chess.ChessPiece;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessGame;
import chess.ChessPosition;
import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements ChessPiece.PieceMovesCalculator {
    private final ChessPiece mypiece;

    public PawnMovesCalculator(ChessPiece mypiece) {
        this.mypiece = mypiece;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> pawnMoves;
        if (mypiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            pawnMoves = pieceMovesBlack(board, myPosition);
        }
        else {
            pawnMoves = pieceMovesWhite(board, myPosition);
        }
        return pawnMoves;
        }

    private void addPawnMove(Collection<ChessMove> pawnMoves, ChessPosition myPosition, int row, int col) {
        pawnMoves.add(new ChessMove(
                new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                new ChessPosition(myPosition.getRow() + row, myPosition.getColumn() + col),
                null
        ));
    }

    private Collection<ChessMove> pieceMovesWhite(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> pawnMoves = new ArrayList<>();
        if (myPosition.getRow() == 2) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn())) == null) {
                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null));
                if (board.getPiece(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn())) == null) {
                    addPawnMove(pawnMoves, myPosition, 2, 0);
                }
            }
            // capture diagonal left
            if (myPosition.getColumn() - 1 > 0) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1));
                if (piece != null && piece.getTeamColor() != mypiece.getTeamColor()) {
                    addPawnMove(pawnMoves, myPosition, 1, -1);
                }
            }
            // capture diagonal right
            if (myPosition.getColumn() + 1 <= 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1));
                if (piece != null && piece.getTeamColor() != mypiece.getTeamColor()) {
                    addPawnMove(pawnMoves, myPosition, 1, 1);
                }
            }
        } else {
            if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn())) == null) {
                if (myPosition.getRow() + 1 == 8) {
                    pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.BISHOP));
                    pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.QUEEN));
                    pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.ROOK));
                    pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.KNIGHT));
                } else {
                    addPawnMove(pawnMoves, myPosition, 1, 0);
                }

            }
            // capture diagonal left
            if (myPosition.getColumn() - 1 > 0) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1));
                if (piece != null && piece.getTeamColor() != mypiece.getTeamColor()) {
                    if (myPosition.getRow() + 1 == 8) {
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.BISHOP));
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.QUEEN));
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.ROOK));
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.KNIGHT));
                    } else {
                        addPawnMove(pawnMoves, myPosition, 1, -1);
                    }
                }
            }
            // capture diagonal right
            if (myPosition.getColumn() + 1 < 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1));
                if (piece != null && piece.getTeamColor() != mypiece.getTeamColor()) {
                    if (myPosition.getRow() + 1 == 8) {
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.BISHOP));
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.QUEEN));
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.ROOK));
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.KNIGHT));
                    } else {
                        addPawnMove(pawnMoves, myPosition, 1, 1);
                    }
                }
            }
        }
        return pawnMoves;
    }

    private Collection<ChessMove> pieceMovesBlack(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> pawnMoves = new ArrayList<>();
        if (myPosition.getRow() == 7) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn())) == null) {
                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                        new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));
                if (board.getPiece(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn())) == null) {
                    pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()), null));
                }
            }
            // capture diagonal left
            if (myPosition.getColumn() - 1 > 0) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1));
                if (piece != null && piece.getTeamColor() != mypiece.getTeamColor()) {
                    pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));
                }
            }
            // capture diagonal right
            if (myPosition.getColumn() + 1 <= 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1));
                if (piece != null && piece.getTeamColor() != mypiece.getTeamColor()) {
                    pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), null));
                }
            }
        } else {
            if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn())) == null) {

                if (myPosition.getRow() - 1 == 1) {
                    pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.BISHOP));
                    pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.QUEEN));
                    pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.ROOK));
                    pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.KNIGHT));
                } else {
                    addPawnMove(pawnMoves, myPosition, -1, 0);
                }

            }
            // capture diagonal left
            if (myPosition.getColumn() - 1 > 0) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1));
                if (piece != null && piece.getTeamColor() != mypiece.getTeamColor()) {
                    if (myPosition.getRow() - 1 == 1) {
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.BISHOP));
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.QUEEN));
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.ROOK));
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.KNIGHT));
                    } else {
                        addPawnMove(pawnMoves, myPosition, -1, -1);
                    }
                }
            }
            // capture diagonal right
            if (myPosition.getColumn() + 1 < 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1));
                if (piece != null && piece.getTeamColor() != mypiece.getTeamColor()) {
                    if (myPosition.getRow() - 1 == 1) {
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.BISHOP));
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.QUEEN));
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.ROOK));
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.KNIGHT));
                    } else {
                        addPawnMove(pawnMoves, myPosition, -1, 1);
                    }
                }
            }
        }

        return pawnMoves;
    }
}