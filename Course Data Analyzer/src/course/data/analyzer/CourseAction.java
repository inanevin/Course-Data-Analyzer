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
public class CourseAction {

    private Course m_ActionSubject;
    private int m_ActionIndex;
    private int m_ListIndex;
    
    public int GetIndex() {
        return m_ActionIndex;
    }


    public Course GetSubject() {
        return m_ActionSubject;
    }
    
    public int GetListIndex()
    {
        return m_ListIndex;
    }

    public CourseAction(Course s, int i, int li) throws ActionIndexException {

        if(i != 1 && i != 2)
        {
            throw new ActionIndexException("Action Index must be 1 or 2!");
        }
        
        m_ActionSubject = s;
        m_ActionIndex = i;
        m_ListIndex = li;
    }
}
