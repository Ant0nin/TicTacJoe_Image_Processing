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
import structures.Box;
import structures.PlayerEnum;
import structures.Point;
import structures.StraightLine;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public class GridEvaluator {

    private final int spaceTolerance;
    private final CellEvaluator cellEvaluator;
    private final int marginOffset;

    private int spaceBetween; // TODO : retirer

    // TODO : retirer ces attributs (redondance)
    private final int BACK = 0xff000000;
    private final int FRONT = 0xffffffff;

    public GridEvaluator(int spaceTolerance, int marginOffset, CellEvaluator cellEvaluator) {
        this.spaceTolerance = spaceTolerance;
        this.cellEvaluator = cellEvaluator;
        this.marginOffset = marginOffset;
    }

    public PlayerEnum[][] evaluate(BufferedImage prefilteredImage, BufferedImage imageGrid, BufferedImage imageCircles) {
        List<Integer>[] borders = detectBorders(imageGrid);
        int sizeX = borders[0].size() - 1;
        int sizeY = borders[2].size() - 1;
        List<StraightLine>[] lines = new List[2];

        Point[][] intersections = new Point[sizeX + 1][sizeY + 1];
        BufferedImage[][] cellsPrefiltered = new BufferedImage[sizeX][sizeY];
        BufferedImage[][] cellsCircles = new BufferedImage[sizeX][sizeY];
        Box[][] boundingBoxes = new Box[sizeX][sizeY];

        detectLinesIntersections(imageGrid, borders, lines, intersections);
        calcBoundingBoxes(intersections, sizeX, sizeY, boundingBoxes);
        constructCells(boundingBoxes, sizeX, sizeY, prefilteredImage, imageGrid, imageCircles, cellsPrefiltered, cellsCircles);
        PlayerEnum[][] gamestate = detectGameState(cellsPrefiltered, cellsCircles, sizeX, sizeY);
        return gamestate;
    }

    private PlayerEnum[][] detectGameState(BufferedImage[][] cellsPrefiltered, BufferedImage[][] cellsCircles, int sizeX, int sizeY) {

        PlayerEnum[][] gamestate = new PlayerEnum[sizeX][sizeY];
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                gamestate[x][y] = cellEvaluator.determineCell(cellsPrefiltered[x][y], cellsCircles[x][y]);
            }
        }
        return gamestate;
    }

    private void constructCells(Box[][] boundingBoxes, int sizeX, int sizeY,
            BufferedImage prefilteredImage, BufferedImage imageGrid, BufferedImage imageCircles,
            BufferedImage[][] prefilteredCells, BufferedImage[][] circleCells) {
        
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                Box box = boundingBoxes[x][y];

                int width = box.xMax - box.xMin;
                int height = box.yMax - box.yMin;
                
                prefilteredCells[x][y] = new BufferedImage(width, height, prefilteredImage.getType());
                circleCells[x][y] = new BufferedImage(width, height, prefilteredImage.getType());

                for (int j = box.yMin; j < box.yMax; j++) {
                    for (int i = box.xMin; i < box.xMax; i++) {
                        
                        int k = i - box.xMin;
                        int l = j - box.yMin;

                        int color, mask;
                        mask = (imageGrid.getRGB(i, j) == FRONT ? 0xff000000 : 0xffffffff);

                        color = prefilteredImage.getRGB(i, j) & mask;
                        prefilteredCells[x][y].setRGB(k, l, color);

                        color = imageCircles.getRGB(i, j) & mask;
                        circleCells[x][y].setRGB(k, l, color);
                    }
                }
            }
        }
    }

    private void calcBoundingBoxes(Point[][] intersections, int sizeX, int sizeY, Box[][] boxes) {

        Point[] pts = new Point[4];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                pts[0] = intersections[x][y];
                pts[1] = intersections[x + 1][y];
                pts[2] = intersections[x][y + 1];
                pts[3] = intersections[x + 1][y + 1];
                
                int xMin, xMax, yMin, yMax;
                xMin = xMax = yMin = yMax = 0;
                boolean init = false;
                
                for (Point pt : pts) {
                    int coordX = pt.x;
                    int coordY = pt.y;

                    if (!init) {
                        xMin = coordX;
                        xMax = coordX;
                        yMin = coordY;
                        yMax = coordY;
                        init = true;
                    } else {
                        if (coordX > xMax) {
                            xMax = coordX;
                        }
                        if (coordX < xMin) {
                            xMin = coordX;
                        }
                        if (coordY > yMax) {
                            yMax = coordY;
                        }
                        if (coordY < yMin) {
                            yMin = coordY;
                        }
                    }

                }
                boxes[x][y] = new Box(xMin, xMax, yMin, yMax);
            }
        }
    }

    private void detectLinesIntersections(BufferedImage imageGrid, List<Integer>[] borders, List<StraightLine>[] lines, Point[][] intersections) {
        
        int k = imageGrid.getWidth() - 1;
        int l = imageGrid.getHeight() - 1;

        List<Integer> top = borders[0];
        List<Integer> bottom = borders[1];
        List<Integer> left = borders[2];
        List<Integer> right = borders[3];

        int gridSizeX = top.size();
        int gridSizeY = left.size();

        lines[0] = new ArrayList<>();
        lines[1] = new ArrayList<>();

        StraightLine verticalLine, horizontalLine;
        for (int j = 0; j < gridSizeY; j++) {
            horizontalLine = new StraightLine(
                    new Point(0, left.get(j)),
                    new Point(k, right.get(j))
            );
            lines[0].add(horizontalLine);
            for (int i = 0; i < gridSizeX; i++) {
                verticalLine = new StraightLine(
                        new Point(top.get(i), 0),
                        new Point(bottom.get(i), l)
                );
                lines[1].add(verticalLine);
                Point intersect = StraightLine.calcIntersect(verticalLine, horizontalLine);
                
                if(intersect.x < 0)
                    intersect.x = 0;
                if(intersect.x > k)
                    intersect.x = k;
                if(intersect.y < 0)
                    intersect.y = 0;
                if(intersect.y > l)
                    intersect.y = l;
                    
                intersections[j][i] = intersect;
            }
        }
    }

    private List[] detectBorders(BufferedImage imageGrid) {

        int width = imageGrid.getWidth();
        int height = imageGrid.getHeight();

        List<Integer> top = new ArrayList<>();
        List<Integer> bottom = new ArrayList<>();
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        List[] borders = {top, bottom, left, right};
        
        final int k = height - 1 - marginOffset;
        final int l = width - 1 - marginOffset;
        final int m = height - marginOffset;
        final int n = width - marginOffset;


        spaceBetween = 0;
        for (int x = marginOffset; x < n; x++) {
            int argb = imageGrid.getRGB(x, marginOffset);
            if (detectEndpoint(argb)) {
                top.add(x);
            }
        }

        spaceBetween = 0;
        for (int x = marginOffset; x < n; x++) {
            int argb = imageGrid.getRGB(x, k);
            if (detectEndpoint(argb)) {
                bottom.add(x);
            }
        }

        spaceBetween = 0;
        for (int y = marginOffset; y < m; y++) {
            int argb = imageGrid.getRGB(marginOffset, y);
            if (detectEndpoint(argb)) {
                left.add(y);
            }
        }

        spaceBetween = 0;
        for (int y = marginOffset; y < m; y++) {
            int argb = imageGrid.getRGB(l, y);
            if (detectEndpoint(argb)) {
                right.add(y);
            }
        }

        if ((top.size() != bottom.size()) || (left.size() != right.size())) {
            System.err.println("Erreur pendant la détection de la grille (parcours des bords)");
            return null;
        }

        if (top.size() == 2) {
            top.add(0, 0);
            top.add(l);
            bottom.add(0, 0);
            bottom.add(l);
        }
        if (left.size() == 2) {
            left.add(0, 0);
            left.add(k);
            right.add(0, 0);
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
