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
public class Skeletonization extends AbstractImageProcess {

    private static final int FOND = 0xff000000;
    private static final int FORME = 0xffffffff;
    
    @Override
    public BufferedImage process(BufferedImage input) 
    {
        BufferedImage output = pass(input, true);
        output = pass(output, false);
        
        return output;
    }
    
    private BufferedImage pass(BufferedImage input, boolean isFirstPass) {
        
        int width = input.getWidth();
        int height = input.getHeight();
        boolean isSecondPass = !isFirstPass;

        BufferedImage output = new BufferedImage(width, height, input.getType());
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++)
                output.setRGB(x, y, FOND);
                
        boolean[] v = new boolean[8];
        
        for(int y = 1; y < height-1; y++)
            for(int x = 1; x < width-1; x++) {
                
                int currentPix = input.getRGB(x, y);
                if(currentPix == FOND) {
                    output.setRGB(x, y, FOND);
                }
                else {
                    v[0] = (input.getRGB(x,     y+1)    == FOND);
                    v[1] = (input.getRGB(x+1,   y+1)    == FOND);
                    v[2] = (input.getRGB(x+1,   y)      == FOND);
                    v[3] = (input.getRGB(x+1,   y-1)    == FOND);
                    v[4] = (input.getRGB(x,     y-1)    == FOND);
                    v[5] = (input.getRGB(x-1,   y-1)    == FOND);
                    v[6] = (input.getRGB(x-1,   y)      == FOND);
                    v[7] = (input.getRGB(x-1,   y+1)    == FOND);
                    
                    int countTransitions = 0;
                    //int countTrueNeighbour = 0;
                    for(int i=0; i<v.length; i++) {
                        if(i!=0 && (v[i-1] != v[i]))
                            countTransitions++;
                        /*if(v[i])
                            countTrueNeighbour++;*/
                    }
                    
                    if( /*(countTrueNeighbour >= 2 && countTrueNeighbour <= 6)
                        ||*/ countTransitions == 1
                        ||  (isFirstPass  && ((v[0] | v[2] | v[4]) || (v[2] | v[4] | v[6])))
                        ||  (isSecondPass && ((v[0] | v[2] | v[6]) || (v[0] | v[4] | v[6])))
                    )
                        output.setRGB(x, y, FOND);
                    else
                        output.setRGB(x, y, FORME);
                }
            }
        
        return output;
    }

}
