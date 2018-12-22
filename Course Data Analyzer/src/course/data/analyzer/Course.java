/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

/**
 *
 * @author InanEvin
 */
public class Course {
    
    private String m_ID;
    private String m_Name;
    private String m_Description;
    public int i_DuplicationCount;
    
    public String GetID() {
        return m_ID;
    }

    public String GetName() {
        return m_Name;
    }

    public String GetDescription() {
        return m_Description;
    }
    
    
    
    public Course(String i, String n, String d) { m_ID = i; m_Name = n; m_Description = d;}
    public Course(Course c, int duplicationCount) {
        m_ID = (new StringBuilder().
                append(c.GetID()).
                append(duplicationCount)).
                toString();
        
        m_Name = c.GetName(); 
        m_Description = c.GetDescription();
    }
}
