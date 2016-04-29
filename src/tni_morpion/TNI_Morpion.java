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
package tni_morpion;

import game_logic.GamestateEvaluator;
import image_analysis.CellEvaluator;
import image_analysis.ColorHistogramEvaluator;
import image_analysis.GridEvaluator;
import image_processing.AccumulatorMask;
import image_processing.CustomFilter;
import image_processing.Dilation;
import image_processing.DominantColorEliminator;
import image_processing.HoughCircle;
import image_processing.HoughLine;
import image_processing.ImageProcessPipeline;
import image_processing.Scaling;
import image_processing.Thresholding;
import import_export.ImageFilesManager;
import import_export.XmlParametersImporter;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import structures.PlayerEnum;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public class TNI_Morpion {

    final static String INPUT_FOLDER_NAME = System.getProperty("user.dir") + "\\res\\img\\input";
    final static String OUTPUT_FOLDER_NAME = System.getProperty("user.dir") + "\\output";
    final static String CONFIG_FOLDER_NAME = System.getProperty("user.dir") + "\\res\\config";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String IMAGE_FILENAME = null;
        System.out.println("Veuillez entrer le nom de l'image à traiter");
        try {
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            IMAGE_FILENAME = bufferRead.readLine();
        } 
        catch (IOException e) {
            System.err.println("Nom de fichier image incorrect");
        }

        XmlParametersImporter configImporter = new XmlParametersImporter(CONFIG_FOLDER_NAME, false);
        Map<String, Integer> configParams = configImporter.importParameter(IMAGE_FILENAME);
        ImageFilesManager filesManager = new ImageFilesManager(INPUT_FOLDER_NAME, OUTPUT_FOLDER_NAME);
                
        final int PARAM_SCALING_WIDTH = configParams.get("scalingWidth");
        final int PARAM_SCALING_HEIGHT = configParams.get("scalingHeight");
        final int PARAM_THRESHOLD_VALUE = configParams.get("thresholdValue");
        final int PARAM_HOUGH_LINES_QUANTITY = configParams.get("houghLinesQuantity");
        final int PARAM_HOUGH_CIRCLES_QUANTITY = configParams.get("houghCirclesQuantity");
        final int PARAM_HOUGH_RADIUS_START = configParams.get("houghCirclesRadiusStart");
        final int PARAM_HOUGH_RADIUS_END = configParams.get("houghCirclesRadiusEnd");
        final int PARAM_ABSCENCE_PERCENTAGE = configParams.get("absencePercentage");
        final int PARAM_CIRCLE_DETECT_PERCENTAGE = configParams.get("circleDetectPercentage");
        final int PARAM_SPACE_TOLERANCE = configParams.get("spaceTolerance");
        final int PARAM_NUMBER_ALIGN_TO_WIN = configParams.get("numberAlignToWin");
        final int PARAM_DOMINANT_COLOR_PERCENTAGE = configParams.get("dominantColorPercentage");
        final int PARAM_GRID_MARGIN_OFFSET = configParams.get("gridMarginOffset");
        
        BufferedImage img_input = filesManager.importImage(IMAGE_FILENAME);

        float[] colorHistogram = new ColorHistogramEvaluator().evaluate(img_input);
        DominantColorEliminator dominantColorElimination = new DominantColorEliminator(colorHistogram, PARAM_DOMINANT_COLOR_PERCENTAGE);
        Dilation dilation = new Dilation();
        CustomFilter blurFilter = new CustomFilter("soften_high");
        Thresholding simpleThresholding = new Thresholding(PARAM_THRESHOLD_VALUE);
        Scaling scaling = new Scaling(PARAM_SCALING_WIDTH, PARAM_SCALING_HEIGHT);
        AccumulatorMask maskDiagonals = new AccumulatorMask(new int[][]{{40, 50},{130, 140}});
        HoughLine houghLine = new HoughLine(PARAM_HOUGH_LINES_QUANTITY, maskDiagonals);
        HoughCircle houghCircle = new HoughCircle(PARAM_HOUGH_CIRCLES_QUANTITY, PARAM_HOUGH_RADIUS_START, PARAM_HOUGH_RADIUS_END);
        CellEvaluator cellEvaluator = new CellEvaluator(PARAM_ABSCENCE_PERCENTAGE, PARAM_CIRCLE_DETECT_PERCENTAGE);
        GridEvaluator gridEvaluator = new GridEvaluator(PARAM_SPACE_TOLERANCE, PARAM_GRID_MARGIN_OFFSET, cellEvaluator);
        GamestateEvaluator gameEvaluator = new GamestateEvaluator(PARAM_NUMBER_ALIGN_TO_WIN);
        
        ImageProcessPipeline pl_prefiltering = new ImageProcessPipeline(
                blurFilter,
                dominantColorElimination,
                dilation,
                scaling
        );
        ImageProcessPipeline pl_gridDetection = new ImageProcessPipeline(
                houghLine,
                simpleThresholding
        );
        ImageProcessPipeline pl_circleDetection = new ImageProcessPipeline(
                houghCircle,
                simpleThresholding
        );

        System.out.println();
        System.out.println("Prétraitement : Elimination du bruit, seuillage en fonction de la couleur diminante et amélioration des formes...");
        BufferedImage img_prefiltered = pl_prefiltering.process(img_input);    
        System.out.println("Détection de la grille : Tracé de "+PARAM_HOUGH_LINES_QUANTITY+" lignes de Hough...");
        BufferedImage img_grid = pl_gridDetection.process(img_prefiltered);
        System.out.println("Détection des cercles : Tracé de "+PARAM_HOUGH_CIRCLES_QUANTITY+" cercles de Hough...");
        BufferedImage img_circles = pl_circleDetection.process(img_prefiltered);

        System.out.println("Export des images intermédiaires générées dans le répertoire de sortie 'output'");
        ImageProcessPipeline.exportPipelineImages(filesManager, IMAGE_FILENAME);

        System.out.println("Evaluation de l'état de la partie...");
        System.out.println();
        PlayerEnum[][] gamestate = gridEvaluator.evaluate(img_prefiltered, img_grid, img_circles);
        PlayerEnum winner = gameEvaluator.determineWinner(gamestate);
        gameEvaluator.displayGamestate(gamestate, winner);

    }

}
