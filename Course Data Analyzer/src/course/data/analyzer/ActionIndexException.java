/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

/**
 *
 * @author InanEvin
 *
 * Custom exception used when a course action is requested to be recorded with a
 * wrong type index. ( Course actions are adding and removing a course, they are
 * tracked for undo operations. )
 *
 */
public class ActionIndexException extends Exception
{

    public ActionIndexException(String msg)
    {
        super(msg);
    }
}
