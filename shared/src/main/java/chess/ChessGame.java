package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn = TeamColor.WHITE;
    private ChessBoard board = new ChessBoard();

    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> allMoves;
        Collection<ChessMove> retMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null){
            return null;
        }
        allMoves = piece.pieceMoves(board, startPosition);
        for (ChessMove move: allMoves) {
            ChessBoard testBoard = new ChessBoard(board);
            testBoard.movePiece(move);
            ChessGame testGame = new ChessGame();
            testGame.setBoard(testBoard);
            testGame.setTeamTurn(getTeamTurn());
            if (!testGame.isInCheck(board.getPiece(move.getStartPosition()).getTeamColor())){
                retMoves.add(move);
            }
        }
        return retMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (validMoves == null || piece.getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException();
        }
        boolean canMakeMove = false;
        for (ChessMove individualMove: validMoves) {
            if (move.equals(individualMove)) {
                canMakeMove = true;
            }
        }
        if (!canMakeMove){
            throw new InvalidMoveException();
        }
        else{
            ChessBoard testBoard = new ChessBoard(board);
            testBoard.movePiece(move);
            if (isInCheck(getTeamTurn())){
                throw new InvalidMoveException();
            }
            board.movePiece(move);
            if (getTeamTurn() == TeamColor.BLACK){
                setTeamTurn(TeamColor.WHITE);
            }else{
                setTeamTurn(TeamColor.BLACK);
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = null;
        Collection<ChessPosition> enemyPieces = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            int row = (i/8)+1;
            int col = (i%8)+1;
            ChessPiece piece = board.getPiece(new ChessPosition(row, col));
            if (piece != null && (piece.getPieceType() == ChessPiece.PieceType.KING &&
                    piece.getTeamColor() == teamColor)){
                kingPosition = new ChessPosition(row, col);
            }
            if (piece != null && piece.getTeamColor() != teamColor){
                enemyPieces.add(new ChessPosition(row, col));
            }
        }
        for (ChessPosition position: enemyPieces) {
            ChessPiece piece = board.getPiece(position);
            Collection<ChessMove> validMoves = piece.pieceMoves(board, position);
            Collection<ChessPosition> validPositions = new ArrayList<>();
            for (ChessMove validMove: validMoves) {
                validPositions.add(validMove.getEndPosition());
            }
            if (validPositions.contains(kingPosition)){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)){
            return false;
        }
        Collection<ChessPosition> allyPieces = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            int row = (i/8)+1;
            int col = (i%8)+1;
            ChessPiece piece = board.getPiece(new ChessPosition(row, col));
            if (piece != null && piece.getTeamColor() == teamColor){
                allyPieces.add(new ChessPosition(row, col));
            }
        }
        for (ChessPosition position: allyPieces) {
            ChessPiece piece = board.getPiece(position);
            Collection<ChessMove> validMoves = piece.pieceMoves(board, position);
            for (ChessMove validMove: validMoves){
                ChessBoard testBoard = new ChessBoard(board);
                testBoard.movePiece(validMove);
                ChessGame testGame = new ChessGame();
                testGame.setBoard(testBoard);
                if (!testGame.isInCheck(teamColor)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheckmate(teamColor)){
            return false;
        }
        Collection<ChessPosition> allyPieces = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            int row = (i/8)+1;
            int col = (i%8)+1;
            ChessPiece piece = board.getPiece(new ChessPosition(row, col));
            if (piece != null && piece.getTeamColor() == teamColor){
                allyPieces.add(new ChessPosition(row, col));
            }
        }
        int validMoveNumber = 0;
        for (ChessPosition position: allyPieces) {
            ChessPiece piece = board.getPiece(position);
            Collection<ChessMove> validMoves = piece.pieceMoves(board, position);
            for (ChessMove validMove: validMoves){
                ChessBoard testBoard = new ChessBoard(board);
                testBoard.movePiece(validMove);
                ChessGame testGame = new ChessGame();
                testGame.setBoard(testBoard);
                if (!testGame.isInCheck(teamColor)){
                    validMoveNumber +=1;
                }
            }
        }
        return validMoveNumber == 0;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
