/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 *
 * @author InanEvin
 *
 * Implements hashCode & equals because Student is used in hash maps.
 *
 */
public class Student implements Serializable
{

    private String name;
    private String id;

    public Student(String id)
    {
        this.id = id;
    }

    public String getID()
    {
        return id;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        else if (obj == null || getClass() != obj.getClass())
            return false;
        
        final Student other = (Student) obj;
        if (!Objects.equals(this.id, other.id))
            return false;
        
        return true;
    }

}
