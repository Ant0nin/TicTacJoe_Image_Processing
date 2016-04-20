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
public class HoughCircle extends AbstractImageProcess {

    final private int circlesQuantity;
    final private int rStart;
    final private int rEnd;

    public HoughCircle(int circlesQuantity, int rStart, int rEnd) {
        super();
        this.circlesQuantity = circlesQuantity;
        this.rStart = rStart;
        this.rEnd = rEnd;
    }

    @Override
    public BufferedImage process(BufferedImage input) {
        int width = input.getWidth();
        int height = input.getHeight();

        BufferedImage acc = generateAcc(input);        
        int[] results = findMax(acc, width, height);
        BufferedImage output = plotCircles(input, results);

        return output;
    }

    private BufferedImage generateAcc(BufferedImage input) {

        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage acc = new BufferedImage(width, height, input.getType());

        int x0, y0;
        double t;
        int r;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if ((input.getRGB(x, y) & 0xff) == 255) {

                    for (int theta = 0; theta < 360; theta++) {

                        r = (int) (x * Math.cos(((theta) * Math.PI) / 180) + y * Math.sin(((theta) * Math.PI) / 180));
                        t = (theta * Math.PI) / 180;
                        x0 = (int) Math.round(x - r * Math.cos(t));
                        y0 = (int) Math.round(y - r * Math.sin(t));
                        
                        if (x0 < width && x0 > 0 && y0 < height && y0 > 0) {
                            int currentARGB = acc.getRGB(x0, y0);
                            acc.setRGB(x0, y0, currentARGB + 1);
                        }
                    }
                }
            }
        }

        int max = 0;
        for (r = 0; r < width; r++) {
            for (int theta = 0; theta < height; theta++) {
                int valueB = acc.getRGB(r, theta) & 0xff;
                if (valueB > max) {
                    max = valueB;
                }
            }
        }

        for (r = 0; r < width; r++) {
            for (int theta = 0; theta < height; theta++) {
                int value = (int) (((double) (acc.getRGB(r, theta) & 0xff) / (double) max) * 255.0);
                acc.setRGB(r, theta, value); // canal BLUE
            }
        }

        return acc;
    }

    // TODO : Méthode commune à isoler
    private int[] findMax(BufferedImage acc, int width, int height) {

        int[] results = new int[circlesQuantity * 3];

        for (int r = 0; r < width; r++) {
            for (int theta = 0; theta < height; theta++) {
                int value = (acc.getRGB(r, theta) & 0xff);

                if (value > results[(circlesQuantity - 1) * 3]) {

                    results[(circlesQuantity - 1) * 3] = value;
                    results[(circlesQuantity - 1) * 3 + 1] = r;
                    results[(circlesQuantity - 1) * 3 + 2] = theta;

                    int i = (circlesQuantity - 2) * 3;
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

    private BufferedImage plotCircles(BufferedImage input, int[] results) {

        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage output = new BufferedImage(width, height, input.getType());

        for (int i = circlesQuantity - 1; i >= 0; i--) {
            
            int pix = results[i*3];
            int xCenter = results[i*3+1];
            int yCenter = results[i*3+2];
            
            for (int r = rStart; r < rEnd; r++) {

                int x, y, r2;
                int radius = r;
                r2 = r * r;
                setPixel(output, pix, xCenter, yCenter + radius);
                setPixel(output, pix, xCenter, yCenter - radius);
                setPixel(output, pix, xCenter + radius, yCenter);
                setPixel(output, pix, xCenter - radius, yCenter);

                //y = radius;
                x = 1;
                y = (int) (Math.sqrt(r2 - 1) + 0.5);
                while (x < y) {
                    setPixel(output, pix, xCenter + x, yCenter + y);
                    setPixel(output, pix, xCenter + x, yCenter - y);
                    setPixel(output, pix, xCenter - x, yCenter + y);
                    setPixel(output, pix, xCenter - x, yCenter - y);
                    setPixel(output, pix, xCenter + y, yCenter + x);
                    setPixel(output, pix, xCenter + y, yCenter - x);
                    setPixel(output, pix, xCenter - y, yCenter + x);
                    setPixel(output, pix, xCenter - y, yCenter - x);
                    x += 1;
                    y = (int) (Math.sqrt(r2 - x * x) + 0.5);
                }
                if (x == y) {
                    setPixel(output, pix, xCenter + x, yCenter + y);
                    setPixel(output, pix, xCenter + x, yCenter - y);
                    setPixel(output, pix, xCenter - x, yCenter + y);
                    setPixel(output, pix, xCenter - x, yCenter - y);
                }
            }
        }
        
        return output;
    }
        
    private void setPixel(BufferedImage image, int value, int x, int y) {
        if(x < image.getWidth() && x > 0 && y < image.getHeight() && y > 0)
            image.setRGB(x, y, 0xff000000 | (value << 16 | value << 8 | value));
    }
  }
