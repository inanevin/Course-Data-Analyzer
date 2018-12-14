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
    
    public CourseManager()
    {
        _AllCourses = new ArrayList<Course>();
    }
    
    public void AddNewCourse(Course c)
    {                           
        _AllCourses.add(c);
    }
    
    public void RemoveCourse(String ID)
    {
        
    }
    
        
    public boolean CheckIfExists(String tt)
    {
                
       for(int i = 0; i < _AllCourses.size(); i++)
       {
           if(_AllCourses.get(i).id.equals(tt))
               return true;
       }
       
       return false;
    }

}
