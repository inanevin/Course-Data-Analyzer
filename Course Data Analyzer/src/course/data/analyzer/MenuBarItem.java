/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author InanEvin
 */
public class MenuBarItem {
    
    private JPanel m_SelectablePanel;
    private JPanel m_MainPanel;
    private UIManager uiManager;
    
    
    public JPanel GetMainPanel() { return m_MainPanel;}
    public JPanel GetSelectablePanel() { return m_SelectablePanel;}
    
    public MenuBarItem(JPanel sp, JPanel mp) {  m_SelectablePanel = sp; m_MainPanel = mp; }

    
}
