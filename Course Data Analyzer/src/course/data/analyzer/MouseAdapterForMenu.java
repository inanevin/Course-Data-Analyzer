/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

/**
 *
 * @author InanEvin
 */
public class MouseAdapterForMenu extends MouseAdapter {

    private int m_MenuIndex;
    private UIManager uiManager;

    public MouseAdapterForMenu(int menuIndex, UIManager uiMan) {
        m_MenuIndex = menuIndex;
        uiManager = uiMan;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() instanceof JLabel) {
            Color clr = new Color(126, 138, 162);
            JLabel lbl = (JLabel) e.getSource();
            lbl.setForeground(clr);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() instanceof JLabel) {
            Color clr = new Color(227, 227, 227);
            JLabel lbl = (JLabel) e.getSource();
            lbl.setForeground(clr);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        uiManager.SelectMenu(m_MenuIndex);
    }
}
