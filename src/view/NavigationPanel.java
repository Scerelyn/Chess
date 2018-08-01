
package view;

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
        JButton newGameBtn = new JButton("New game");
        newGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Chess.newGame();
            }
        });
        JButton regretBtn = new JButton("Regret move");
        regretBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // something
            }
        });
        add(newGameBtn);
        add(regretBtn);
    }
}
