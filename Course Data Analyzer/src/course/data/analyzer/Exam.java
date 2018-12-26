/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author InanEvin
 */
public class Exam implements Serializable
{

    public enum ExamType
    {
        Midterm(0),
        Final(1),
        Quiz(2),
        Lab(3),
        Other(4);

        private int value;

        private static Map map = new HashMap<>();

        private ExamType(int value)
        {
            this.value = value;
        }

        static
        {
            for (ExamType pageType : ExamType.values())
            {
                map.put(pageType.value, pageType);
            }
        }

        public static ExamType valueOf(int pageType)
        {
            return (ExamType) map.get(pageType);
        }

        public int getValue()
        {
            return value;
        }
    }

    public ExamType m_Type;
    public int m_Percentage;
    public Date m_Date;

    public Exam(ExamType type, int percentage, Date date)
    {
        m_Type = type;
        m_Percentage = percentage;
        m_Date = date;
    }

    public ExamType getType()
    {
        return m_Type;
    }

    public void setType(ExamType type)
    {
        this.m_Type = type;
    }

    public int getPercentage()
    {
        return m_Percentage;
    }

    public void setPercentage(int percentage)
    {
        this.m_Percentage = percentage;
    }

    public Date getDate()
    {
        return m_Date;
    }

    public void setDate(Date date)
    {
        this.m_Date = date;
    }

}
