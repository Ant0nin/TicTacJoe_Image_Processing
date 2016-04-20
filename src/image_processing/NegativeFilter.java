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
public class NegativeFilter extends AbstractImageProcess {

    @Override
    public BufferedImage process(BufferedImage input) {
        
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
        
        for(int x = 0; x < output.getWidth(); x++)
            for(int y = 0; y < output.getHeight(); y++) {
                int currentARGB = input.getRGB(x, y);
                output.setRGB(x, y, 0xffffffff - currentARGB);
            }
        
        return output;
    }
    
}
