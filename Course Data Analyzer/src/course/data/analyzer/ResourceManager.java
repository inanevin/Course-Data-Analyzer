/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author InanEvin
 */
public class ResourceManager {


    public void SaveCourseList(ArrayList<Course> list) {
        
        try {
            FileOutputStream fos = new FileOutputStream("courseList.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
    }
    
    public ArrayList<Course> LoadCourseList()
    {
        ArrayList<Course> list = new ArrayList<Course>();
        
        try
        {
            FileInputStream fis = new FileInputStream("courseList.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
 
            list = (ArrayList) ois.readObject();
 
            ois.close();
            fis.close();
        }
        catch (IOException ioe)
        {
            //ioe.printStackTrace();
        }
        catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
            //c.printStackTrace();
            return new ArrayList<Course>();
        }
        
        return list;
    }
}
