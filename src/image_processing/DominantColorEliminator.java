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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public class DominantColorEliminator extends AbstractImageProcess {
    
    List<Integer> colorsToRemove = new ArrayList<>();
    
    public DominantColorEliminator(long[] colorHistogram, int dominantColorCoef) {
        
        for(int i = 0; i < colorHistogram.length; i++)
            if(colorHistogram[i] > dominantColorCoef)
                colorsToRemove.add(i);
    }

    @Override
    public BufferedImage process(BufferedImage input) {
        
        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage output = new BufferedImage(width, height, input.getType());
        
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++) {
                
                int currentRGB = input.getRGB(x, y) & 0x00ffffff;
                output.setRGB(x, y, FRONT);
                for(Integer color : colorsToRemove) {
                    if((int)color == currentRGB) {
                        output.setRGB(x, y, BACK);
                        break;
                    }
                }
            }
        
        return output;
    }
}
