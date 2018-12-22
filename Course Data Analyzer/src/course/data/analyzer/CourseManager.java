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
public class CourseManager {

    private ArrayList<Course> _AllCourses;
    private ResourceManager resourceManager;
    private UIManager uiManager;
    private int lastActionIndex;
    private Course lastActionSubject;

    public CourseManager(UIManager um, ResourceManager rm) {
        
        uiManager = um;
        resourceManager = rm;
        _AllCourses = resourceManager.GetCourseList();

        _AllCourses.add(new Course("sa", "ss", "df"));
        _AllCourses.add(new Course("s12", "ss", "df"));

        // Choose the initially selected course, -1 if list is empty.
        int selectionIndex = _AllCourses.size() == 0 ? -1 : 0;
        uiManager.UpdateCourseList(_AllCourses, selectionIndex);
    }

    public void AddNewCourse(String id, String name, String desc) {
        
        // Instantiate a course and add it.
        Course c = new Course(id, name, desc);
        _AllCourses.add(c);
        
        // Record last action for undo operation.
        uiManager.SetLastActionForCourses(c, 1);
    }

    public void DuplicateCourse(int index) {

        // Increment the duplication count of the selected course.
        _AllCourses.get(index).i_DuplicationCount++;

        // Copy constructor with duplication counter.
        Course c = new Course(_AllCourses.get(index), _AllCourses.get(index).i_DuplicationCount);

        // Add the new course to list.
        _AllCourses.add(index + 1, c);

        // Update UI list.
        uiManager.UpdateCourseList(_AllCourses, index + 1);

        // Record last action for undo operation.
        uiManager.SetLastActionForCourses(c, 1);
    }

    public void RemoveCourse(int index) {

        // Record last action for undo operation.
        uiManager.SetLastActionForCourses(_AllCourses.get(index), 2);
        
        // Remove the target course and update the UI list.
        _AllCourses.remove(index);

        int toSelect = ((index - 1) == -1 && _AllCourses.size() > 0) ? 0 : index-1;
        uiManager.UpdateCourseList(_AllCourses, toSelect);
    }

    public void RemoveCourse(String ID) {

        // Iterate & find the matching ID.
        for (int i = 0; i < _AllCourses.size(); i++) {
            if (_AllCourses.get(i).GetID() == ID) {

                // Record last action for undo operation.
                uiManager.SetLastActionForCourses(_AllCourses.get(i), 2);

                // Remove & break.
                _AllCourses.remove(i);
                break;

            }
        }
    }

    public void UndoCourseAction() {
        if (lastActionIndex == 1) {

        }
    }

    public boolean CheckIfExists(String tt) {
        
        // Iterate courses and check if there exists a matching ID.
        for (int i = 0; i < _AllCourses.size(); i++) {
            if (_AllCourses.get(i).GetID().equals(tt)) {
                return true;
            }
        }
        
        return false;
    }

}
