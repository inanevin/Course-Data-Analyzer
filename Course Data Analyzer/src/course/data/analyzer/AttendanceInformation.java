/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.io.Serializable;

/**
 *
 * @author InanEvin Used to hold the attendance information of a student in
 * attendance excel sheet per date column.
 *
 */
public class AttendanceInformation implements Serializable
{

    private int presentCount;
    private int absentCount;
    private boolean isNotEnrolled;

    public int getPresentCount()
    {
        return presentCount;
    }

    public void setPresentCount(int presentCount)
    {
        this.presentCount = presentCount;
    }

    public int getAbsentCount()
    {
        return absentCount;
    }

    public void setAbsentCount(int absentCount)
    {
        this.absentCount = absentCount;
    }

    public boolean isIsNotEnrolled()
    {
        return isNotEnrolled;
    }

    public void setIsNotEnrolled(boolean isNotEnrolled)
    {
        this.isNotEnrolled = isNotEnrolled;
    }
}
