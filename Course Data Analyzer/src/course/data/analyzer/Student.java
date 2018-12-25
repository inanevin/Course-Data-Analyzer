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
public class Student
{
    private int m_ID;
    private String m_FullName;
    private int m_Year;

    public int getID()
    {
        return m_ID;
    }

    public void setID(int m_ID)
    {
        this.m_ID = m_ID;
    }

    public String getFullName()
    {
        return m_FullName;
    }

    public void setFullName(String m_FullName)
    {
        this.m_FullName = m_FullName;
    }

    public int getYear()
    {
        return m_Year;
    }

    public void setYear(int m_Year)
    {
        this.m_Year = m_Year;
    }
    
    
}
