/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.util.*;

/**
 *
 * @author InanEvin
 */
public class CourseManager
{

    private ArrayList<Course> _AllCourses;
    private ResourceManager resourceManager;
    private UIManager uiManager;
    private int lastActionIndex;
    private Course lastActionSubject;

    public CourseManager(UIManager um, ResourceManager rm)
    {

        uiManager = um;
        resourceManager = rm;
    }

    public void AddSectionToCourse(int course, String sc)
    {
        _AllCourses.get(course).addSection(sc);
        uiManager.SelectSection(_AllCourses.get(course).getSections().size()-1);
    }

    public void RemoveSectionFromCourse(int course)
    {
        if (_AllCourses.get(course).getSections().size() < 1)
        {
            return;
        }
        _AllCourses.get(course).removeSelectedSection();
        uiManager.SelectSection(_AllCourses.get(course).getSelectedSectionIndex());
    }

    public void PopulateCourses()
    {
        _AllCourses = resourceManager.LoadCourseList();

        // Choose the initially selected course, -1 if list is empty.
        int selectionIndex = _AllCourses.size() == 0 ? -1 : 0;
        uiManager.SelectCourse(selectionIndex);
    }

    public void SaveCourses()
    {
        resourceManager.SaveCourseList(_AllCourses);
    }

    public ArrayList<Course> GetCourseList()
    {
        return _AllCourses;
    }

    public Course GetCourse(int index)
    {
        if (index < -1 || _AllCourses.size() <= index)
        {
            throw new IndexOutOfBoundsException();
        }
        return _AllCourses.get(index);
    }

    public void AddNewCourse(String id, String name, String desc)
    {

        // Instantiate a course and add it.
        Course c = new Course(id, name, desc);
        _AllCourses.add(c);

        // Record last action for undo operation.
        uiManager.SetLastActionForCourses(c, 1);

    }

    // Adds new course - UNDO OPERATION
    public void AddNewCourse(Course c, int index)
    {
        _AllCourses.add(index, c);

        // Update UI list.
        uiManager.SelectCourse(index);
    }

    public void DuplicateCourse(int index)
    {

        // Increment the duplication count of the selected course.
        _AllCourses.get(index).duplicationCount++;

        // Copy constructor with duplication counter.
        Course c = new Course(_AllCourses.get(index), _AllCourses.get(index).i_duplicationCount

        // Add the new course to list.
        _AllCourses.add(index + 1, c);

        // Update UI list.
        uiManager.SelectCourse(index + 1);

        // Record last action for undo operation.
        uiManager.SetLastActionForCourses(c, 1);
    }

    // Removes an added course. - UNDO OPERATION
    public void RemoveCourse(Course c)
    {
        int toSelect = _AllCourses.indexOf(c) == 0 ? -1 : _AllCourses.indexOf(c) - 1;

        // Remove the course.
        _AllCourses.remove(c);

        uiManager.SelectCourse(toSelect);
    }

    public void RemoveCourse(int index)
    {

        // Record last action for undo operation.
        uiManager.SetLastActionForCourses(_AllCourses.get(index), 2);

        // Delete the section data of the course.
        //_AllCourses.get(index).DeleteSectionData();

        // Remove the target course and update the UI list.
        _AllCourses.remove(index);

        int toSelect = ((index - 1) == -1 && _AllCourses.size() > 0) ? 0 : index - 1;
        uiManager.SelectCourse(toSelect);
    }

    public void RemoveCourse(String ID)
    {

        // Iterate & find the matching ID.
        for (int i = 0; i < _AllCourses.size(); i++)
        {
            if (_AllCourses.get(i).getID() == ID)
            {

                // Record last action for undo operation.
                uiManager.SetLastActionForCourses(_AllCourses.get(i), 2);

                // Remove & break.
                _AllCourses.remove(i);
                break;

            }
        }
    }

    public void UndoCourseAction()
    {
        if (lastActionIndex == 1)
        {

        }
    }

    public boolean CheckIfExists(String tt)
    {

        // Iterate courses and check if there exists a matching ID.
        for (int i = 0; i < _AllCourses.size(); i++)
        {
            if (_AllCourses.get(i).getID().equals(tt))
            {
                return true;
            }
        }

        return false;
    }

}
