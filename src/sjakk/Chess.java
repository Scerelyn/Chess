
package sjakk;

import enums.GameMode;
import enums.Player;

/**
 *
 * @author Joakim
 */
public class Chess {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        newGame(GameMode.STANDARD);
    }
    
    private static Game game = null;
    
    public static void newGame(GameMode gm){
        if(game == null){
            game = new Game(gm);
            game.startGame();
        }
        else {
            game.initialize(gm);
            game.startGame();
        }
    }
    
    public static Player switchPlayer(Player player){
        if(player == Player.WHITE){
            return Player.BLACK;
        } else {
            return Player.WHITE;
        }
    }
    
    
}
