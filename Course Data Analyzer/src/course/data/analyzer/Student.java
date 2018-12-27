/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 *
 * @author InanEvin
 */
public class Student implements Serializable
{
    private int m_ID;
    private String m_FullName;
    private int m_Year;
    private String id;

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Student other = (Student) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }
    
    
       
    public String getStringID()
    {
        return id;
    }

    public Student(String id)
    {
        this.id = id;
    }

    public Student(int id, String name, int year) 
    {
        m_ID = id;
        m_FullName = name;
        m_Year = year;
    }
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
