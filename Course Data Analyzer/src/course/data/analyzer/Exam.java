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

    // Enumeration for exam type, utilities are added to map the types to int for proper conversion of int to enum and vice versa.
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
            for (ExamType examType : ExamType.values())
            {
                map.put(examType.value, examType);
            }
        }

        public static ExamType valueOf(int examType)
        {
            return (ExamType) map.get(examType);
        }

        public int getValue()
        {
            return value;
        }
    }

    private int percentage;
    private int selectedQuestion = -1;
    private float studentScoreOverTotal;
    private ArrayList<Student> studentList;
    private ArrayList<Question> questionList;
    private ExamType type;
    private Date date;

    public Exam(ExamType type, int percentage, Date date)
    {
        this.type = type;
        this.percentage = percentage;
        this.date = date;
        questionList = new ArrayList<Question>();
        studentList = new ArrayList<Student>();
    }

    public int getPercentage()
    {
        return percentage;
    }

    public void setPercentage(int percentage)
    {
        this.percentage = percentage;
    }
    
    public float getStudentScoreOverTotal()
    {
        return studentScoreOverTotal;
    }
    
    public int getSelectedQuestionIndex()
    {
        return selectedQuestion;
    }

    public Question getSelectedQuestion()
    {
        return questionList.get(selectedQuestion);
    }

    public void setSelectedQuestion(int i)
    {
        selectedQuestion = i;
    }

    public ArrayList<Question> getQuestions()
    {
        return questionList;
    }
    
    public ArrayList<Student> getStudents()
    {
        return studentList;
    }

    public ExamType getType()
    {
        return type;
    }

    public void setType(ExamType type)
    {
        this.type = type;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }


    public ArrayList<Question> calculateQuestionSuccessRate()
    {
        studentScoreOverTotal = 0;
        for (int i = 0; i < questionList.size(); i++)
        {
            float questionMaxScore = questionList.get(i).getPoints();
            float totalQuestionScore = 0;
            float totalStudentScore = 0;
            for (int j = 0; j < studentList.size(); j++)
            {
                totalQuestionScore += questionMaxScore * (i + 1);

                totalStudentScore += questionList.get(i).getStudentPointPair().get(studentList.get(j));
            }

            float rate = totalStudentScore / totalQuestionScore;
            questionList.get(i).setSuccessRate(rate);
            studentScoreOverTotal += totalStudentScore / studentList.size();
        }

        ArrayList<Question> copyList = new ArrayList<Question>(questionList);
        return copyList;
    }

    public void addQuestion(Question q)
    {
        questionList.add(q);
        selectedQuestion = questionList.size() - 1;
    }

    public void removeQuestion(int index)
    {
        if (index < 0 || index > questionList.size())
            return;

        questionList.remove(index);
        selectedQuestion--;

        if (selectedQuestion < 0 && questionList.size() > 0)
            selectedQuestion = 0;
    }

}
