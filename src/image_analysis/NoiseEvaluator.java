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
package image_analysis;

import java.awt.image.BufferedImage;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public class NoiseEvaluator {
    
    public long evaluate(BufferedImage image) {
        
        int width = image.getWidth();
        int height = image.getHeight();
        
        long noiseQuantity = 0;
        
        for(int y = 1; y < height-1; y++) {
            for(int x = 1; x < width-1; x++) {
                
                int currentARGB = image.getRGB(x, y);
                
                for(int j=-1; j<1; j++)
                    for(int i=-1; i<1; i++) {
                        
                        int comparableARGB = image.getRGB(x+i, y+j);
                        
                        int diffR = Math.abs(((currentARGB & 0x00ff0000) >> 16) - ((comparableARGB & 0x00ff0000) >> 16));
                        int diffG = Math.abs(((currentARGB & 0x0000ff00) >> 8) - ((comparableARGB & 0x0000ff00) >> 8));
                        int diffB = Math.abs((currentARGB & 0x000000ff) - (comparableARGB & 0x000000ff));

                        noiseQuantity += diffR + diffG + diffB;
                    }
            }
        }
        
        return noiseQuantity;
    }
    
}
