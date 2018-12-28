/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

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
    private ArrayList<Week> weekList;
    private ArrayList<String> learningOutcomeList;
    private int selectedWeek;
    private int selectedLearningOutcome = -1;

    public Syllabus()
    {
        weekList = new ArrayList<Week>();
        learningOutcomeList = new ArrayList<String>();
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
        calculateWeeks();
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
        calculateWeeks();
    }

    public ArrayList<String> getLearningOutcomes()
    {
        return learningOutcomeList;
    }

    public ArrayList<Week> getWeeks()
    {
        return weekList;
    }

    public int getSelectedWeek()
    {
        return selectedWeek;
    }

    public void setSelectedWeek(int i)
    {
        selectedWeek = i;
    }

    public int getSelectedLO()
    {
        return selectedLearningOutcome;
    }

    public void getSelectedLO(int i)
    {
        selectedLearningOutcome = i;
    }

    public void addLearningOutcome(String outcome)
    {
        learningOutcomeList.add(outcome);
        selectedLearningOutcome = learningOutcomeList.size() - 1;
    }

    public void editLearningOutcome(int index, String outcome)
    {
        if (index < 0 || index >= learningOutcomeList.size())
        {
            return;
        }

        learningOutcomeList.set(index, outcome);
    }

    public void removeLearningOutcome(int index)
    {
        if (index < 0 || index >= learningOutcomeList.size())
        {
            return;
        }

        learningOutcomeList.remove(index);
        selectedLearningOutcome--;

        if (learningOutcomeList.size() > 0 && selectedLearningOutcome == -1)
        {
            selectedLearningOutcome = 0;
        }
    }

    private void calculateWeeks()
    {
        if (startDate == null || endDate == null)
        {
            return;
        }

        int weekCount = getWeeksBetween(startDate, endDate) + 1;

        if (weekList.size() > weekCount)
        {
            int diff = weekList.size() - weekCount;
            for (int i = 0; i < diff; i++)
            {
                weekList.remove(weekList.size() - 1);
            }
        }
        else
        {
            int diff = weekCount - weekList.size();

            int noOfDays = 7;

            for (int i = 0; i < diff; i++)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDate);
                calendar.add(Calendar.DAY_OF_YEAR, noOfDays * i);
                Date date = calendar.getTime();
                weekList.add(new Week(date));
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
