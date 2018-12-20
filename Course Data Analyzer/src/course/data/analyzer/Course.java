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
public class Course {
    
    public String id;
    public String description;
    public Course(){}
    public Course(String id,String describe){
        this.description=describe;
        this.id=id;
}
}
