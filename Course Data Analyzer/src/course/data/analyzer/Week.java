/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author InanEvin
 */
public class Week implements Serializable
{
    private Date date;
    private String topic;
    private float successScore;
    
    public Week(Date date)
    {
        date = date;
    }
    
    public Date getDate()
    {
        return date;
    }

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String tpc)
    {
        topic = tpc;
    }

    public void setSuccessScore(float s)
    {
        successScore = s;
    }

    public float getSuccessScore()
    {
        return successScore;
    }

}
