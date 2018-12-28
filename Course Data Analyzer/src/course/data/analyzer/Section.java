/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author InanEvin
 */
public class Section implements Serializable
{

    private String name;
    public ArrayList<AttendanceDate> attendanceDateList;
    public ArrayList<String> lectureList;
    public ArrayList<Student> studentList;

    public Section(String n)
    {
        name = n;
        studentList = new ArrayList<Student>();
        attendanceDateList = new ArrayList<AttendanceDate>();
        lectureList = new ArrayList<String>();
    }

    public String GetName()
    {
        return name;
    }

    public void SetName(String n)
    {
        name = n;
    }

    public ArrayList<AttendanceDate> getAttendanceDates()
    {
        return attendanceDateList;
    }

    public ArrayList<Student> GetStudents()
    {
        return studentList;
    }

    public int getAbsenteeismUntilDate(Date date)
    {
        int count = 0;
        for (int i = 0; i < attendanceDateList.size(); i++)
        {
            if (date.after(attendanceDateList.get(i).getDate()))
            {
                count += getTotalAbsenteeismCountAtDate(i);
            }
        }
        return count;
    }

    public int getTotalAbsenteeismCountAtDate(int attIndex)
    {
        int total = 0;
        for (int i = 0; i < studentList.size(); i++)
        {
            AttendanceInformation info = attendanceDateList.get(attIndex).getStudentAttendancePair().get(studentList.get(i));
            total += info.getAbsentCount();
        }
        return total;
    }

    public int getAbsenteeismOfStudentAtDate(int attIndex, int studentIndex)
    {
        AttendanceInformation info = attendanceDateList.get(attIndex).getStudentAttendancePair().get(studentList.get(studentIndex));
        return info.getAbsentCount();
    }

    public int getAbsenteeismOfStudentUntilDate(Date date, int studentIndex)
    {
        int count = 0;
        for (int i = 0; i < attendanceDateList.size(); i++)
        {
            if (date.after(attendanceDateList.get(i).getDate()))
            {
                AttendanceInformation info = attendanceDateList.get(i).getStudentAttendancePair().get(studentList.get(studentIndex));
                count += info.getAbsentCount();
            }
        }
        return count;
    }

    public void PrintStudentAttendanceLoad()
    {
        for (int i = 0; i < studentList.size(); i++)
        {
            System.out.println("Student ID: " + studentList.get(i).getID() + "\n\n");
            for (int j = 0; j < attendanceDateList.size(); j++)
            {
                System.out.println(j + ". Attendance Date: " + attendanceDateList.get(j).getDate() + "\n");
                System.out.println("PresentCount: " + attendanceDateList.get(j).getStudentAttendancePair().get(studentList.get(i)).getPresentCount());
                System.out.println("AbsentCount: " + attendanceDateList.get(j).getStudentAttendancePair().get(studentList.get(i)).getAbsentCount());
            }
        }
    }

    public void AddNewStudent(Student s)
    {
        studentList.add(s);
    }

    public void RemoveStudent(int index)
    {
        if (index < 0 || index >= studentList.size())
            return;

        studentList.remove(index);
    }

}
