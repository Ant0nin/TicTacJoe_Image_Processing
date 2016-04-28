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
public class BWHistogramEvaluator {

    private final boolean normalized;
    
    public BWHistogramEvaluator(boolean normalized) {
        this.normalized = normalized;
    }
    
    public float[] evaluate(BufferedImage image) {
        
        float[] histogram = new float[2];
        for(int i=0; i<histogram.length; i++)
            histogram[i] = 0;
        
        for(int y = 0; y < image.getHeight(); y++)
            for(int x = 0; x < image.getWidth(); x++)
            {
                int pixelColor = image.getRGB(x, y);
                
                switch (pixelColor) {
                    case 0xff000000:
                        histogram[0]++;
                        break;
                    case 0xffffffff:
                        histogram[1]++;
                        break;
                    default:
                        System.err.println("Erreur : Il faut appliquer un seuillage avant d'évaluer l'histogramme noir et blanc.");
                        return null;
                }
            }
        
        if(normalized) {
            float sum = histogram[0] + histogram[1];
            histogram[0] = (histogram[0] / sum * 100F);
            histogram[1] = (histogram[1] / sum * 100F);
        }
        
        return histogram;
    }
    
}
