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
import java.util.Iterator;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author InanEvin
 */
public class ResourceManager
{

    public void SaveCourseList(ArrayList<Course> list)
    {

        try
        {
            File f = new File(System.getProperty("user.dir"), "courseList.dat");
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);
            oos.close();
            fos.close();
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

    }

    public ArrayList<Course> LoadCourseList()
    {
        ArrayList<Course> list = new ArrayList<Course>();

        try
        {
            File f = new File(System.getProperty("user.dir"), "courseList.dat");
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);

            list = (ArrayList) ois.readObject();

            ois.close();
            fis.close();
        } catch (IOException ioe)
        {
            //ioe.printStackTrace();
        } catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
            //c.printStackTrace();
            return new ArrayList<Course>();
        }

        return list;
    }

    public void LoadStudentXLSX() throws IOException
    {
        File myFile = new File(System.getProperty("user.dir"), "studentDataTest.xlsx");
        FileInputStream fis = new FileInputStream(myFile);

        XSSFWorkbook mw = new XSSFWorkbook(fis);

        XSSFSheet mySheet = mw.getSheetAt(0);

        Iterator<Row> rowIterator = mySheet.iterator();

        while (rowIterator.hasNext())
        {

            Row row = rowIterator.next();

            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();
                
                System.out.println(cell.toString() + "\t");
            }

            System.out.println("\n");
        }

        mw.close();
        fis.close();

    }
}
