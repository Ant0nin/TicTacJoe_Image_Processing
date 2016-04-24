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
public class HoughLineAccumulator extends AbstractImageProcess {

    private final int rmax;
    
    HoughLineAccumulator(int rmax) {
        this.rmax = rmax;
    }
    
    @Override
    public BufferedImage process(BufferedImage input) {
        
        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage acc = new BufferedImage(rmax, 180, input.getType());
        int r;
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if ((input.getRGB(x, y) & 0xff) == 255) {
                    for (int theta = 0; theta < 180; theta++) {

                        r = (int) (x * Math.cos(((theta) * Math.PI) / 180) + y * Math.sin(((theta) * Math.PI) / 180));

                        if ((r > 0) && (r <= rmax)) {
                            int currentARGB = acc.getRGB(r, theta);
                            acc.setRGB(r, theta, currentARGB + 1); // canal BLUE
                        }
                    }
                }
            }
        }

        int max = 0;
        for (r = 0; r < rmax; r++) {
            for (int theta = 0; theta < 180; theta++) {
                int valueB = acc.getRGB(r, theta) & 0xff;
                if (valueB > max) {
                    max = valueB;
                }
            }
        }

        for (r = 0; r < rmax; r++) {
            for (int theta = 0; theta < 180; theta++) {
                int value = (int) (((double) (acc.getRGB(r, theta) & 0xff) / (double) max) * 255.0);
                acc.setRGB(r, theta, value); // canal BLUE
            }
        }
        
        return acc;
        
    }
    
}
