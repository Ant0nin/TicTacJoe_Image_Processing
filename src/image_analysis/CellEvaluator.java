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

import image_processing.HoughCircleAccumulator;
import image_processing.HoughLineAccumulator;
import java.awt.image.BufferedImage;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public class CellEvaluator {
    
    public final static Boolean NOTHING = null;
    public final static Boolean CROSS = false;
    public final static Boolean CIRCLE = true;
    
    private final HoughLineAccumulator linesAccGenerator;
    private final HoughCircleAccumulator circlesAccGenerator;
    int abscencePercentage;
    
    public CellEvaluator(HoughLineAccumulator linesAcc, HoughCircleAccumulator circlesAcc, int abscencePercentage) {
        this.linesAccGenerator = linesAcc;
        this.circlesAccGenerator = circlesAcc;
        
        if(abscencePercentage < 0 || abscencePercentage > 100)
            System.err.println("Erreur : Le pourcentage d'absence doit être compris entre 0 et 100");
        else
            this.abscencePercentage = abscencePercentage;
    }

    public Boolean determineCell(BufferedImage cell) {
        
        BWHistogramEvaluator evaluator = new BWHistogramEvaluator();
        long histogram[] = evaluator.evaluate(cell);
        int countTotalPixels = cell.getWidth() * cell.getHeight();
        int limit = (int)((float)abscencePercentage/100.0*(float)countTotalPixels);
        if(histogram[0] > limit || histogram[1] < limit )
            return NOTHING;
        
        BufferedImage linesAcc = linesAccGenerator.process(cell);
        BufferedImage circlesAcc = circlesAccGenerator.process(cell);
        
        // TODO : comparer les accumulateurs;
        return null;
    }
    
}
