/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package course.data.analyzer;

import java.util.Comparator;

/**
 *
 * @author InanEvin Comparator for sorting questions depending on their success
 * rate when needed.
 *
 */
public class QuestionSuccessRateComparator implements Comparator<Question>
{

    @Override
    public int compare(Question q1, Question q2)
    {
        if (q1.getSuccessRate() > q2.getSuccessRate())
            return 1;
        else if (q1.getSuccessRate() < q2.getSuccessRate())
            return -1;
        else
            return 0;
    }
}
