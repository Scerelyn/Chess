package Chessboard;

import enums.Player;
import java.util.ArrayList;
import java.util.Random;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Tower;
import sjakk.Position;

/**
 *
 * @author Joakim
 */
public abstract class ChessboardBuilder {

    public static Chessboard build() {
        Piece pieces[][] = new Piece[8][8];
        Chessboard chessboard = new Chessboard();

        addWhitePieces(pieces, chessboard);
        addBlackPieces(pieces, chessboard);
        chessboard.setPieces(pieces);
        chessboard.setKingPosition(new Position(0, 3), new Position(7, 3));
        return chessboard;
    }

    public static Chessboard copy(Chessboard old) {
        Chessboard newBoard = new Chessboard();
        Piece pieces[][] = new Piece[8][8];
        for (Piece piece : old.getPieceList()) {
            pieces[piece.getPosition().getRow()][piece.getPosition().getColumn()] = clonePiece(newBoard, piece);
        }
        newBoard.setPieces(pieces);
        newBoard.setKingPosition(old.kingPosition(Player.WHITE),
                old.kingPosition(Player.BLACK));
        return newBoard;
    }

    private static Piece clonePiece(Chessboard board, Piece piece) {
        if (piece == null) {
            return null;
        }

        if (piece instanceof Pawn) {
            return new Pawn(board, piece);
        }
        if (piece instanceof Tower) {
            return new Tower(board, piece);
        }
        if (piece instanceof Knight) {
            return new Knight(board, piece);
        }
        if (piece instanceof Queen) {
            return new Queen(board, piece);
        }
        if (piece instanceof King) {
            return new King(board, piece);
        }
        if (piece instanceof Bishop) {
            return new Bishop(board, piece);
        }

        throw new IllegalStateException("Piece instance not found");
    }

    private static void addWhitePieces(Piece pieces[][], Chessboard board) {
        Position pos = new Position(0, 0);
        pieces[0][0] = new Tower(Player.WHITE, pos, board);

        pos = new Position(0, 1);
        pieces[0][1] = new Knight(Player.WHITE, pos, board);

        pos = new Position(0, 2);
        pieces[0][2] = new Bishop(Player.WHITE, pos, board);

        pos = new Position(0, 3);
        pieces[0][3] = new King(Player.WHITE, pos, board);

        pos = new Position(0, 4);
        pieces[0][4] = new Queen(Player.WHITE, pos, board);

        pos = new Position(0, 5);
        pieces[0][5] = new Bishop(Player.WHITE, pos, board);

        pos = new Position(0, 6);
        pieces[0][6] = new Knight(Player.WHITE, pos, board);

        pos = new Position(0, 7);
        pieces[0][7] = new Tower(Player.WHITE, pos, board);

        for (int i = 0; i < 8; i++) {
            pos = new Position(1, i);
            pieces[1][i] = new Pawn(Player.WHITE, pos, board);
        }
    }

    private static void addBlackPieces(Piece pieces[][], Chessboard board) {
        Position pos = new Position(7, 0);
        pieces[7][0] = new Tower(Player.BLACK, pos, board);

        pos = new Position(7, 1);
        pieces[7][1] = new Knight(Player.BLACK, pos, board);

        pos = new Position(7, 2);
        pieces[7][2] = new Bishop(Player.BLACK, pos, board);

        pos = new Position(7, 3);
        pieces[7][3] = new King(Player.BLACK, pos, board);

        pos = new Position(7, 4);
        pieces[7][4] = new Queen(Player.BLACK, pos, board);

        pos = new Position(7, 5);
        pieces[7][5] = new Bishop(Player.BLACK, pos, board);

        pos = new Position(7, 6);
        pieces[7][6] = new Knight(Player.BLACK, pos, board);

        pos = new Position(7, 7);
        pieces[7][7] = new Tower(Player.BLACK, pos, board);

        for (int i = 0; i < 8; i++) {
            pos = new Position(6, i);
            pieces[6][i] = new Pawn(Player.BLACK, pos, board);
        }
    }

