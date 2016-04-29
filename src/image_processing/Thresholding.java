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
public class Thresholding extends AbstractImageProcess {

    final private int ALimit;
    final private int RLimit;
    final private int GLimit;
    final private int BLimit;

    public Thresholding(int thresholdARGB) {
        super();
        this.ALimit = (thresholdARGB & 0xff000000) >> 24;
        this.RLimit = (thresholdARGB & 0x00ff0000) >> 16;
        this.GLimit = (thresholdARGB & 0x0000ff00) >> 8;
        this.BLimit = (thresholdARGB & 0x000000ff);
    }
    
    @Override
    public BufferedImage process(BufferedImage input) {
        
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
        
        for(int x = 0; x < input.getWidth(); x++)
            for(int y = 0; y < input.getHeight(); y++)
            {
                int pixelColor = input.getRGB(x, y);
                int A = (pixelColor & 0xff000000) >> 24;
                int R = (pixelColor & 0x00ff0000) >> 16;
                int G = (pixelColor & 0x0000ff00) >> 8;
                int B = (pixelColor & 0x000000ff);     
                
                if(A > ALimit || R > RLimit || G > GLimit || B > BLimit)
                    output.setRGB(x, y, 0xffffffff);
                else
                    output.setRGB(x, y, 0xff000000);
            }
        
        return output;
    }
    
}
