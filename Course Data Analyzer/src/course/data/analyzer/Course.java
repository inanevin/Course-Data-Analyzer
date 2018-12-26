/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


/**
 *
 * @author InanEvin
 */
public class Course implements Serializable
{

    private String m_ID;
    private String m_Name;
    private String m_Description;
    public int i_DuplicationCount;
    private ArrayList<Section> _Sections;
    private int i_SelectedSection = 0;
    private Syllabus m_Syllabus;
    private ArrayList<Exam> _Exams;
    private int i_SelectedExam = -1;

    public Syllabus GetSyllabus()
    {
        return m_Syllabus;
    }
    
    public int GetSelectedExamIndex()
    {
        return i_SelectedExam;
    }
    
    public String GetID()
    {
        return m_ID;
    }

    public String GetName()
    {
        return m_Name;
    }

    public String GetDescription()
    {
        return m_Description;
    }

    public int GetSelectedSectionIndex()
    {
        return i_SelectedSection;
    }

    public Section GetSelectedSection()
    {
        return _Sections.get(i_SelectedSection);
    }
    public void SetSection(int i)
    {
        if (i == -1)
        {
            i = 0;
        }
        i_SelectedSection = i;
    }
    
    public void AddSection(String name)
    {
        _Sections.add(new Section(name));
        SetSection(++i_SelectedSection);
    }

    public void RemoveSelectedSection()
    {
        _Sections.remove(_Sections.get(i_SelectedSection));
        SetSection(--i_SelectedSection);
    }

    public boolean CheckIfSectionExists(String name)
    {
        for (int i = 0; i < _Sections.size(); i++)
        {
            if (_Sections.get(i).GetName().equals(name))
            {
                return true;
            }
        }

        return false;
    }

    public Course(String i, String n, String d)
    {
        m_ID = i;
        m_Name = n;
        m_Description = d;

        if (_Sections == null)
        {
            _Sections = new ArrayList<Section>();
            _Sections.add(new Section("Section 1"));
            i_SelectedSection = 0;
        }
        
        if(_Exams == null)
        {
            _Exams = new ArrayList<Exam>();
        }

        m_Syllabus = new Syllabus();
    }

    public Course(Course c, int duplicationCount)
    {
        m_ID = (new StringBuilder().
                append(c.GetID()).
                append(duplicationCount)).
                toString();

        m_Name = c.GetName();
        m_Description = c.GetDescription();
        _Sections = c.GetSections();

        if (_Sections == null)
        {
            _Sections = new ArrayList<Section>();
            _Sections.add(new Section("Section 1"));
            i_SelectedSection = 0;
        }
        
         if(_Exams == null)
        {
            _Exams = new ArrayList<Exam>();
        }
        
        m_Syllabus = new Syllabus();
    }

    public ArrayList<Section> GetSections()
    {
        return _Sections;
    }

    public ArrayList<Exam> GetExams()
    {
        return _Exams;
    }
    
    public Exam GetSelectedExam()
    {
        return _Exams.get(i_SelectedExam);
    }
    
    public void Edit(String id, String name, String Desc)
    {
        m_ID = id;
        m_Name = name;
        m_Description = Desc;
    }
    
    
    public void AddExam(Exam exam)
    {
        _Exams.add(exam);
        i_SelectedExam++;
    }
    
    public void RemoveExam(int index)
    {
        if(index < 0 || index > _Exams.size()) return;
        _Exams.remove(index);
        i_SelectedExam--;
        
        if(i_SelectedExam < 0 && _Exams.size() > 0)
            i_SelectedExam = 0;
    }
    
    public void SetSelectedExam(int i)
    {
        i_SelectedExam = i;
    }
}
