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
    private int m_Points;
    private ArrayList<Integer> _RelatedTopics;
    private ArrayList<Integer> _RelatedLearningOutcomes;
    public LinkedHashMap<Student, Integer> studentPointPairs = new LinkedHashMap<Student,Integer>();
    
    public LinkedHashMap<Student, Integer> getMap()
    {
        return studentPointPairs;
    }
    
    public void setSuccessRate(float rate)
    {
        successRate = rate;
    }
    
    public float getSuccessRate()
    {
        return successRate;
    }
    
    public void AddPair(Student s, int p)
    {
        studentPointPairs.put(s, p);
    }
    
    public ArrayList<Integer> GetTopicList()
    {
        return _RelatedTopics;
    }

    public ArrayList<Integer> GetLOList()
    {
        return _RelatedLearningOutcomes;
    }

    public int getPoints()
    {
        return m_Points;
    }

    public void setPoints(int i)
    {
        m_Points = i;
    }

    public Question()
    {
        if (_RelatedTopics == null)
        {
            _RelatedTopics = new ArrayList<Integer>();
        }

        if (_RelatedLearningOutcomes == null)
        {
            _RelatedLearningOutcomes = new ArrayList<Integer>();
        }
    }
    
     public Question(int p)
    {
        m_Points = p;
        if (_RelatedTopics == null)
        {
            _RelatedTopics = new ArrayList<Integer>();
        }

        if (_RelatedLearningOutcomes == null)
        {
            _RelatedLearningOutcomes = new ArrayList<Integer>();
        }
    }

    
    public void ClearLOList()
    {
        _RelatedLearningOutcomes.clear();
    }
    
    public void ClearTopicsList()
    {
        _RelatedTopics.clear();
    }
    public void AddRelatedTopic(int topic)
    {
        _RelatedTopics.add(topic);
    }

    public void RemoveRelatedTopic(int topic)
    {
        _RelatedTopics.remove((Integer) topic);
    }

    public void AddRelatedLO(int lo)
    {
        _RelatedLearningOutcomes.add(lo);
       
    }

    public void RemoveRelatedLO(int lo)
    {
        _RelatedLearningOutcomes.remove((Integer) lo);
    }

}
