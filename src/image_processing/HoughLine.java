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
public class HoughLine extends AbstractImageProcess implements IHough {

    private final int linesQuantity;
    private final AccumulatorMask accMask;
    
    public HoughLine(int linesQuantity) {
        this(linesQuantity, null);
    }
    
    public HoughLine(int linesQuantity, AccumulatorMask accMask) {
        super();
        this.linesQuantity = linesQuantity;
        this.accMask = accMask;
    }
    
    @Override
    public BufferedImage process(BufferedImage input) {

        int width = input.getWidth();
        int height = input.getHeight();
        int rmax = (int) Math.sqrt(width * width + height * height);
                
        BufferedImage acc = new HoughLineAccumulator(rmax).process(input);
        if(accMask != null)
            acc = accMask.process(acc);
        int[] maxima = findMax(acc, rmax, 180, linesQuantity);
        BufferedImage output = plotLines(input, maxima);

        return output;
    }
    
    private BufferedImage plotLines(BufferedImage input, int[] results) {
        
        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage output = new BufferedImage(width, height, input.getType());
        
        for(int i=linesQuantity-1; i>=0; i--){
            
            int value = results[i*3];
            int r = results[i*3+1];
            int theta = results[i*3+2];
                                    
            for(int x=0;x<width;x++) {

                for(int y=0;y<height;y++) {

                    int temp = (int)(x*Math.cos(((theta)*Math.PI)/180) + y*Math.sin(((theta)*Math.PI)/180));

                    if((temp - r) == 0)
                        output.setRGB(x, y, 0xff000000 | (value << 16 ) | (value << 8) | value);
                }
            }
	}
        
        return output;
    }

}
