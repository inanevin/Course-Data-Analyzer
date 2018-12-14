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
public class Core {
    
    private CourseManager m_CourseManager;
    
    public CourseManager GetCourseManager() {return m_CourseManager;}
    
    
    public Core()
    {
        System.out.println("Core is alive");
        
        m_CourseManager = new CourseManager(); 
    }
    

}
