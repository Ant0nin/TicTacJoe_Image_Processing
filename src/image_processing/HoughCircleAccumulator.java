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
public class HoughCircleAccumulator extends AbstractImageProcess {

    private final int rStart;
    private final int rEnd;
    
    public HoughCircleAccumulator(int rStart, int rEnd) {
        this.rStart = rStart;
        this.rEnd = rEnd;
    }
    
    @Override
    public BufferedImage process(BufferedImage input) {
        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage acc = new BufferedImage(width, height, input.getType());

        int x0, y0;
        double t;
        int[][] tempAcc = new int[width][height];

        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                
                if ((input.getRGB(x, y) & 0xff) == 0xff) {
                    
                    for (int theta = 0; theta < 360; theta++) {
                        for(int r=rStart; r<rEnd; r++) {
                            t = (theta * Math.PI) / 180;
                            x0 = (int) Math.round(x - r * Math.cos(t));
                            y0 = (int) Math.round(y - r * Math.sin(t));

                            if (x0 < width && x0 > 0 && y0 < height && y0 > 0) {
                                tempAcc[x0][y0]++;
                            }
                        }
                    }
                }
            }
        }

        int max = 0;
        for (int r = 0; r < width; r++) {
            for (int theta = 0; theta < height; theta++) {
                int value = tempAcc[r][theta];
                if (value > max) {
                    max = value;
                }
            }
        }

        for (int r = 0; r < width; r++) {
            for (int theta = 0; theta < height; theta++) {
                int value = (int) (((double) (tempAcc[r][theta]) / (double) max) * 255.0);
                acc.setRGB(r, theta, 0xff000000 | (value << 16 | value << 8 | value));
            }
        }

        return acc;
    }

}
