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

    private static final int BACK = 0xff000000;
    private static final int FRONT = 0xffffffff;
    
    private static final boolean ON = true;
    private static final boolean OFF = false;
    
    @Override
    public BufferedImage process(BufferedImage input) 
    {
        int width = input.getWidth();
        int height = input.getHeight();
        
        Boolean[][] bImage = new Boolean[width][height];
        Boolean[][] classificator = new Boolean[width][height];
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++) {
                classificator[x][y] = OFF;
                switch (input.getRGB(x, y)) {
                    case BACK:
                        bImage[x][y] = OFF;
                        break;
                    case FRONT:
                        bImage[x][y] = ON;
                        break;
                    default:
                        System.err.println("Erreur : Il faut effectuer un seuillage avant d'appliquer une squeletisation");
                        break;
                }
            }
        
        pass(bImage, classificator, width, height, true);
        pass(bImage, classificator, width, height, false);
        
        BufferedImage output = new BufferedImage(width, height, input.getType());
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++) {
                output.setRGB(x, y, bImage[x][y] ? FRONT : BACK);
            }
        
        return output;
    }
    
    private void pass(Boolean[][] bImage, Boolean[][] classificator, int width, int height, boolean isFirstPass) {
        
        boolean isSecondPass = !isFirstPass;
                
        boolean[] v = new boolean[8];
        
        for(int y = 1; y < height-1; y++)
            for(int x = 1; x < width-1; x++) {
                
                boolean currentPix = bImage[x][y];
                if(currentPix == OFF) {
                    classificator[x][y] = OFF;
                }
                else {
                    v[0] = (bImage[x]  [y+1] == OFF);
                    v[1] = (bImage[x+1][y+1] == OFF);
                    v[2] = (bImage[x+1][y]   == OFF);
                    v[3] = (bImage[x+1][y-1] == OFF);
                    v[4] = (bImage[x]  [y-1] == OFF);
                    v[5] = (bImage[x-1][y-1] == OFF);
                    v[6] = (bImage[x-1][y]   == OFF);
                    v[7] = (bImage[x-1][y+1] == OFF);
                    
                    int countNeighbour = 0;
                    for(int i=0; i<v.length; i++)
                        if(v[i])
                            countNeighbour++;
                    
                    if(!(countNeighbour >= 2 &&  countNeighbour <= 6))
                        continue;
                    
                    int countTransitions = 0;
                    for(int i=1; i<v.length; i++)
                        if((v[i-1]==false && v[i]==true))
                            countTransitions++;
                    
                    if(     countTransitions == 1
                        &&  (       (isFirstPass  && (v[0] | v[2] | v[4]) && (v[2] | v[4] | v[6]))
                                ||  (isSecondPass && (v[0] | v[2] | v[6]) && (v[0] | v[4] | v[6]))
                            )
                    )
                        classificator[x][y] = ON;
                }
            }
        
        for(int y = 0; y < height; y++)
            for(int x = 0; x < width; x++) {
                if(classificator[x][y] == ON)
                    bImage[x][y] = OFF;
            }
    }

}
