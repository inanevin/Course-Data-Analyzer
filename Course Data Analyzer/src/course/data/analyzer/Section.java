/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.io.Serializable;

/**
 *
 * @author InanEvin
 */
public class Section implements Serializable {
    
    private String m_Name;

    public String GetName() {
        return m_Name;
    }
    
    public void SetName(String n) { m_Name = n;}
    public Section(String n) { m_Name = n;}
    
    
}
