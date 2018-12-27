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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author InanEvin
 */
public class ResourceManager
{

    private Section currentCourse;
    private Exam currentExam;
    
    public void SetCurrentExam(Exam e)
    {
        currentExam = e;
    }
    public void SetCurrentSection(Section s)
    {
        currentCourse = s;
    }

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

    public void LoadExamXLSX() throws IOException
    {
        File myFile = new File(System.getProperty("user.dir"), "studentExamData.xlsx");
        FileInputStream fis = new FileInputStream(myFile);

        XSSFWorkbook mw = new XSSFWorkbook(fis);

        XSSFSheet mySheet = mw.getSheetAt(0);

        Iterator<Row> rowIterator = mySheet.iterator();
        DataFormatter objDefaultFormat = new DataFormatter();
        FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) mw);

        while (rowIterator.hasNext())
        {

            Row row = rowIterator.next();
            
            Iterator<Cell> cellIterator = row.cellIterator();
          
            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();
                
                
                if (cell.getCellType() == CellType.NUMERIC)
                {
                    if (cell.getAddress().getColumn() == 0)
                    {
                        objFormulaEvaluator.evaluate(cell);
                        String studentID = objDefaultFormat.formatCellValue(cell, objFormulaEvaluator);
                        Student student = new Student(studentID);
                        currentExam.getStudents().add(student);
                    } else
                    {
                        int point = (int)cell.getNumericCellValue();
                        int columnIndex = cell.getColumnIndex() -1;
                        
                        currentExam.getQuestions().get(columnIndex).AddPair(currentExam.getStudents().get(currentExam.getStudents().size()-1), point);
                    }

                } else
                {
                    if (cell.getCellType() == CellType.STRING)
                    {
                        // QUESTION TITLES

                        String cellStr = cell.getStringCellValue();

                        StringBuilder pointsBuilder = new StringBuilder();

                        boolean inParanthesis = false;
                        int counter = 0;

                        for (int i = 0; i < cellStr.length(); i++)
                        {
                            if (inParanthesis == true)
                            {
                                if (cellStr.charAt(i) == ')')
                                {
                                    inParanthesis = false;
                                } else
                                {
                                    pointsBuilder.append(cellStr.charAt(i));
                                }
                            } else
                            {
                                if (cellStr.charAt(i) == '(')
                                {
                                    inParanthesis = true;
                                }
                            }
                        }

                        String points = pointsBuilder.toString();

                        int point = 0;

                        try
                        {
                            point = Integer.parseInt(points);

                        } catch (NumberFormatException e)
                        {

                        }

                        Question q = new Question(point);
                        currentExam.getQuestions().add(q);

                    }
                }

            }
        }

    }

    public void LoadStudentXLSX() throws IOException
    {
        File myFile = new File(System.getProperty("user.dir"), "studentDataTest.xlsx");
        FileInputStream fis = new FileInputStream(myFile);

        XSSFWorkbook mw = new XSSFWorkbook(fis);

        XSSFSheet mySheet = mw.getSheetAt(0);

        Iterator<Row> rowIterator = mySheet.iterator();
        DataFormatter objDefaultFormat = new DataFormatter();
        FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) mw);

        boolean hasFinishedLectures = false;
        int lecturePerDate = 0;
        int cellCounter = 0;
        int attendanceDateCounter = 0;
        AttendanceInformation attInfo = null;

        while (rowIterator.hasNext())
        {

            Row row = rowIterator.next();

            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();

                // Student Numbers as Strings
                if (cell.getAddress().getColumn() == 0)
                {
                    if (cell.getCellType() == CellType.NUMERIC)
                    {
                        objFormulaEvaluator.evaluate(cell);
                        String studentID = objDefaultFormat.formatCellValue(cell, objFormulaEvaluator);
                        Student student = new Student(studentID);
                        // System.out.println("Adding Student To List + " + student);
                        currentCourse._Students.add(student);

                        if (!hasFinishedLectures)
                        {
                            int attDataSize = currentCourse._AttendanceDates.size();
                            int lectureDataSize = currentCourse._Lectures.size();
                            lecturePerDate = (lectureDataSize / attDataSize);
                            hasFinishedLectures = true;
                        }
                        attInfo = new AttendanceInformation();
                        attendanceDateCounter = 0;
                    }
                } else
                {
                    if (cell.getCellType() == CellType.STRING)
                    {
                        String cellStr = cell.getStringCellValue();

                        boolean isDate = IsInDateFormat(cellStr);

                        if (isDate)
                        {
                            try
                            {
                                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(cellStr);
                                AttendanceDate attData = new AttendanceDate(date);
                                currentCourse._AttendanceDates.add(attData);

                            } catch (Exception e)
                            {

                            }

                        } else
                        {
                            boolean isLecture = IsInLectureFormat(cellStr);

                            if (isLecture)
                            {
                                currentCourse._Lectures.add(cellStr);
                            } else
                            {
                                if (hasFinishedLectures)
                                {

                                    if (cellStr.equals("OK") || cellStr.equals(""))
                                    {
                                        attInfo.presentCount++;
                                    } else
                                    {
                                        if (cellStr.equals("ABSENT"))
                                        {
                                            attInfo.absentCount++;
                                        } else
                                        {
                                            if (cellStr.equals("NOT ENROLLED"))
                                            {
                                                attInfo.isNotEnrolled = true;
                                            }
                                        }
                                    }

                                    //System.out.println("Adding Student + " + _Students.get(_Students.size()-1) + " to the map of " + _AttendanceDates.get(attendanceDateCounter));
                                    currentCourse._AttendanceDates.get(attendanceDateCounter).AddPair(currentCourse._Students.get(currentCourse._Students.size() - 1), attInfo);

                                    cellCounter++;
                                    if (cellCounter == lecturePerDate)
                                    {
                                        cellCounter = 0;
                                        attendanceDateCounter++;
                                        attInfo = new AttendanceInformation();
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

        mw.close();
        fis.close();

    }

    private boolean IsInDateFormat(String s)
    {
        if (s.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})"))
        {
            return true;
        } else

        {
            return false;
        }
    }

    private boolean IsInLectureFormat(String s)
    {
        if (s.matches("([0-9]{1}).Lecture"))
        {
            return true;
        } else
        {
            return false;
        }
    }
}
