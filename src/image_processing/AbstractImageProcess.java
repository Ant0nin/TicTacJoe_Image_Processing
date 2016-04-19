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

package image_processing;

import java.awt.image.BufferedImage;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public abstract class AbstractImageProcess {

    public abstract BufferedImage process(BufferedImage input);
    
    protected int getB(int rgb) {
        return rgb & 0xFF;
    }
    
    protected int getG(int rgb) {
        return (rgb>>8) & 0xFF;
    }
    
    protected int getR(int rgb) {
        return (rgb>>16);
    }
    
    protected int makeRGB(int r, int g, int b) {
        return ((b&0xFF) + ((g&0xFF)<<8) + ((r&0xFF)<<16) + (0xFF<<24));
    }
    
}
