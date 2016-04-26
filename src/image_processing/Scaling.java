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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public class Scaling extends AbstractImageProcess {

    private final int width;
    private final int height;

    public Scaling(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public BufferedImage process(BufferedImage input) {

        BufferedImage output = new BufferedImage(width, height, input.getType());
        
        Graphics2D bGr = output.createGraphics();
        bGr.drawImage(input.getScaledInstance(width, height, 0), 0, 0, null);
        bGr.dispose();

        return output;
    }

}
