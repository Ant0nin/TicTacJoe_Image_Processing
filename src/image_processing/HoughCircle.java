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
public class HoughCircle extends AbstractImageProcess implements IHough {

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

        BufferedImage acc = new HoughCircleAccumulator().process(input);
        int[] maxima = findMax(acc, width, height, circlesQuantity);
        BufferedImage output = plotCircles(input, maxima);

        return output;
    }

    private BufferedImage plotCircles(BufferedImage input, int[] results) {

        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage output = new BufferedImage(width, height, input.getType());

        for (int i = circlesQuantity - 1; i >= 0; i--) {

            int pix = results[i * 3];
            int xCenter = results[i * 3 + 1];
            int yCenter = results[i * 3 + 2];

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
        if (x < image.getWidth() && x > 0 && y < image.getHeight() && y > 0) {
            image.setRGB(x, y, 0xff000000 | (value << 16 | value << 8 | value));
        }
    }
}
