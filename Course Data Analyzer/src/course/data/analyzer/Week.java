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
public class Week implements Serializable
{
    
    private String m_Topic;
    
    public String getTopic() { return m_Topic; }
    public void setTopic(String tpc) { m_Topic = tpc; }
    
}
