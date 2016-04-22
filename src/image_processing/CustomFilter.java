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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public class CustomFilter extends AbstractImageProcess {

    private static final String FILTERS_FOLDER = System.getProperty("user.dir") + "\\res\\filters\\";
    private final Sampler filter;

    private class Sampler {

        protected int w;
        protected int h;
        protected int[][] data;

        Sampler(int w, int h, int[][] data) {
            this.w = w;
            this.h = h;
            this.data = data;
        }
    }

    public CustomFilter(String filterName) {
        this.filter = openSamplerFromFile(filterName);
    }

    @Override
    public BufferedImage process(BufferedImage input) {

        final int halfW = (filter.w - 1) >> 1;
        final int halfH = (filter.h - 1) >> 1;
        int divisionFactor = 0;

        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());

        for (int x = 0; x < filter.w; x++) {
            for (int y = 0; y < filter.h; y++) {
                divisionFactor += filter.data[x][y];
            }
        }

        final int imgW = input.getWidth();
        final int imgH = input.getHeight();
        int[] total = new int[3]; // rgb
        
        for (int y = halfH; y < (imgH - halfH); y++) {
            for (int x = halfW; x < (imgW - halfW); x++) {
                
                total[0] = 0;
                total[1] = 0;
                total[2] = 0;

                for (int j = -halfH; j <= halfH; j++) {
                    for (int i = -halfW; i <= halfW; i++) {
                        int argb = input.getRGB(x + i, y + j);
                        int filterCoef = filter.data[i + halfW][j + halfH];
                        
                        total[0] += ((argb & 0x00ff0000) >> 16) * filterCoef;   // R
                        total[1] += ((argb & 0x0000ff00) >> 8) * filterCoef;    // G
                        total[2] += ((argb & 0x000000ff)) * filterCoef;         // B
                    }
                }
                total[0] /= divisionFactor;
                total[1] /= divisionFactor;
                total[2] /= divisionFactor;
                output.setRGB(x, y, (0xff000000 | (total[0] << 16) | (total[1] << 8) | (total[2])));
            }
        }

        return output;
    }

    private Sampler openSamplerFromFile(String filterName) {

        Path path = Paths.get(FILTERS_FOLDER + filterName + ".filter");

        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            int width = Integer.parseInt(lines.get(0));
            int height = Integer.parseInt(lines.get(1));
            int[][] data = new int[width][height];
            for (int i = 0; i < height; i++) {
                String line = lines.get(i + 2);
                String[] digits = line.split(" ");
                for (int j = 0; j < width; j++) {
                    data[j][i] = Integer.parseInt(digits[j]);
                }
            }
            return new Sampler(width, height, data);

        } catch (IOException ex) {
            System.err.println("Erreur lors de l'ouverture du filtre " + path.toString());
        }
        return null;
    }

}
