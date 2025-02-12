package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (getPieceType() == PieceType.PAWN)return new PawnMovesCalculator().pieceMoves(board, myPosition);
        if (getPieceType() == PieceType.BISHOP) return new chess.BishopMovesCalculator(this).pieceMoves(board, myPosition);
        if (getPieceType() == PieceType.ROOK) return new chess.RookMovesCalculator(this).pieceMoves(board, myPosition);
        if (getPieceType() == PieceType.KNIGHT) return new KnightMovesCalculator().pieceMoves(board, myPosition);
        if (getPieceType() == PieceType.QUEEN) return new chess.QueenMovesCalculator(this).pieceMoves(board, myPosition);
        if (getPieceType() == PieceType.KING) return new KingMovesCalculator().pieceMoves(board, myPosition);
        return null;
    }

    public interface PieceMovesCalculator{
        default Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
            return null;
        }
    }

    public class PawnMovesCalculator implements PieceMovesCalculator{

        @Override
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
            Collection<ChessMove> pawnMoves = new ArrayList<>();
            // Black pawn logic
            if (pieceColor == ChessGame.TeamColor.BLACK){
                // Pawn in the starting position
                if (myPosition.getRow() == 7){
                    if (board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn())) == null){
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()), null));
                        if (board.getPiece(new ChessPosition(myPosition.getRow()-2, myPosition.getColumn())) == null){
                            pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()), null));
                        }
                    }
                    // capture diagonal left
                    if (myPosition.getColumn()-1 > 0){
                        ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1));
                        if (piece != null && piece.pieceColor != pieceColor){
                            pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1), null));
                        }
                    }
                    // capture diagonal right
                    if (myPosition.getColumn()+1 <= 8){
                        ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1));
                        if (piece != null && piece.pieceColor != pieceColor){
                            pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1), null));
                        }
                    }
                } else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn())) == null){

                        if (myPosition.getRow()-1 == 1){
                            pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()), PieceType.BISHOP));
                            pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()), PieceType.QUEEN));
                            pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()), PieceType.ROOK));
                            pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()), PieceType.KNIGHT));
                        }else{
                            pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()), null));
                        }

                    }
                    // capture diagonal left
                    if (myPosition.getColumn()-1 > 0){
                        ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1));
                        if (piece != null && piece.pieceColor != pieceColor){
                            if (myPosition.getRow()-1 == 1){
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1), PieceType.BISHOP));
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1), PieceType.QUEEN));
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1), PieceType.ROOK));
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1), PieceType.KNIGHT));
                            }else{
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1), null));
                            }
                        }
                    }
                    // capture diagonal right
                    if (myPosition.getColumn()+1 < 8){
                        ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1));
                        if (piece != null && piece.pieceColor != pieceColor){
                            if (myPosition.getRow()-1 == 1){
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1), PieceType.BISHOP));
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1), PieceType.QUEEN));
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1), PieceType.ROOK));
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1), PieceType.KNIGHT));
                            }
                            else{
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1), null));
                            }
                        }
                    }
                }

            }
            // White pawn logic
            else {
                if (myPosition.getRow() == 2){
                    if (board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn())) == null){
                        pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()), null));
                        if (board.getPiece(new ChessPosition(myPosition.getRow()+2, myPosition.getColumn())) == null){
                            pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()), null));
                        }
                    }
                    // capture diagonal left
                    if (myPosition.getColumn()-1 > 0){
                        ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1));
                        if (piece != null && piece.pieceColor != pieceColor){
                            pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1), null));
                        }
                    }
                    // capture diagonal right
                    if (myPosition.getColumn()+1 <= 8){
                        ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1));
                        if (piece != null && piece.pieceColor != pieceColor){
                            pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1), null));
                        }
                    }
                } else {
                    if (board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn())) == null){
                        if (myPosition.getRow()+1 == 8){
                            pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()), PieceType.BISHOP));
                            pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()), PieceType.QUEEN));
                            pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()), PieceType.ROOK));
                            pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()), PieceType.KNIGHT));
                        }else{
                            pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()), null));
                        }

                    }
                    // capture diagonal left
                    if (myPosition.getColumn()-1 > 0){
                        ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1));
                        if (piece != null && piece.pieceColor != pieceColor){
                            if (myPosition.getRow()+1 == 8){
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1), PieceType.BISHOP));
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1), PieceType.QUEEN));
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1), PieceType.ROOK));
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1), PieceType.KNIGHT));
                            }else{
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1), null));
                            }
                        }
                    }
                    // capture diagonal right
                    if (myPosition.getColumn()+1 < 8){
                        ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1));
                        if (piece != null && piece.pieceColor != pieceColor){
                            if (myPosition.getRow()+1 == 8){
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1), PieceType.BISHOP));
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1), PieceType.QUEEN));
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1), PieceType.ROOK));
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1), PieceType.KNIGHT));
                            }
                            else{
                                pawnMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1), null));
                            }
                        }
                    }
                }

            }
            return pawnMoves;
        }
    }

    public class KnightMovesCalculator implements PieceMovesCalculator {
        @Override
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
            Collection<ChessMove> knightMoves = new ArrayList<>();
            // Up Left
            if (myPosition.getRow() + 2 <= 8 && myPosition.getColumn()-1 >= 1) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()-1));
                if (piece == null || piece.pieceColor != pieceColor) {
                    knightMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()-1), null));
                }
            }
            // Up Right
            if (myPosition.getRow() + 2 <= 8 && myPosition.getColumn()+1 <= 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()+1));
                if (piece == null || piece.pieceColor != pieceColor) {
                    knightMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()+1), null));
                }
            }
            // Down Left
            if (myPosition.getRow() - 2 >= 1 && myPosition.getColumn()-1 >= 1) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()-1));
                if (piece == null || piece.pieceColor != pieceColor) {
                    knightMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()-1), null));
                }
            }
            // Down Right
            if (myPosition.getRow() - 2 >= 1 && myPosition.getColumn()+1 <= 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()+1));
                if (piece == null || piece.pieceColor != pieceColor) {
                    knightMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()+1), null));
                }
            }
            // Left Up
            if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn()-2 >= 1) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-2));
                if (piece == null || piece.pieceColor != pieceColor) {
                    knightMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-2), null));
                }
            }
            // Left Down
            if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn()-2 >= 1) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-2));
                if (piece == null || piece.pieceColor != pieceColor) {
                    knightMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-2), null));
                }
            }
            // Right Up
            if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn()+2 <= 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+2));
                if (piece == null || piece.pieceColor != pieceColor) {
                    knightMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+2), null));
                }
            }
            // Right Down
            if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn()+2 <= 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+2));
                if (piece == null || piece.pieceColor != pieceColor) {
                    knightMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+2), null));
                }
            }
            return knightMoves;
        }

    }

    public class KingMovesCalculator implements PieceMovesCalculator {
        @Override
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
            Collection<ChessMove> kingMoves = new ArrayList<>();
            // Up
            if (myPosition.getRow() + 1 <= 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()));
                if (piece == null || piece.pieceColor != pieceColor) {
                    kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()), null));
                }
            }
            // Up Left
            if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn()-1 >= 1) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1));
                if (piece == null || piece.pieceColor != pieceColor) {
                    kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1), null));
                }
            }
            // Up Right
            if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn()+1 <= 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1));
                if (piece == null || piece.pieceColor != pieceColor) {
                    kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1), null));
                }
            }
            // Down Left
            if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn()-1 >= 1) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1));
                if (piece == null || piece.pieceColor != pieceColor) {
                    kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1), null));
                }
            }
            // Down Right
            if (myPosition.getRow() - 1 >= 1 && myPosition.getColumn()+1 <= 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1));
                if (piece == null || piece.pieceColor != pieceColor) {
                    kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1), null));
                }
            }
            // Down
            if (myPosition.getRow() - 1 >= 1) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()));
                if (piece == null || piece.pieceColor != pieceColor) {
                    kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()), null));
                }
            }
            // Left
            if (myPosition.getColumn()-1 >= 1) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1));
                if (piece == null || piece.pieceColor != pieceColor) {
                    kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1), null));
                }
            }
            // Right
            if (myPosition.getColumn()+1 <= 8) {
                ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1));
                if (piece == null || piece.pieceColor != pieceColor) {
                    kingMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                            new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1), null));
                }
            }
            return kingMoves;
        }
    }

}