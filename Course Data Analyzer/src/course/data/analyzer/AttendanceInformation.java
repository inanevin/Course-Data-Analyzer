/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.io.Serializable;

/**
 *
 * @author InanEvin
 */
public class AttendanceInformation implements Serializable
{
    public int presentCount;
    public int absentCount;
    public boolean isNotEnrolled;
}
