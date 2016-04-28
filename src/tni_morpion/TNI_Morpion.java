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
import image_processing.DominantColorThresholding;
import image_processing.Erosion;
import image_processing.HoughCircle;
import image_processing.HoughCircleAccumulator;
import image_processing.HoughLine;
import image_processing.HoughLineAccumulator;
import image_processing.ImageProcessPipeline;
import image_processing.NegativeFilter;
import image_processing.Scaling;
import image_processing.Skeletonization;
import image_processing.Thresholding;
import import_export.ImageFilesManager;
import java.awt.image.BufferedImage;
import structures.PlayerEnum;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public class TNI_Morpion {

    final static String INPUT_FOLDER_NAME = System.getProperty("user.dir") + "\\res\\img\\" + "input";
    final static String OUTPUT_FOLDER_NAME = System.getProperty("user.dir") + "\\res\\img\\" + "output";
    final static String IMAGE_FILENAME = "morpion002.png";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        
        
        ImageFilesManager filesManager = new ImageFilesManager(INPUT_FOLDER_NAME, OUTPUT_FOLDER_NAME);
        BufferedImage img_input = filesManager.importImage(IMAGE_FILENAME);

        // Processes
        AccumulatorMask linesAccMask = new AccumulatorMask(
                new int[][]{
                    {40, 50},
                    {130, 140}}
        );
        CustomFilter blurFilter = new CustomFilter("softer");
        CustomFilter sharpenFilter = new CustomFilter("sharpen_medium");
        CustomFilter softenVeryHigh = new CustomFilter("soften_very_high");
        long[] colorHistogram = new ColorHistogramEvaluator().evaluate(img_input);
        DominantColorThresholding deleteDominantColor = new DominantColorThresholding(colorHistogram, 1);
        Dilation simpleDilation = new Dilation();
        Skeletonization skeletonizationProcess = new Skeletonization();
        HoughLineAccumulator linesAcc = new HoughLineAccumulator((int) Math.sqrt(img_input.getWidth() * img_input.getWidth() + img_input.getHeight() * img_input.getHeight()));
        HoughCircleAccumulator circleAcc = new HoughCircleAccumulator(40, 70);
        NegativeFilter inversion = new NegativeFilter();
        Erosion erosion = new Erosion();
        
        Thresholding simpleThresholding = new Thresholding(0xffaaaaaa);
        Scaling scaling = new Scaling(300, 300);
        HoughLine houghLine = new HoughLine(4000);
        HoughCircle houghCircle = new HoughCircle(250, 15, 25);
        CellEvaluator cellEvaluator = new CellEvaluator(98, 5);
        GridEvaluator gridEvaluator = new GridEvaluator(10, cellEvaluator);
        GamestateEvaluator gameEvaluator = new GamestateEvaluator(3);

        /*NoiseEvaluator noiseEv = new NoiseEvaluator();
        long noiseQty = noiseEv.evaluate(image);
        int blurIt = (int)(noiseQty / 1000000);
        for(int i = 0; i < blurIt; i++)
            allProcesses.add(blurFilter);*/
        
        ImageProcessPipeline pl_prefiltering = new ImageProcessPipeline(
                scaling,
                inversion,
                simpleThresholding
        );
        ImageProcessPipeline pl_gridDetection = new ImageProcessPipeline(
                houghLine,
                simpleThresholding
        );
        ImageProcessPipeline pl_circleDetection = new ImageProcessPipeline(
                houghCircle,
                simpleThresholding
        );        
        
        BufferedImage img_prefiltered = pl_prefiltering.process(img_input);
        BufferedImage img_grid = pl_gridDetection.process(img_prefiltered);
        BufferedImage img_circles = pl_circleDetection.process(img_prefiltered);

        ImageProcessPipeline.exportPipelineImages(filesManager, IMAGE_FILENAME);
        
        PlayerEnum[][] gamestate = gridEvaluator.evaluate(img_prefiltered, img_grid, img_circles);
        PlayerEnum winner = gameEvaluator.determineWinner(gamestate);
        gameEvaluator.displayGamestate(gamestate, winner);
        
    }

}
