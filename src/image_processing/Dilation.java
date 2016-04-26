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
public class Dilation extends AbstractImageProcess {

    @Override
    public BufferedImage process(BufferedImage input) {
        
        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage output = new BufferedImage(width, height, input.getType());
        
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++) {
                
                int currentARGB = input.getRGB(x, y);
                if(currentARGB==FRONT || currentARGB==BACK)
                    output.setRGB(x, y, currentARGB);
                else {
                    System.err.println("Erreur : Il faut appliquer un seuillage avant d'effectuer une dilatation.");
                    return null;
                }
            }
                
        int k = height - 1;
        int l = width - 1;
        
        for(int y = 1; y < k; y++)
            for(int x = 1; x < l; x++) {
                
                int currentARGB = input.getRGB(x, y);
                if(currentARGB == FRONT) {
                    output.setRGB(x, y-1, FRONT);
                    output.setRGB(x+1, y, FRONT);
                    output.setRGB(x, y+1, FRONT);
                    output.setRGB(x-1, y, FRONT);
                }
            }
        
        return output;
    }
    
}
