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

    private String m_Name;

    public ArrayList<AttendanceDate> _AttendanceDates;
    public ArrayList<String> _Lectures;
    public ArrayList<Student> _Students;

    public ArrayList<AttendanceDate> GetAttendanceDates()
    {
        return _AttendanceDates;
    }

    public int GetAbsenteeismUntilDate(Date date)
    {
        int count = 0;
        for (int i = 0; i < _AttendanceDates.size(); i++)
        {
            if (date.after(_AttendanceDates.get(i).getDate()))
            {
                count += GetTotalAbsentheismCountAtDate(i);
            }
        }

        return count;
    }

    public int GetTotalAbsentheismCountAtDate(int attIndex)
    {
        int total = 0;
        for (int i = 0; i < _Students.size(); i++)
        {
            AttendanceInformation info = _AttendanceDates.get(attIndex).getStudentAttendancePair().get(_Students.get(i));
            total += info.getAbsentCount();
        }

        return total;
    }

    public int GetAbsenteeismOfStudentAtDate(int attIndex, int studentIndex)
    {

        AttendanceInformation info = _AttendanceDates.get(attIndex).getStudentAttendancePair().get(_Students.get(studentIndex));

        return info.getAbsentCount();
    }

    public int GetAbsenteeismOfStudentUntilDate(Date date, int studentIndex)
    {

        int count = 0;
        for (int i = 0; i < _AttendanceDates.size(); i++)
        {
            if (date.after(_AttendanceDates.get(i).getDate()))
            {
                AttendanceInformation info = _AttendanceDates.get(i).getStudentAttendancePair().get(_Students.get(studentIndex));
                count += info.getAbsentCount();
            }
        }

        return count;
    }

    public void PrintStudentAttendanceLoad()
    {

        for (int i = 0; i < _Students.size(); i++)
        {
            System.out.println("Student ID: " + _Students.get(i).getStringID() + "\n\n");

            for (int j = 0; j < _AttendanceDates.size(); j++)
            {
                System.out.println(j + ". Attendance Date: " + _AttendanceDates.get(j).getDate() + "\n");

                System.out.println("PresentCount: " + _AttendanceDates.get(j).getStudentAttendancePair().get(_Students.get(i)).getPresentCount());
                System.out.println("AbsentCount: " + _AttendanceDates.get(j).getStudentAttendancePair().get(_Students.get(i)).getAbsentCount());

            }
        }
    }

    public ArrayList<Student> GetStudents()
    {
        return _Students;
    }

    public String GetName()
    {
        return m_Name;
    }

    public void SetName(String n)
    {
        m_Name = n;
    }

    public void AddNewStudent(Student s)
    {
        _Students.add(s);
    }

    public void RemoveStudent(int index)
    {
        if (index < 0 || index >= _Students.size())
        {
            return;
        }

        _Students.remove(index);

    }

    public boolean CheckIfStudentExists(int ID)
    {

        for (int i = 0; i < _Students.size(); i++)
        {
            if (_Students.get(i).getID() == ID)
            {
                return true;
            }
        }

        return false;
    }

    public Section(String n)
    {
        m_Name = n;

        if (_Students == null)
        {
            _Students = new ArrayList<Student>();

        }

        if (_AttendanceDates == null)
        {
            _AttendanceDates = new ArrayList<AttendanceDate>();

        }

        if (_Lectures == null)
        {
            _Lectures = new ArrayList<String>();

        }
    }

    public Section()
    {

        if (_Students == null)
        {
            _Students = new ArrayList<Student>();

        }

        if (_AttendanceDates == null)
        {
            _AttendanceDates = new ArrayList<AttendanceDate>();

        }

        if (_Lectures == null)
        {
            _Lectures = new ArrayList<String>();

        }
    }

}
