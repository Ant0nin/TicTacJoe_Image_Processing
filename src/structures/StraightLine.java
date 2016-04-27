/*
 * Copyright (C) 2016 Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package structures;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public class StraightLine {

    public int x1;
    public int x2;
    public int y1;
    public int y2;
    public Float a;
    public Float b;

    public StraightLine() {
    }

    public StraightLine(Point p1, Point p2) {
        setP1(p1);
        setP2(p2);
        evaluate();
    }

    public void setP1(Point p1) {
        this.x1 = p1.x;
        this.y1 = p1.y;
    }

    public void setP2(Point p2) {
        this.x2 = p2.x;
        this.y2 = p2.y;
    }

    public void evaluate() {
        evaluateSlope();
        evaluateOffset();
    }

    public void evaluateSlope() {
        if (x1 == x2 && y1 == y2) {
            a = null;
        } else if (y1 == y2) {
            a = 0F;
        } else if (x1 == x2) {
            a = null;
        } else {
            a = (float) (y1 - y2) / (float) (x1 - x2);
        }
    }

    public void evaluateOffset() {
        if (x1 == x2) {
            b = null;
        } else {
            b = y1 - a * x1;
        }
    }

    public static Point calcIntersect(StraightLine line1, StraightLine line2) {

        Float a1 = line1.a;
        Float a2 = line2.a;
        Float b1 = line1.b;
        Float b2 = line2.b;
        
        // null = infinite => absolute vertical line OR line is a point

        int x, y;
        
        if (a1 == null && a2 == null) // vertical parallel
            return null;
        else if (a1 != null && a1.equals(0F) && a2 == null) {
            x = line2.y1;
            y = line1.x1;
        }
        else if (a1 == null && a2!=null && a2.equals(0F)) {
            x = line1.x1;
            y = line2.y1;
        }
        else if(a1!=null && a2 == null) {
            y = line2.y1;
            x = (int)(((float)y - b1) / a1);
        }
        else if(a1 == null && a2 != null) {
            x = line1.x1;
            y = (int)(a2 * (float)x + b2);
        }
        else if ((a1 - a2) == 0) // parallel
            return null;
        else {            
            x = (int) ((float) (b2 - b1) / (float) (a1 - a2));
            y = (int) (a1 * (float)x + b1);
        }
        
        return new Point(x, y);
    }

}
