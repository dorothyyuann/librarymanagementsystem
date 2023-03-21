/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Calendar;
import java.util.Comparator;

/**
 *
 * @author dorothyyuan
 */
public class TimeComparator implements Comparator<Calendar> {

    @Override
    public int compare(Calendar c1, Calendar c2) {
        if (c1.get(Calendar.HOUR_OF_DAY) != c2.get(Calendar.HOUR_OF_DAY)) {
            return c1.get(Calendar.HOUR_OF_DAY) - c2.get(Calendar.HOUR_OF_DAY);
        }
        return c1.get(Calendar.MINUTE) - c2.get(Calendar.MINUTE);
    }
}
