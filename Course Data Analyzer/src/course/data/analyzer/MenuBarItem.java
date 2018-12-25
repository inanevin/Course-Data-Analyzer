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
public class MenuBarItem
{

    private JPanel m_SelectablePanel;
    private JPanel m_MainPanel;
    private UIManager uiManager;
    private JPanel m_NoCoursePanel;
    private JPanel m_InnerPanel;

    public JPanel GetMainPanel()
    {
        return m_MainPanel;
    }

    public JPanel GetSelectablePanel()
    {
        return m_SelectablePanel;
    }

    public JPanel GetNoCoursePanel()
    {
        return m_NoCoursePanel;
    }

    public JPanel GetInnerPanel()
    {
        return m_InnerPanel;
    }

    public MenuBarItem(JPanel sp, JPanel mp, JPanel innp, JPanel ncp)
    {
        m_SelectablePanel = sp;
        m_MainPanel = mp;
        m_InnerPanel = innp;
        m_NoCoursePanel = ncp;
    }

}
