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
    private Date m_Date;
    private String m_Topic;
    private float successScore;
    public Date getDate() { return m_Date;}
    public String getTopic() { return m_Topic; }
    public void setTopic(String tpc) { m_Topic = tpc; }
    
    public void SetSuccessScore(float s)
    {
        successScore = s;
        
    }
    
    public float GetSuccessScore()
    {
        return successScore;
    }
    public Week(Date date)
    {
        m_Date = date;
    }
}
