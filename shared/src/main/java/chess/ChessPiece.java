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
     * @return a bool of if the piece at the square is on the same team as this piece
     */
    private boolean getSameTeam(ChessBoard board, ChessPosition movePosition){
        ChessPiece otherPiece = board.getPiece(new ChessPosition(movePosition.getRow(), movePosition.getColumn()));
        if (otherPiece.pieceColor == pieceColor){
            return true;
        }
        return false;
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
        if (getPieceType() == PieceType.BISHOP) return new BishopMovesCalculator().pieceMoves(board, myPosition);
        return null;
    }

    public interface PieceMovesCalculator{
        Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
    }

    public class PawnMovesCalculator implements PieceMovesCalculator{

        @Override
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
            Collection<ChessMove> pawnMoves = new ArrayList<ChessMove>();
            return pawnMoves;
        }
    }

    public class BishopMovesCalculator implements PieceMovesCalculator{

        @Override
        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
            Collection<ChessMove> bishopMoves = new ArrayList<ChessMove>();
            if (pieceColor == ChessGame.TeamColor.BLACK){
                // Right Down
                for (int i = 1; i <= 8 - myPosition.getColumn(); i++) {
                    if (((myPosition.getRow() - i) >= 1) && ((myPosition.getColumn() + i) <= 8)) {
                        ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() +i));
                        if ((piece != null) && piece.pieceColor == ChessGame.TeamColor.BLACK){
                            i = 8;
                        }
                        else if (((piece != null) && piece.pieceColor == ChessGame.TeamColor.WHITE)){
                            bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i), null));
                            i = 8;
                        }
                        else{
                            bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i), null));
                        }
                    }
                }
                // Right Up
                for (int i = 1; i <= 8 - myPosition.getColumn(); i++) {
                    if (myPosition.getRow()+i <= 8 && myPosition.getColumn()+i <= 8){
                        ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() +i));
                        if ((piece != null) && piece.pieceColor == ChessGame.TeamColor.BLACK){
                            i = 8;
                        }
                        else if (((piece != null) && piece.pieceColor == ChessGame.TeamColor.WHITE)){
                            bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i), null));
                            i = 8;
                        }
                        else {
                            bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i), null));
                        }
                    }
                }
                // Left Down
                for (int i = 1; i <= myPosition.getColumn(); i++) {
                    if (myPosition.getRow()-i >= 1 && myPosition.getColumn()-i >= 1){
                        ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i));
                        if ((piece != null) && piece.pieceColor == ChessGame.TeamColor.BLACK){
                            i = myPosition.getColumn();
                        }
                        else if (((piece != null) && piece.pieceColor == ChessGame.TeamColor.WHITE)){
                            bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i), null));
                            i = myPosition.getColumn();
                        }
                        else {
                            bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i), null));
                        }
                    }
                }
                // Left Up
                for (int i = 1; i <= myPosition.getColumn(); i++) {
                    if (myPosition.getRow()+i <= 8 && myPosition.getColumn()-i >= 1){
                        ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i));
                        if ((piece != null) && piece.pieceColor == ChessGame.TeamColor.BLACK){
                            i = myPosition.getColumn();
                        }
                        else if (((piece != null) && piece.pieceColor == ChessGame.TeamColor.WHITE)){
                            bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i), null));
                            i = myPosition.getColumn();
                        }
                        else{
                            bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                    new ChessPosition(myPosition.getRow() + i, myPosition.getColumn()-i), null));
                        }
                    }
                }
            } else {
                    // Right Down
                    for (int i = 1; i <= 8 - myPosition.getColumn(); i++) {
                        if (((myPosition.getRow() - i) >= 1) && ((myPosition.getColumn() + i) <= 8)) {
                            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() +i));
                            if ((piece != null) && piece.pieceColor == ChessGame.TeamColor.WHITE){
                                i = 8;
                            }
                            else if (((piece != null) && piece.pieceColor == ChessGame.TeamColor.BLACK)){
                                bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i), null));
                                i = 8;
                            }
                            else{
                                bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i), null));
                            }
                        }
                    }
                    // Right Up
                    for (int i = 1; i <= 8 - myPosition.getColumn(); i++) {
                        if (myPosition.getRow()+i <= 8 && myPosition.getColumn()+i <= 8){
                            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() +i));
                            if ((piece != null) && piece.pieceColor == ChessGame.TeamColor.WHITE){
                                i = 8;
                            }
                            else if (((piece != null) && piece.pieceColor == ChessGame.TeamColor.BLACK)){
                                bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i), null));
                                i = 8;
                            }
                            else {
                                bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i), null));
                            }
                        }
                    }
                    // Left Down
                    for (int i = 1; i <= myPosition.getColumn(); i++) {
                        if (myPosition.getRow()-i >= 1 && myPosition.getColumn()-i >= 1){
                            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i));
                            if ((piece != null) && piece.pieceColor == ChessGame.TeamColor.WHITE){
                                i = myPosition.getColumn();
                            }
                            else if (((piece != null) && piece.pieceColor == ChessGame.TeamColor.BLACK)){
                                bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i), null));
                                i = myPosition.getColumn();
                            }
                            else {
                                bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i), null));
                            }
                        }
                    }
                    // Left Up
                    for (int i = 1; i <= myPosition.getColumn(); i++) {
                        if (myPosition.getRow() + i <= 8 && myPosition.getColumn() - i >= 1) {
                            ChessPiece piece = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i));
                            if ((piece != null) && piece.pieceColor == ChessGame.TeamColor.WHITE) {
                                i = myPosition.getColumn();
                            } else if (((piece != null) && piece.pieceColor == ChessGame.TeamColor.BLACK)) {
                                bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i), null));
                                i = myPosition.getColumn();
                            } else {
                                bishopMoves.add(new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()),
                                        new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i), null));
                            }
                        }
                    }

            }
            return bishopMoves;
        }
    }
}

