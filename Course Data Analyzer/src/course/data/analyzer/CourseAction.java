/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

/**
 *
 * @author InanEvin
 *
 * This class is for recording the adding & removing courses through the GUI.
 * Instances of these actions are used for undo operations.
 *
 */
public class CourseAction
{

    private Course actionSubject;
    private int actionIndex;
    private int listIndex;

    public CourseAction(Course s, int i, int li) throws ActionIndexException
    {

        if (i != 1 && i != 2)
            throw new ActionIndexException("Action Index must be 1 or 2!");

        actionSubject = s;
        actionIndex = i;
        listIndex = li;
    }

    public int getIndex()
    {
        return actionIndex;
    }

    public Course getSubject()
    {
        return actionSubject;
    }

    public int getListIndex()
    {
        return listIndex;
    }
}
