/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author InanEvin
 */
public class ResourceManager
{

    private Exam currentExam;
    private Section currentCourse;

    public void setCurrentExam(Exam e)
    {
        currentExam = e;
    }

    public void setCurrentSection(Section s)
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
        }
        catch (IOException ioe)
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
        }
        catch (IOException ioe)
        {
            
        }
        catch (ClassNotFoundException c)
        {
            
            return new ArrayList<Course>();
        }

        return list;
    }

    public void LoadExamXLSX(File file) throws IOException
    {
        File myFile = file;
        FileInputStream fis = new FileInputStream(myFile);
        XSSFWorkbook mw = new XSSFWorkbook(fis);
        XSSFSheet mySheet = mw.getSheetAt(0);

        Iterator<Row> rowIterator = mySheet.iterator();
        DataFormatter objDefaultFormat = new DataFormatter();
        FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) mw);
        currentExam.getStudents().clear();
        currentExam.getQuestions().clear();
        boolean first = true;
        boolean breakOuter = false;

        while (rowIterator.hasNext())
        {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();
                if (first)
                {
                    String firstCell = cell.getStringCellValue();

                    if (!firstCell.equals("EXAMDATA"))
                    {
                        currentExam.getStudents().clear();
                        currentExam.getQuestions().clear();
                        breakOuter = true;
                        break;
                    }
                    first = false;
                    continue;
                }

                if (cell.getCellType() == CellType.NUMERIC)
                {
                    if (cell.getAddress().getColumn() == 0)
                    {
                        objFormulaEvaluator.evaluate(cell);
                        String studentID = objDefaultFormat.formatCellValue(cell, objFormulaEvaluator);
                        Student student = new Student(studentID);
                        currentExam.getStudents().add(student);
                    }
                    else
                    {
                        int point = (int) cell.getNumericCellValue();
                        int columnIndex = cell.getColumnIndex() - 1;

                        currentExam.getQuestions().get(columnIndex).addStudentPointPair(currentExam.getStudents().get(currentExam.getStudents().size() - 1), point);
                    }
                }
                else
                {
                    if (cell.getCellType() == CellType.STRING)
                    {
                        String cellStr = cell.getStringCellValue();
                        StringBuilder pointsBuilder = new StringBuilder();

                        boolean inParanthesis = false;
                        int counter = 0;

                        for (int i = 0; i < cellStr.length(); i++)
                        {
                            if (inParanthesis == true)
                            {
                                if (cellStr.charAt(i) == ')')
                                    inParanthesis = false;
                                else
                                    pointsBuilder.append(cellStr.charAt(i));
                            }
                            else
                            {
                                if (cellStr.charAt(i) == '(')
                                    inParanthesis = true;
                            }
                        }

                        String points = pointsBuilder.toString();
                        int point = 0;

                        try
                        {
                            point = Integer.parseInt(points);

                        }
                        catch (NumberFormatException e)
                        {
                            e.printStackTrace();
                        }
                        Question q = new Question(point);
                        currentExam.getQuestions().add(q);
                    }
                }

            }

            if (breakOuter)
                break;
        }

    }

    public void LoadStudentXLSX(File file) throws IOException
    {
        File myFile = file;
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
        currentCourse.attendanceDateList.clear();
        currentCourse.lectureList.clear();
        currentCourse.studentList.clear();
        boolean first = true;
        boolean breakOuter = false;

        while (rowIterator.hasNext())
        {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();
                if (first)
                {
                    String firstCell = cell.getStringCellValue();

                    if (!firstCell.equals("ATTDATA"))
                    {
                        breakOuter = true;
                        break;
                    }

                    first = false;
                    continue;
                }

                // Student Numbers as Strings
                if (cell.getAddress().getColumn() == 0)
                {
                    if (cell.getCellType() == CellType.NUMERIC)
                    {
                        objFormulaEvaluator.evaluate(cell);
                        String studentID = objDefaultFormat.formatCellValue(cell, objFormulaEvaluator);
                        Student student = new Student(studentID);
                        // System.out.println("Adding Student To List + " + student);
                        currentCourse.studentList.add(student);

                        if (!hasFinishedLectures)
                        {
                            int attDataSize = currentCourse.attendanceDateList.size();
                            int lectureDataSize = currentCourse.lectureList.size();
                            try
                            {
                                lecturePerDate = (lectureDataSize / attDataSize);
                            }
                            catch (ArithmeticException e)
                            {
                                currentCourse.attendanceDateList.clear();
                                currentCourse.lectureList.clear();
                                currentCourse.studentList.clear();
                                break;
                            }
                            hasFinishedLectures = true;
                        }
                        attInfo = new AttendanceInformation();
                        attendanceDateCounter = 0;
                    }
                }
                else
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
                                currentCourse.attendanceDateList.add(attData);

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            boolean isLecture = IsInLectureFormat(cellStr);

                            if (isLecture)
                            {
                                currentCourse.lectureList.add(cellStr);
                            }
                            else
                            {
                                if (hasFinishedLectures)
                                {

                                    if (cellStr.equals("OK") || cellStr.equals(""))
                                    {
                                        attInfo.setPresentCount(attInfo.getPresentCount() + 1);
                                    }
                                    else
                                    {
                                        if (cellStr.equals("ABSENT"))
                                        {
                                            attInfo.setAbsentCount(attInfo.getAbsentCount() + 1);
                                        }
                                        else
                                        {
                                            if (cellStr.equals("NOT ENROLLED"))
                                            {
                                                attInfo.setIsNotEnrolled(true);
                                            }
                                        }
                                    }

                                    //System.out.println("Adding Student + " + _Students.get(_Students.size()-1) + " to the map of " + _AttendanceDates.get(attendanceDateCounter));
                                    currentCourse.attendanceDateList.get(attendanceDateCounter).addPair(currentCourse.studentList.get(currentCourse.studentList.size() - 1), attInfo);

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

            if (breakOuter)
            {
                break;
            }
        }

        mw.close();
        fis.close();

    }

    private boolean IsInDateFormat(String s)
    {
        return s.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})");

    }

    private boolean IsInLectureFormat(String s)
    {
        return s.matches("([0-9]{1}).Lecture");
    }
}
