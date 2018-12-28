/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author InanEvin
 * 
 * An item existing in the left panel of the GUI. Used for activation and deactivation of the panels for these categories.
 * 
 */
public class MenuBarItem
{

    private final JPanel selectablePanel;
    private final JPanel mainPanel;
    private final JPanel noCoursePanel;
    private UIManager uiManager;
    private final JScrollPane innerPanel;

    public MenuBarItem(JPanel sp, JPanel mp, JScrollPane innp, JPanel ncp)
    {
        selectablePanel = sp;
        mainPanel = mp;
        innerPanel = innp;
        noCoursePanel = ncp;
    }

    public JPanel getMainPanel()
    {
        return mainPanel;
    }

    public JPanel getSelectablePanel()
    {
        return selectablePanel;
    }

    public JPanel getNoCoursePanel()
    {
        return noCoursePanel;
    }

    public JScrollPane getInnerPanel()
    {
        return innerPanel;
    }

}
