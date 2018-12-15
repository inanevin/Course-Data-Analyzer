/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author InanEvin
 */
public class MenuBarItem {
    
    private JPanel m_SelectionImage;
    private JLabel m_Label;
    private JPanel m_Panel;
    
    public JPanel GetSelectionImage() { return m_SelectionImage; }
    public JLabel GetLabel() { return m_Label;}
    public JPanel GetPanel() { return m_Panel;}
    
    public MenuBarItem(JPanel p, JLabel l) { m_SelectionImage = p; m_Label = l; }
    
}
