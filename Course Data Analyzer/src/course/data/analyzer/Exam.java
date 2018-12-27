/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.io.Serializable;
import java.util.ArrayList;
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

    private ArrayList<Question> _Questions;
    private ExamType m_Type;
    private int m_Percentage;
    private Date m_Date;
    private int i_SelectedQuestion = -1;
    
    private ArrayList<Student> _Students;

    public ArrayList<Student> getStudents()
    {
        return _Students;
    }
    


    public int GetSelectedQuestionIndex()
    {
        return i_SelectedQuestion;
    }

    public Question GetSelectedQuestion()
    {
        return _Questions.get(i_SelectedQuestion);
    }

    public void SetSelectedQuestion(int i)
    {
        i_SelectedQuestion = i;
    }

    public ArrayList<Question> getQuestions()
    {
        return _Questions;
    }

    public void AddQuestion(Question q)
    {
        _Questions.add(q);
        i_SelectedQuestion = _Questions.size() - 1;
    }

    public void RemoveQuestion(int index)
    {
        if (index < 0 || index > _Questions.size())
        {
            return;
        }

        _Questions.remove(index);
        i_SelectedQuestion--;

        if (i_SelectedQuestion < 0 && _Questions.size() > 0)
        {
            i_SelectedQuestion = 0;
        }
    }

    public Exam(ExamType type, int percentage, Date date)
    {
        m_Type = type;
        m_Percentage = percentage;
        m_Date = date;

        if (_Questions == null)
        {
            _Questions = new ArrayList<Question>();
        }
        
        if(_Students == null)
        {
            _Students = new ArrayList<Student>();
        }
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
