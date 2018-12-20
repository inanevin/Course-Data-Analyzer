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
    
    public ArrayList<Course> _AllCourses;
    private ArrayList<Course> course_list;
    
     public ArrayList<Course> getCourse_list() {
        return course_list;
    }

    /**
     * Creates new form MainFrame
     */
    public CourseManager()
    {
        course_list = new ArrayList<Course>();
    }
    
    public void AddNewCourse(Course c)
    {                           
        course_list.add(c);
    }
    
    public void RemoveCourse(String ID)
    {
        
    }
    
        
    public boolean CheckIfExists(String tt)
    {
                
       for(int i = 0; i < course_list.size(); i++)
       {
           if(course_list.get(i).id.equals(tt))
               return true;
       }
       
       return false;
    }

}
