/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author InanEvin
 */
public class Question implements Serializable
{

    private float successRate;
    private int totalPoints;
    private ArrayList<Integer> topicsIndex;
    private ArrayList<Integer> learningOutcomesIndex;
    public LinkedHashMap<Student, Integer> studentPointPairs;

    // Used by resource manager when questions are red.
    public Question(int p)
    {
        totalPoints = p;
        studentPointPairs = new LinkedHashMap<Student, Integer>();
        topicsIndex = new ArrayList<Integer>();
        learningOutcomesIndex = new ArrayList<Integer>();
    }

    public LinkedHashMap<Student, Integer> getStudentPointPair()
    {
        return studentPointPairs;
    }

    public float getSuccessRate()
    {
        return successRate;
    }

    public void setSuccessRate(float rate)
    {
        successRate = rate;
    }

    public int getPoints()
    {
        return totalPoints;
    }

    public void setPoints(int i)
    {
        totalPoints = i;
    }

    public ArrayList<Integer> getTopicList()
    {
        return topicsIndex;
    }

    public ArrayList<Integer> getLOList()
    {
        return learningOutcomesIndex;
    }

    public void clearLOList()
    {
        learningOutcomesIndex.clear();
    }

    public void clearTopicList()
    {
        topicsIndex.clear();
    }

    public void addTopic(int topic)
    {
        topicsIndex.add(topic);
    }

    public void removeTopic(int topic)
    {
        topicsIndex.remove((Integer) topic);
    }

    public void addLO(int lo)
    {
        learningOutcomesIndex.add(lo);
    }

    public void removeLO(int lo)
    {
        learningOutcomesIndex.remove((Integer) lo);
    }

    public void addStudentPointPair(Student s, int p)
    {
        studentPointPairs.put(s, p);
    }

}
