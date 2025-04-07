package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.List;

import static ui.EscapeSequences.*;

public class ChessBoardPrinter {

    public static void printGame(ChessGame chessGame, String teamColor, List<ChessPosition> highlights){
        if (teamColor == null || teamColor.equalsIgnoreCase("white")){
            System.out.print("   ");
            System.out.print(" a ");
            System.out.print(" b ");
            System.out.print(" c ");
            System.out.print(" d ");
            System.out.print(" e ");
            System.out.print(" f ");
            System.out.print(" g ");
            System.out.print(" h ");
            System.out.print("   \n");
            for (int j = 8; j > 0; j--) {
                System.out.print(SET_BG_COLOR_LIGHT_GREY);
                System.out.print(" "+ j +" ");
                for (int i = 1; i < 9; i++) {
                    ChessPosition pos = new ChessPosition(j, i);
                    if ((i+j) % 2 == 0){
                        if (highlights != null && highlights.stream().anyMatch(p -> p.getRow() == pos.getRow() && p.getColumn() == pos.getColumn())) {
                            System.out.print(SET_BG_COLOR_DARK_GREEN);
                        } else{
                            System.out.print(SET_BG_COLOR_BLUE);
                        }
                    }else{
                        if (highlights != null && highlights.stream().anyMatch(p -> p.getRow() == pos.getRow() && p.getColumn() == pos.getColumn())) {
                            System.out.print(SET_BG_COLOR_GREEN);
                        } else{
                            System.out.print(SET_BG_COLOR_WHITE);
                        }
                    }
                    System.out.print(pieceGetter(chessGame.getBoard().getPiece(new ChessPosition(j,i))));
                }
                System.out.print(SET_BG_COLOR_LIGHT_GREY);
                System.out.print(" "+ j +" \n");
            }
            System.out.print("   ");
            System.out.print(" a ");
            System.out.print(" b ");
            System.out.print(" c ");
            System.out.print(" d ");
            System.out.print(" e ");
            System.out.print(" f ");
            System.out.print(" g ");
            System.out.print(" h ");
            System.out.print("   \n");
        }
        else{
            System.out.print("   ");
            System.out.print(" h ");
            System.out.print(" g ");
            System.out.print(" f ");
            System.out.print(" e ");
            System.out.print(" d ");
            System.out.print(" c ");
            System.out.print(" b ");
            System.out.print(" a ");
            System.out.print("   \n");
            for (int j = 8; j > 0; j--) {
                System.out.print(SET_BG_COLOR_LIGHT_GREY);
                int col = 9 - j;
                System.out.print(" "+ col +" ");
                for (int i = 1; i < 9; i++) {
                    ChessPosition pos = new ChessPosition(9 - j, 9 - i);
                    if ((i+j) % 2 == 0){
                        if (highlights != null && highlights.stream().anyMatch(p -> p.getRow() == pos.getRow() && p.getColumn() == pos.getColumn())) {
                            System.out.print(SET_BG_COLOR_DARK_GREEN);
                        } else{
                            System.out.print(SET_BG_COLOR_BLUE);
                        }
                    }else{
                        if (highlights != null && highlights.stream().anyMatch(p -> p.getRow() == pos.getRow() && p.getColumn() == pos.getColumn())) {
                            System.out.print(SET_BG_COLOR_GREEN);
                        } else{
                            System.out.print(SET_BG_COLOR_WHITE);
                        }
                    }
                    System.out.print(pieceGetter(chessGame.getBoard().getPiece(new ChessPosition(9-j,9-i))));
                }
                System.out.print(SET_BG_COLOR_LIGHT_GREY);
                System.out.print(" "+ col +" \n");
            }
            System.out.print("   ");
            System.out.print(" h ");
            System.out.print(" g ");
            System.out.print(" f ");
            System.out.print(" e ");
            System.out.print(" d ");
            System.out.print(" c ");
            System.out.print(" b ");
            System.out.print(" a ");
            System.out.print("   \n");
        }
        System.out.print(SET_TEXT_COLOR_WHITE);
    }

    private static String pieceGetter(ChessPiece chessPiece){
        if (chessPiece == null){
            return EMPTY;
        }
        if (chessPiece.getPieceType() == ChessPiece.PieceType.ROOK){
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                return WHITE_ROOK;
            } return BLACK_ROOK;
        }
        if (chessPiece.getPieceType() == ChessPiece.PieceType.BISHOP){
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                return WHITE_BISHOP;
            } return BLACK_BISHOP;
        }
        if (chessPiece.getPieceType() == ChessPiece.PieceType.PAWN){
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                return WHITE_PAWN;
            } return BLACK_PAWN;
        }
        if (chessPiece.getPieceType() == ChessPiece.PieceType.KING){
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                return WHITE_KING;
            } return BLACK_KING;
        }
        if (chessPiece.getPieceType() == ChessPiece.PieceType.QUEEN){
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                return WHITE_QUEEN;
            } return BLACK_QUEEN;
        }
        else{
            if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                return WHITE_KNIGHT;
            }
            return BLACK_KNIGHT;
        }
    }
}
