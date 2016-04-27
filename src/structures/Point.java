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

import java.awt.image.BufferedImage;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public class Point {
    
    public final int x;
    public final int y;
        
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    };
    
    public boolean isInside(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        return (x > 0 && x < w && y > 0 && y < h);
    }
}