    /**
     * Builds the chessboard, following Chess960's setup
     *
     * @return A Chessboard instance whose initial piece setup follows Chess960
     * rules
     */
    public static Chessboard build960Mode() {
        Piece pieces[][] = new Piece[8][8];
        Chessboard chessboard = new Chessboard();

        //int[] wKingPos = addWhitePieces960Mode(pieces, chessboard);
        int[][] kingsPos = addPieces960Mode(pieces, chessboard);
        chessboard.setPieces(pieces);
        chessboard.setKingPosition(new Position(kingsPos[1][0], kingsPos[1][1]), new Position(kingsPos[0][0], kingsPos[0][1]));
        return chessboard;
    }

    /**
     * Sets up all the pieces for the given board following Chess960 rules
     *
     * @param pieces The 2D piece array to modify
     * @param board The Chessboard instance to modify
     * @return A pair of coordinates of integers representing the black king's
     * position at index 0, and the white kings position at index 1; subindex 0
     * is for row, and subindex 1 is for columns
     */
    private static int[][] addPieces960Mode(Piece pieces[][], Chessboard board) {
        /*
            960 chess has, outside of pawns, randomized placements
            There are two rules to placing however
                A king must be between two rooks
                Bishops must be on opposite color tiles
            Randomization is based off of black pieces, and the white pieces simply mirror the black pieces' placements
        
            The randomization algorithm will work by going from left to right along the bottom black row, placing pieces one at a time
            A piece is randomly selected from a list, initially: rook, both knights, queen, bishop
            If the first bishop is choosen, we will track what color tile it is on
            If the second bishop is choosen, the tile color must be opposite, or we just move on and place another piece
                If the second bishop reaches the last or second to last, depending on the first's color, we will imediately place the bishop since no other tiles of opposite can exist
            If the first rook is choosen, we add in the king to the pieces to place
            When the king is placed, we put in the rook for placing
                This gurantees that the placement order is always rook, king, rook with some other pieces mixed in. This will satisfy the king between two rook rule
            After any piece is placed, we place the equivalent white piece to the other side to mirror the black piece placement
         */

        boolean firstBishopPlaced = false; //determines of the first bishop is placed
        boolean firstBishopIsOnWhite = false; //determines if the first bishop places is on a white tile or not
        boolean kingPlaced = false; //determines if the king was places
        int[][] kingPositions = new int[2][2]; //the return array for the kings' positions

        //the list of pieces to choose from and place
        ArrayList<Character> piecesToPlace = new ArrayList<Character>() {
            { //since the pieces require a position for initializing, a char list to represent pieces must be used since there is not position yet
                add('r');
                add('h');
                add('b');
                add('q');
                add('h');
                add('b'); //default pieces to add in first
            }
        }; //key: r = rook, h = knight, b = bishop, k = king, q = queen

        Position pos = new Position(7, 0); // initial postion instance, will be overwritten later
        Random r = new Random(); // for randomly selecting from the pieces to place list
        for (int i = 0; i < 8; i++) { //along the length/columns of the bottom row
            System.out.println("pieces size:" + piecesToPlace.size());
            char toPlace = piecesToPlace.remove(r.nextInt(piecesToPlace.size())); //randomly pick a piece; cool thing is remove returns the removed piece at that index
            System.out.println("placing: " + toPlace);

            //firstly, deal with forced placement case with the bishop: we can hit a case where the bishop runs out of places to be, so on the last possible spot we ignore the random piece and place it
            if (firstBishopPlaced && piecesToPlace.contains('b') && //first bishop places and bishop is left
                (i == 6 && !firstBishopIsOnWhite || //6th index, meaning we are on second to last tile which is the last white tile. we must place the bishop here
                i == 7 && firstBishopIsOnWhite //7 index, last tile, which is the last black. we must place the bishop here
                )
            ){
                piecesToPlace.add(toPlace);
                pos = new Position(7, i);
                pieces[7][i] = new Bishop(Player.BLACK, pos, board); //place it
                pos = new Position(0, i);
                pieces[0][i] = new Bishop(Player.WHITE, pos, board); //mirrow it
                piecesToPlace.remove(new Character('b'));
            } else { //no forced cases? ok move on
                switch (toPlace) { //convert our char code into placing the actual piece
                    case 'r': //rook
                        if (!kingPlaced) { // no king placed yet means we have yet to place the first rook
                            piecesToPlace.add('k'); // so we add the king in
                            kingPlaced = true; // then flip the bool
                        }
                        pos = new Position(7, i);
                        pieces[7][i] = new Tower(Player.BLACK, pos, board); //place it
                        pos = new Position(0, i);
                        pieces[0][i] = new Tower(Player.WHITE, pos, board); //mirrow it
                        break;
                    case 'k': //king
                        pos = new Position(7, i);
                        pieces[7][i] = new King(Player.BLACK, pos, board); //place it
                        kingPositions[0][0] = 7; //set the black king position
                        kingPositions[0][1] = i;

                        pos = new Position(0, i);
                        pieces[0][i] = new King(Player.WHITE, pos, board); //mirrow it
                        kingPositions[1][0] = 0; //set the white king position
                        kingPositions[1][1] = i;

                        piecesToPlace.add('r'); //add in the second rook
                        break;
                    case 'b': //bishop
                        //index 0 is white for the black piece row,
                        //hence, even indexes are white; odd are black
                        if (firstBishopPlaced) { // first bishop being placed applies the no similar color rules
                            if (firstBishopIsOnWhite && i % 2 == 1 || //index is odd(black) and the first bishop is on white
                                !firstBishopIsOnWhite && i % 2 == 0  //index is even(white) and the first biship is on black
                            ){
                                pos = new Position(7, i);
                                pieces[7][i] = new Bishop(Player.BLACK, pos, board); //place it
                                pos = new Position(0, i);
                                pieces[0][i] = new Bishop(Player.WHITE, pos, board); //mirrow it
                            } else { //bishop cannot place placed in other cases, so add it back and try another piece by cycling the loop back once
                                piecesToPlace.add('b');
                                i--;
                            }
                        } else { // no bishop? just place it and set the bools
                            pos = new Position(7, i);
                            pieces[7][i] = new Bishop(Player.BLACK, pos, board); //place it
                            pos = new Position(0, i);
                            pieces[0][i] = new Bishop(Player.WHITE, pos, board); //mirrow it
                            firstBishopPlaced = true;
                            firstBishopIsOnWhite = i % 2 == 0; //why use and if when the expression is fine: even -> true(white), odd -> false(black)
                        }
                        break;
                    case 'h': //knight, no fancy behaviors
                        pos = new Position(7, i);
                        pieces[7][i] = new Knight(Player.BLACK, pos, board); //place it
                        pos = new Position(0, i);
                        pieces[0][i] = new Knight(Player.WHITE, pos, board); //mirrow it
                        break;
                    case 'q': //queen, also nothing fancy
                        pos = new Position(7, i);
                        pieces[7][i] = new Queen(Player.BLACK, pos, board); //place it
                        pos = new Position(0, i);
                        pieces[0][i] = new Queen(Player.WHITE, pos, board); //mirrow it
                        break;
                }
            }

        }

        //pawns' placements are not randomized, so borrow the pawn placement code and use it
        for (int i = 0; i < 8; i++) {
            pos = new Position(6, i);
            pieces[6][i] = new Pawn(Player.BLACK, pos, board);
        }
        for (int i = 0; i < 8; i++) {
            pos = new Position(1, i);
            pieces[1][i] = new Pawn(Player.WHITE, pos, board);
        }
        return kingPositions;
    }

}
