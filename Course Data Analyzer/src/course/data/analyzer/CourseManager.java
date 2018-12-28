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

    private int lastActionIndex;
    private ArrayList<Course> allCourses;
    private ResourceManager resourceManager;
    private UIManager uiManager;
    private Course lastActionSubject;

    public CourseManager(UIManager um, ResourceManager rm)
    {
        uiManager = um;
        resourceManager = rm;
    }

    public ArrayList<Course> GetCourseList()
    {
        return allCourses;
    }

    public Course GetCourse(int index)
    {
        if (index < -1 || allCourses.size() <= index)
            throw new IndexOutOfBoundsException();

        return allCourses.get(index);
    }

    public void AddSectionToCourse(int course, String sc)
    {
        allCourses.get(course).addSection(sc);
        uiManager.SelectSection(allCourses.get(course).getSections().size() - 1);
    }

    public void RemoveSectionFromCourse(int course)
    {
        if (allCourses.get(course).getSections().size() < 1)
            return;

        allCourses.get(course).removeSelectedSection();
        uiManager.SelectSection(allCourses.get(course).getSelectedSectionIndex());
    }

    public void PopulateCourses()
    {
        allCourses = resourceManager.LoadCourseList();

        // Choose the initially selected course, -1 if list is empty.
        int selectionIndex = allCourses.size() == 0 ? -1 : 0;
        uiManager.SelectCourse(selectionIndex);
    }

    public void SaveCourses()
    {
        resourceManager.SaveCourseList(allCourses);
    }

    public void AddNewCourse(String id, String name, String desc)
    {
        // Instantiate a course and add it.
        Course c = new Course(id, name, desc);
        allCourses.add(c);

        // Record last action for undo operation.
        uiManager.SetLastActionForCourses(c, 1);
    }

    // Adds new course - UNDO OPERATION
    public void AddNewCourse(Course c, int index)
    {
        allCourses.add(index, c);

        // Update UI list.
        uiManager.SelectCourse(index);
    }

    public void DuplicateCourse(int index)
    {
        Course targetCourse = allCourses.get(index);

        // Increment the duplication count of the selected course.
        targetCourse.setDuplicationCount(targetCourse.getDuplicationCount() + 1);

        // Copy constructor with duplication counter.
        Course c = new Course(targetCourse, targetCourse.getDuplicationCount());

        // Add the new course to list.
        allCourses.add(index + 1, c);

        // Update UI list.
        uiManager.SelectCourse(index + 1);

        // Record last action for undo operation.
        uiManager.SetLastActionForCourses(c, 1);
    }

    // Removes an added course. - UNDO OPERATION
    public void RemoveCourse(Course c)
    {
        // Remove the course.
        allCourses.remove(c);

        // Select another one from the list.
        int toSelect = allCourses.indexOf(c) == 0 ? -1 : allCourses.indexOf(c) - 1;
        uiManager.SelectCourse(toSelect);
    }

    public void RemoveCourse(int index)
    {
        // Record last action for undo operation.
        uiManager.SetLastActionForCourses(allCourses.get(index), 2);

        // Remove the target course and update the UI list.
        allCourses.remove(index);

        // Select another one from the list.
        int toSelect = ((index - 1) == -1 && allCourses.size() > 0) ? 0 : index - 1;
        uiManager.SelectCourse(toSelect);
    }


    public boolean CheckIfExists(String tt)
    {
        // Iterate courses and check if there exists a matching ID.
        for (int i = 0; i < allCourses.size(); i++)
        {
            if (allCourses.get(i).getID().equals(tt))
                return true;
        }
        return false;
    }

}
