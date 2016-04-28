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
public class CellEvaluator {
    
    public final static Boolean NOTHING = null;
    public final static Boolean CROSS = false;
    public final static Boolean CIRCLE = true;
    
    private int abscencePercentage;
    private int circleDetectPercentage;
    
    public CellEvaluator(int abscencePercentage, int circleDetectPercentage) {
        
        if(abscencePercentage < 0 || abscencePercentage > 100) {
            System.err.println("Erreur : Le pourcentage d'absence de forme doit être compris entre 0 et 100");
        }
        else
            this.abscencePercentage = abscencePercentage;
        
        if(circleDetectPercentage < 0 || circleDetectPercentage > 100) {
            System.err.println("Erreur : Le pourcentage de détection de cercle doit être compris entre 0 et 100");
        }
        else
            this.circleDetectPercentage = circleDetectPercentage;
    }

    public Boolean determineCell(BufferedImage cellPrefiltered, BufferedImage cellCircle) {
                
        BWHistogramEvaluator colorEvaluator = new BWHistogramEvaluator(true);
        float histogram[];
        
        histogram = colorEvaluator.evaluate(cellPrefiltered);
        if(histogram[0] > (float)abscencePercentage)
            return NOTHING;
        
        histogram = colorEvaluator.evaluate(cellCircle);
        if(histogram[1] > (float)circleDetectPercentage)
            return CIRCLE;
        else
            return CROSS;
    }
    
}
