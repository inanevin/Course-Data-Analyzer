/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author InanEvin
 */
public class Section implements Serializable
{

    private String m_Name;
    private ArrayList<Student> _Students;

    public ArrayList<Student> GetStudents()
    {
        return _Students;
    }

    public String GetName()
    {
        return m_Name;
    }

    public void SetName(String n)
    {
        m_Name = n;
    }
    
    public void AddNewStudent(Student s)
    {
        _Students.add(s);
    }
    
    public void RemoveStudent(int index)
    {
        if(index < 0 || index >= _Students.size()) return;
        
        _Students.remove(index);
    }
    
    public boolean CheckIfStudentExists(int ID)
    {
     
        
        for(int i = 0; i < _Students.size(); i++)
        {
            if(_Students.get(i).getID() == ID)
            {
                return true;
            }
        }
        
        return false;
    }

    public Section(String n)
    {
        m_Name = n;
        _Students = new ArrayList<Student>();
    }

    public Section()
    {
        _Students = new ArrayList<Student>();
    }

}
