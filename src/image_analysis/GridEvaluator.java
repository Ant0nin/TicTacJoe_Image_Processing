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

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import structures.Point;
import structures.Rectangle;
import structures.StraightLine;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public class GridEvaluator {

    private final int spaceTolerance;
    private int spaceBetween;

    // TODO : retirer ces attributs (redondance)
    private final int BACK = 0xff000000;
    private final int FRONT = 0xffffffff;

    public GridEvaluator(int spaceTolerance) {
        this.spaceTolerance = spaceTolerance;
    }

    public Boolean[][] evaluate(BufferedImage imageGrid) {

        List[] borders = detectBorders(imageGrid);
        Point[][] cellsPos = detectCellsPositions(borders, imageGrid);
        Rectangle[][] boundingBoxes = calcBoundingBoxes(cellsPos);

        // TODO
        return null;
    }

    private Rectangle[][] calcBoundingBoxes(Point[][] cellsPos) {
        
        // TODO
        return null;
    }
    
    private Point[][] detectCellsPositions(List[] borders, BufferedImage imageGrid) {
        
        int k = imageGrid.getWidth()-1;
        int l = imageGrid.getHeight()-1;

        List<Integer> top = borders[0];
        List<Integer> bottom = borders[1];
        List<Integer> left = borders[2];
        List<Integer> right = borders[3];
        
        int gridSizeX = top.size() - 1;
        int gridSizeY = left.size() - 1;

        Point[][] cellsPos = new Point[gridSizeX][gridSizeY];

        StraightLine verticalLine, horizontalLine;
        for (int j = 0; j < gridSizeY; j++) {
            horizontalLine = new StraightLine(
                    new Point(0, left.get(j)),
                    new Point(k, right.get(j))
            );
            for(int i = 0; i < gridSizeX; i++) {
                verticalLine = new StraightLine(
                    new Point(top.get(i), 0),
                    new Point(bottom.get(i), l)
                );
                Point intersect = StraightLine.calcIntersect(verticalLine, horizontalLine);
                cellsPos[j][i] = intersect;
            }
        }

        return cellsPos;
    }

    private List[] detectBorders(BufferedImage imageGrid) {

        int width = imageGrid.getWidth();
        int height = imageGrid.getHeight();

        List<Integer> top = new ArrayList<>();
        List<Integer> bottom = new ArrayList<>();
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        List[] borders = {top, bottom, left, right};

        spaceBetween = 0;
        for (int x = 0; x < width; x++) {
            int argb = imageGrid.getRGB(x, 0);
            if (detectEndpoint(argb)) {
                top.add(x);
            }
        }

        spaceBetween = 0;
        final int k = height - 1;
        for (int x = 0; x < width; x++) {
            int argb = imageGrid.getRGB(x, k);
            if (detectEndpoint(argb)) {
                bottom.add(x);
            }
        }

        spaceBetween = 0;
        for (int y = 0; y < height; y++) {
            int argb = imageGrid.getRGB(0, y);
            if (detectEndpoint(argb)) {
                left.add(y);
            }
        }

        spaceBetween = 0;
        final int l = width - 1;
        for (int y = 0; y < height; y++) {
            int argb = imageGrid.getRGB(l, y);
            if (detectEndpoint(argb)) {
                right.add(y);
            }
        }

        if ((top.size() != bottom.size()) || (left.size() != right.size())) {
            System.err.println("Erreur pendant la construction de la grille");
            return null;
        }

        if (top.size() == 2) {
            top.add(0);
            top.add(l);
            bottom.add(0);
            bottom.add(l);
        }
        if (left.size() == 2) {
            left.add(0);
            left.add(k);
            right.add(0);
            right.add(k);
        }

        return borders;
    }

    private Boolean detectEndpoint(int argb) {

        switch (argb) {

            case FRONT:
                if (spaceBetween > spaceTolerance) {
                    spaceBetween = 0;
                    return true;
                } else {
                    return false;
                }

            case BACK:
                spaceBetween++;
                return false;

            default:
                System.err.println("Erreur : Pour construire la grille il faut partir d'une image en noir et blanc");
                return null;
        }
    }

}
