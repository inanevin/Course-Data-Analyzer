/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author InanEvin
 *
 * Each date column in student attendance sheet instantiates one object of this
 * class. The map holds student and attendance information pairs while date
 * represents the column.
 *
 */
public class AttendanceDate implements Serializable
{

    private Date date;
    private LinkedHashMap<Student, AttendanceInformation> studentAttendanceInfo;

    public AttendanceDate(Date date)
    {
        this.date = date;
        studentAttendanceInfo = new LinkedHashMap<Student, AttendanceInformation>();
    }

    public Date getDate()
    {
        return date;
    }

    public LinkedHashMap<Student, AttendanceInformation> getStudentAttendancePair()
    {
        return studentAttendanceInfo;
    }

    public void addPair(Student s, AttendanceInformation i)
    {
        studentAttendanceInfo.put(s, i);
    }
}
