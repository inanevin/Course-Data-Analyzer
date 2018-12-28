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
 *
 * The main course class that holds all the necessary information and includes
 * course operations.
 *
 */
public class Course implements Serializable
{

    private int selectedExamIndex = -1;
    private int selectedSectionIndex = 0;
    private int remainingExamPercentage = 100;
    private int duplicationCount;
    private String id;
    private String name;
    private String description;
    private Syllabus syllabus;
    private ArrayList<Exam> examList;
    private ArrayList<Section> sectionList;

    public Course(String i, String n, String d)
    {
        // Init fields.
        id = i;
        name = n;
        description = d;
        sectionList = new ArrayList<Section>();
        sectionList.add(new Section("Section 1"));
        selectedSectionIndex = 0;
        examList = new ArrayList<Exam>();
        syllabus = new Syllabus();
    }

    // Constructor for duplicated course.
    public Course(Course c, int duplicationCount)
    {
        // Init fields.
        id = (new StringBuilder().
                append(c.getID()).
                append(duplicationCount)).
                toString();

        name = c.getName();
        description = c.getDescription();
        sectionList = c.getSections();
        sectionList = new ArrayList<Section>();
        sectionList.add(new Section("Section 1"));
        selectedSectionIndex = 0;
        examList = new ArrayList<Exam>();
        syllabus = new Syllabus();
    }

    public int getSelectedExamIndex()
    {
        return selectedExamIndex;
    }

    public int getSelectedSectionIndex()
    {
        return selectedSectionIndex;
    }

    public int getRemainingExamPercentage()
    {
        return remainingExamPercentage;
    }

    public int getDuplicationCount()
    {
        return duplicationCount;
    }
    
    public void setDuplicationCount(int c)
    {
        duplicationCount = c;
    }

    public String getID()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public Syllabus getSyllabus()
    {
        return syllabus;
    }

    public ArrayList<Exam> getExams()
    {
        return examList;
    }

    public Exam getSelectedExam()
    {
        return examList.get(selectedExamIndex);
    }

    public Section getSelectedSection()
    {
        return sectionList.get(selectedSectionIndex);
    }

    public ArrayList<Section> getSections()
    {
        return sectionList;
    }

    public void setSelectedExam(int i)
    {
        selectedExamIndex = i;
    }

    public void setSection(int i)
    {
        // Always make sure we have one section at least.
        if (i == -1)
            i = 0;
        
        selectedSectionIndex = i;
    }

    public void addSection(String name)
    {
        sectionList.add(new Section(name));
        setSection(++selectedSectionIndex);
    }

    public void removeSelectedSection()
    {
        sectionList.remove(sectionList.get(selectedSectionIndex));
        setSection(--selectedSectionIndex);
    }

    public boolean checkIfSectionExists(String name)
    {
        for (int i = 0; i < sectionList.size(); i++)
        {
            if (sectionList.get(i).GetName().equals(name))
                return true;
        }
        return false;
    }

    public void calculateRemainingExamPercentage()
    {
        remainingExamPercentage = 100;

        for (int i = 0; i < examList.size(); i++)
            remainingExamPercentage -= examList.get(i).getPercentage();
    }

    public void edit(String id, String name, String Desc)
    {
        this.id = id;
        this.name = name;
        description = Desc;
    }

    public void addExam(Exam exam)
    {
        examList.add(exam);
        selectedExamIndex++;
    }

    public void removeExam(int index)
    {
        if (index < 0 || index > examList.size())
            return;

        // Update remaining percentage then remove.
        remainingExamPercentage += examList.get(index).getPercentage();
        examList.remove(index);
        selectedExamIndex--;

        // If we still have a number of exams but exam index is -1 (removed the first one), select the upper one.
        if (selectedExamIndex < 0 && examList.size() > 0)
            selectedExamIndex = 0;
    }

}
