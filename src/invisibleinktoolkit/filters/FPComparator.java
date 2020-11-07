// 
// Decompiled by Procyon v0.5.36
// 

package invisibleinktoolkit.filters;

import java.util.Comparator;

public class FPComparator implements Comparator
{
    public int compare(final Object o1, final Object o2) {
        final FilteredPixel a = (FilteredPixel)o1;
        final FilteredPixel b = (FilteredPixel)o2;
        if (a.getFilterValue() == b.getFilterValue()) {
            if (a.getX() == b.getX()) {
                if (a.getY() == b.getY()) {
                    return 0;
                }
                if (a.getY() > b.getY()) {
                    return 20;
                }
                return -20;
            }
            else {
                if (a.getX() > b.getX()) {
                    return 50;
                }
                return -50;
            }
        }
        else {
            if (a.getFilterValue() < b.getFilterValue()) {
                return -100;
            }
            return 100;
        }
    }
    
    public boolean equals(final Object o1, final Object o2) {
        final FilteredPixel a = (FilteredPixel)o1;
        final FilteredPixel b = (FilteredPixel)o2;
        return a.getFilterValue() == b.getFilterValue() && a.getX() == b.getX() && a.getY() == b.getY();
    }
}
