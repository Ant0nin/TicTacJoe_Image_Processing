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
public class HoughLine extends AbstractImageProcess {

    private final int linesQuantity;

    public HoughLine(int linesQuantity) {
        super();
        this.linesQuantity = linesQuantity;
    }
    
    @Override
    public BufferedImage process(BufferedImage input) {

        int width = input.getWidth();
        int height = input.getHeight();
        int rmax = (int) Math.sqrt(width * width + height * height);
        
        BufferedImage acc = generateAcc(input, rmax);
        int[] results = findMax(acc, rmax);
        BufferedImage output = plotLines(input, results);

        return output;
    }
    
    private BufferedImage generateAcc(BufferedImage input, int rmax) {
        
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

    private int[] findMax(BufferedImage acc, int rmax) {

        int[] results = new int[linesQuantity * 3];

        for (int r = 0; r < rmax; r++) {
            for (int theta = 0; theta < 180; theta++) {
                int value = (acc.getRGB(r, theta) & 0xff);

                if (value > results[(linesQuantity - 1) * 3]) {

                    results[(linesQuantity - 1) * 3] = value;
                    results[(linesQuantity - 1) * 3 + 1] = r;
                    results[(linesQuantity - 1) * 3 + 2] = theta;

                    int i = (linesQuantity - 2) * 3;
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
