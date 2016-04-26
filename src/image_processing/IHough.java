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
public interface IHough {
        
    default int[] findMax(BufferedImage acc, int width, int height, int accSize) {

        int[] results = new int[accSize * 3];

        for (int r = 0; r < width; r++) {
            for (int theta = 0; theta < height; theta++) {
                int value = (acc.getRGB(r, theta) & 0x00ffffff);

                if (value > results[(accSize - 1) * 3]) {

                    results[(accSize - 1) * 3] = value;
                    results[(accSize - 1) * 3 + 1] = r;
                    results[(accSize - 1) * 3 + 2] = theta;

                    int i = (accSize - 2) * 3;
                    while ((i >= 0) && (results[i + 3] > results[i])) {
                        for (int j = 0; j < 3; j++) {
                            int temp = results[i + j];
                            results[i + j] = results[i + 3 + j];
                            results[i + 3 + j] = temp;
                        }
                        i = i - 3;
                        if (i < 0) {
                            break;
                        }
                    }
                }
            }
        }
        return results;
    }    
}
