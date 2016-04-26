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
public class AccumulatorMask extends AbstractImageProcess {
    
    private final int[][] intervalMask;

    public AccumulatorMask(int[][] intervalMask) 
    {
        this.intervalMask = intervalMask;
    }
    
    @Override
    public BufferedImage process(BufferedImage input) {
        
        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage output = new BufferedImage(width, height, input.getType());
        
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++) {
                output.setRGB(x, y, input.getRGB(x, y));
            }
        
        for (int[] intervalMask1 : intervalMask) {
            for (int y = intervalMask1[0]; y < intervalMask1[1]; y++) {
                for(int x = 0; x < width; x++) {
                    output.setRGB(x, y, BACK);
                }
            }
        }
        
        return output;
    }
    
}
