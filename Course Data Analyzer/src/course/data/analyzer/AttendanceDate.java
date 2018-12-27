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
 */
public class AttendanceDate implements Serializable
{
    private Date date;
    private LinkedHashMap<Student, AttendanceInformation> studentAttendancePairs = new LinkedHashMap<Student,AttendanceInformation>();
    
    public LinkedHashMap<Student,AttendanceInformation> GetMap()
    {
        return studentAttendancePairs;
    }
    
    public AttendanceDate(Date date)
    {
        this.date = date;
    }
    public void AddPair(Student s, AttendanceInformation i)
    {
        studentAttendancePairs.put(s,i);
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
    
    
}
