/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sjakk;

import Chessboard.*;
import enums.Player;
import pieces.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Joakim
 */
public class ChessboardTest {

    private Chessboard chessboard;

    public ChessboardTest() {
    }

    @Before
    public void setUp() {
        chessboard = ChessboardBuilder.build();
    }

    @After
    public void tearDown() {
        chessboard = null;
    }

    @Test
    public void testBoardDimensions() {
        Piece board[][] = chessboard.getPieces();
        assertEquals("Dimension is not 8x8", 8, board.length);
        assertEquals("Dimension is not 8x8", 8, board[0].length);
    }

    @Test
    public void testPrepareBoardPieceColors() {
        Piece board[][] = chessboard.getPieces();
        //Check colors
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 8; y++) {
                assertEquals("Wrong piece color", Player.WHITE,
                        board[x][y].getPieceColor());
            }
        }

        for (int x = 7; x < 5; x--) {
            for (int y = 0; y < 8; y++) {
                assertEquals("Wrong piece color", Player.BLACK,
                        board[x][y].getPieceColor());
            }
        }
    }

    @Test
    public void testPrepareBoardPieceType() {
        Piece board[][] = chessboard.getPieces();
        assertTrue(board[0][0] instanceof Tower);
        assertTrue(board[0][1] instanceof Knight);
        assertTrue(board[0][2] instanceof Bishop);
        assertTrue(board[0][3] instanceof King);
        assertTrue(board[0][4] instanceof Queen);
        assertTrue(board[0][5] instanceof Bishop);
        assertTrue(board[0][6] instanceof Knight);
        assertTrue(board[0][7] instanceof Tower);

        assertTrue(board[7][0] instanceof Tower);
        assertTrue(board[7][1] instanceof Knight);
        assertTrue(board[7][2] instanceof Bishop);
        assertTrue(board[7][3] instanceof King);
        assertTrue(board[7][4] instanceof Queen);
        assertTrue(board[7][5] instanceof Bishop);
        assertTrue(board[7][6] instanceof Knight);
        assertTrue(board[7][7] instanceof Tower);

        //Test pawns
        for (int y = 0; y < 8; y++) {
            assertTrue(board[1][y] instanceof Pawn);
            assertTrue(board[6][y] instanceof Pawn);
        }
    }

    @Test
    public void testPrepareBoardRightNumberOfPieces() {
        Piece[][] board = chessboard.getPieces();
        //Checks middleground for pieces
        for (int x = 2; x < 6; x++) {
            for (int y = 0; y < 8; y++) {
                assertNull(board[x][y]);
            }
        }
    }

    @Test
    public void testUpdateMoveSimple() {
        Piece oldBoard[][] = chessboard.getPieces();
        //Moves pawn at e2 to e 4.
        Position pos = new Position(1, 4);
        Position endPos = new Position(3, 4);
        Move move = new Move(pos, endPos);
        chessboard.updateMove(move);
        Piece newBoard[][] = chessboard.getPieces();

        //Assert board not changes apart from move
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if ((x != 1 && y != 4) || (x != 3 && y != 4)) {
                    assertEquals(oldBoard[x][y], newBoard[x][y]);
                }
            }
        }

        //Check that move has been updated.
        assertNull(newBoard[1][4]);
        assertTrue(newBoard[3][4] instanceof Pawn);
    }

    @Test
    public void testUpdateAttackingMove() {
        Piece oldBoard[][] = chessboard.getPieces();

        //Attacks A7 with pawn at a2.. Illegal moves works here

        Position pos = new Position(1, 0);
        Position endPos = new Position(6, 0);
        Move move = new Move(pos, endPos);
        chessboard.updateMove(move);
        Piece newBoard[][] = chessboard.getPieces();

        //Assert board not changes apart from move
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if ((x != 1 && y != 0) || (x != 6 && y != 0)) {
                    assertEquals(oldBoard[x][y], newBoard[x][y]);
                }
            }
        }

        //Check that move has been updated.
        assertNull(newBoard[1][0]);
        assertTrue(newBoard[6][0] instanceof Pawn);
    }

    @Test
    public void testContainsEnemyOnEnemyPosition() {
        Position white = new Position(0, 0);
        Position black = new Position(6, 0);
        assertTrue(chessboard.containsEnemy(white, black));
    }

    @Test
    public void testContainsEnemyOnFriendlyPosition() {
        Position white = new Position(0, 0);
        Position friendly = new Position(0, 5);
        assertFalse(chessboard.containsEnemy(white, friendly));
    }

    @Test
    public void testContainsEnemyOnEmptyPosition() {
        Position white = new Position(0, 0);
        Position empty = new Position(5, 5);
        assertFalse(chessboard.containsEnemy(white, empty));

    }

    @Test
    public void testContainsEnemyOnOwnPosition() {
        Position white = new Position(0, 0);
        Position self = new Position(0, 0);
        assertFalse(chessboard.containsEnemy(white, self));
    }

    @Test
    public void testIsFreeOnFreeSquare() {
        Position pos = new Position(4,4);
        assertTrue(chessboard.isFree(pos));
    }
    
    @Test
    public void testIsFreeOnOccupiedSquare() {
        Position pos = new Position(0,0);
        assertFalse(chessboard.isFree(pos));    
    }
    
    @Test
    public void testIsOutOfBounds() {
        Position pos = new Position(5,5); //Mid point
        assertFalse(chessboard.isOutOfBounds(pos));
        
        pos = new Position(-1,-1);
        assertTrue(chessboard.isOutOfBounds(pos));
        
        pos = new Position(-1,0);
        assertTrue(chessboard.isOutOfBounds(pos));
        
        pos = new Position(-1,7);
        assertTrue(chessboard.isOutOfBounds(pos));
        
        pos = new Position(-1,8);
        assertTrue(chessboard.isOutOfBounds(pos));
        
        pos = new Position(0,-1);
        assertTrue(chessboard.isOutOfBounds(pos));
        
        pos = new Position(0,0);
        assertFalse(chessboard.isOutOfBounds(pos));
        
        pos = new Position(0,7);
        assertFalse(chessboard.isOutOfBounds(pos));
        
        pos = new Position(0,8);
        assertTrue(chessboard.isOutOfBounds(pos));
        
        pos = new Position(7,-1);
        assertTrue(chessboard.isOutOfBounds(pos));
        
        pos = new Position(7,0);
        assertFalse(chessboard.isOutOfBounds(pos));
        
        pos = new Position(7,7);
        assertFalse(chessboard.isOutOfBounds(pos));
        
        pos = new Position(7,8);
        assertTrue(chessboard.isOutOfBounds(pos));
        
        pos = new Position(8,-1);
        assertTrue(chessboard.isOutOfBounds(pos));
        
        pos = new Position(8,0);
        assertTrue(chessboard.isOutOfBounds(pos));
        
        pos = new Position(8,7);
        assertTrue(chessboard.isOutOfBounds(pos));
        
        pos = new Position(8,8);
        assertTrue(chessboard.isOutOfBounds(pos));
    }
    
    @Test
    public void testChess960Setup(){
        chessboard = ChessboardBuilder.build960Mode();
        Piece board[][] = chessboard.getPieces();
        //check the pawns
        for (int y = 0; y < 8; y++) {
            assertTrue(board[1][y] instanceof Pawn);
            assertTrue(board[6][y] instanceof Pawn);
        }
        //now the black row
        //go along the row and check: 1 queen, 2 knights, 1 rook, then a king, then another rook, a bishop, then another bishop on a different tile
        // count variables
        int rookCount = 0;
        int bishopCount = 0;
        int firstBishopTile = -1;
        int knightCount = 0;
        // find booleans
        boolean kingFound = false;
        boolean queenFound = false;
        //across the board...
        for(int i = 0; i < 8; i++){
            Piece blackPiece = board[7][i];
            Piece whitePiece = board[0][i];
            if(blackPiece instanceof Queen && whitePiece instanceof Queen){ //sure would be nice if there was a switch for types
                if(queenFound){ //queen already found
                    fail("too many queens placed");
                } else {
                    queenFound = true;
                }
            } else if(blackPiece instanceof Knight && whitePiece instanceof Knight){
                if(knightCount >= 2){ // already 2 knights found
                    fail("too many knights placed");
                } else {
                    knightCount++;
                }
            } else if(blackPiece instanceof Tower && whitePiece instanceof Tower){
                if(rookCount >= 2){ //2 rooks found already
                    fail("too many rooks placed");
                } else if(rookCount == 1){ //exactly 1 rook found before -> we found the second one
                    if(!kingFound){ //second rook but no king? violates chess960 rules
                        fail("a king must be inbetween the two rooks");
                    } else {
                        rookCount++;
                    }
                } else { // 0 rooks found
                    rookCount++;
                }
            } else if(blackPiece instanceof King && whitePiece instanceof King){
                if(kingFound){ // king already found
                    fail("too many kings placed");
                } else if(rookCount != 1) { //anything but 1 rook already found
                    fail("king is not between 2 rooks");
                } else {
                    kingFound = true;
                }
            } else if(blackPiece instanceof Bishop && whitePiece instanceof Bishop){
                if(bishopCount >= 2){ //2 bishops already placed
                    fail("too many bishops placed");
                } else if(bishopCount == 1){ // only one bishop placed
                    if(firstBishopTile % 2 == i % 2){ //if they are both even or both odd -> both are black or both are white
                        fail("bishops are not on different tiles");
                    } else {
                        bishopCount++;
                    }
                } else { // no bishops placed
                    bishopCount++;
                }
            } else {
                fail("invalid piece or white and black are not mirrored");
            }
        }
    }
}
