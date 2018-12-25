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

    public Syllabus GetSyllabus()
    {
        return m_Syllabus;
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

    private void writeObject(ObjectOutputStream oos) throws IOException
    {

        // default serialization 
        oos.defaultWriteObject();

        try
        {
            String fileName = (new StringBuilder().append(m_ID).append("-").append("sections.dat")).toString();
            File f = new File(System.getProperty("user.dir"), fileName);
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream mOOS = new ObjectOutputStream(fos);
            mOOS.writeObject(_Sections);
            mOOS.close();
            fos.close();
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException
    {

        ois.defaultReadObject();

        ArrayList<Section> list = new ArrayList<Section>();

        try
        {
            String fileName = (new StringBuilder().append(m_ID).append("-").append("sections.dat")).toString();
            File f = new File(System.getProperty("user.dir"), fileName);
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream mOis = new ObjectInputStream(fis);
            
            _Sections = (ArrayList) mOis.readObject();
            
            
            
            mOis.close();
            fis.close();
        } catch (IOException ioe)
        {
            //ioe.printStackTrace();
            _Sections = new ArrayList<Section>();
            _Sections.add(new Section("Section 1"));
            i_SelectedSection = 0;
        } catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
            //c.printStackTrace();
            _Sections = new ArrayList<Section>();
            _Sections.add(new Section("Section 1"));
            i_SelectedSection = 0;
        }

    }

    public void DeleteSectionData()
    {
        try
        {

            String fileName = (new StringBuilder().append(m_ID).append("-").append("sections.dat")).toString();
            File f = new File(System.getProperty("user.dir"), fileName);
            FileOutputStream fos = new FileOutputStream(f);
            fos.close();
            f.delete();

        } catch (IOException ioe)
        {
            //ioe.printStackTrace();
        }

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
        
        m_Syllabus = new Syllabus();
    }

    public ArrayList<Section> GetSections()
    {
        return _Sections;
    }

    public void Edit(String id, String name, String Desc)
    {
        m_ID = id;
        m_Name = name;
        m_Description = Desc;
    }
}
