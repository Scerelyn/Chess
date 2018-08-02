
package view;

import enums.GameMode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import sjakk.Chess;

/**
 *
 * @author quist
 */
public class NavigationPanel extends JPanel {
    
    public NavigationPanel(){
        init();
    }
    
    private void init(){
        JButton newGameBtn = new JButton("New Standard Game");
        newGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Chess.newGame(GameMode.STANDARD);
            }
        });
        JButton regretBtn = new JButton("New 960 Game");
        regretBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Chess.newGame(GameMode.CHESS960);
            }
        });
        add(newGameBtn);
        add(regretBtn);
    }
}
