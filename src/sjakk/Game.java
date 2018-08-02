package sjakk;

import Chessboard.Chessboard;
import Chessboard.ChessboardBuilder;
import ai.BruteChessAI;
import enums.GameMode;
import enums.Player;

/**
 *
 * @author Joakim
 */
public class Game {
    private Chessboard chessboard;
    private Controller controller;
    
    public Game(GameMode gm){
        initialize(gm);
    }
    
    public void initialize(GameMode gm){
        switch(gm){
            case STANDARD:
                chessboard = ChessboardBuilder.build();
                break;
            case CHESS960:
                chessboard = ChessboardBuilder.build960Mode();
                break;
            default:
                chessboard = ChessboardBuilder.build();
        }
        if(controller != null){
            controller.disposeFrame(); // dispose the old window
        }
        else {
            controller = new Controller();
        }
    }
    
    public void startGame(){
        controller.prepareUI(this);
        controller.showStartupBoard();      
        controller.startGame();
    }
    
    public Chessboard getChessboard(){
        return chessboard;
    }
    
    public Move getAIMove(Player player){
        Move move;
      
        if (player == Player.WHITE) {
            BruteChessAI aiWhite = new BruteChessAI(Player.WHITE);
            move = aiWhite.getChessMove(chessboard);
        } else {
            BruteChessAI aiBlack = new BruteChessAI(Player.BLACK);
            move = aiBlack.getChessMove(chessboard);
        }

        chessboard.updateMove(move);
        return move;
    }
    
    public void gameLoop(){

    }
    
    private Move parseUserMove(String userInput){
        userInput = userInput.toUpperCase();
        String tokens[] = userInput.split("");
        
        //Start position
        char c = tokens[1].charAt(0); 
        int startPos[] = new int[2];
        startPos[1] = c - 65;
        startPos[0] = Integer.parseInt(tokens[2]) - 1;
        
        //End position
        c = tokens[4].charAt(0); 
        int endPos[] = new int[2];
        endPos[1] = c - 65;
        endPos[0] = Integer.parseInt(tokens[5]) - 1;
        //Move move = new Move(startPos, endPos);
        return null;
    }
}
