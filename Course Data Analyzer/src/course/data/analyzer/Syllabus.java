/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import com.toedter.calendar.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author InanEvin
 */
public class Syllabus implements Serializable
{

    private Date startDate;
    private Date endDate;
    private ArrayList<Week> _Weeks;
    private ArrayList<String> _LearningOutcomes;
    
    
    
    public Syllabus()
    {
        _Weeks = new ArrayList<Week>();
        _LearningOutcomes = new ArrayList<String>();
        
    }

    public ArrayList<String> getLearningOutcomes()
    {
        return _LearningOutcomes;
    }
    
    public ArrayList<Week> getWeeks()
    {
        return _Weeks;
    }

    public void AddLearningOutcome(String outcome)
    {
        _LearningOutcomes.add(outcome);
    }
    
    public void EditLearningOutcome(int index, String outcome)
    {
        if(index < 0 || index >= _LearningOutcomes.size()) return;
        
        _LearningOutcomes.set(index, outcome);
    }
    
    public void RemoveLearningOutcome(int index)
    {
        if(index < 0 || index >= _LearningOutcomes.size()) return;
        
        _LearningOutcomes.remove(index);
    }
    
    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
        CalculateWeeks();
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
        CalculateWeeks();
    }

    private void CalculateWeeks()
    {
        if(startDate == null || endDate == null) return;
        
        int weekCount = getWeeksBetween(startDate, endDate)+1;
        
       
        if(_Weeks.size() > weekCount)
        {
            int diff = _Weeks.size() - weekCount;
            for(int i = 0; i < diff; i++)
            {
                _Weeks.remove(_Weeks.size()-1);
            }
        }
        else
        {
            int diff = weekCount - _Weeks.size();
            
            for(int i = 0; i < diff; i++)
            {
                _Weeks.add(new Week());
            }
        }
    }

    public static int getWeeksBetween(Date a, Date b)
    {

        if (b.before(a))
        {
            return -getWeeksBetween(b, a);
        }
        a = resetTime(a);
        b = resetTime(b);

        Calendar cal = new GregorianCalendar();
        cal.setTime(a);
        int weeks = 0;
        while (cal.getTime().before(b))
        {
            // add another week
            cal.add(Calendar.WEEK_OF_YEAR, 1);
            weeks++;
        }
        return weeks;
    }

    public static Date resetTime(Date d)
    {
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}
