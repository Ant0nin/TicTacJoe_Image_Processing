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

import image_analysis.ColorHistogramEvaluator;
import image_processing.AbstractImageProcess;
import image_processing.AccumulatorMask;
import image_processing.CustomFilter;
import image_processing.Dilation;
import image_processing.DominantColorThresholding;
import image_processing.HoughCircle;
import image_processing.HoughCircleAccumulator;
import image_processing.HoughLine;
import image_processing.HoughLineAccumulator;
import image_processing.ImageProcessPipeline;
import image_processing.NegativeFilter;
import image_processing.Skeletonization;
import image_processing.Thresholding;
import import_export.ImageFilesManager;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public class TNI_Morpion {

    final static String INPUT_FOLDER_NAME = System.getProperty("user.dir") + "\\res\\img\\" + "input";
    final static String OUTPUT_FOLDER_NAME = System.getProperty("user.dir") + "\\res\\img\\" + "output";
    final static String IMAGE_FILENAME = "cross001.png";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ImageFilesManager filesManager = new ImageFilesManager(INPUT_FOLDER_NAME, OUTPUT_FOLDER_NAME);
        BufferedImage image = filesManager.importImage(IMAGE_FILENAME);

        // Processes
        AccumulatorMask linesAccMask = new AccumulatorMask(
            new int[][]{ 
                {40,50}, 
                {130,140}}
        );
        List<AbstractImageProcess> allProcesses = new ArrayList<>();
        CustomFilter blurFilter = new CustomFilter("softer");
        CustomFilter sharpenFilter = new CustomFilter("sharpen_medium");
        CustomFilter softenVeryHigh = new CustomFilter("soften_very_high");
        long[] colorHistogram = new ColorHistogramEvaluator().evaluate(image);
        DominantColorThresholding deleteDominantColor = new DominantColorThresholding(colorHistogram, 20);
        Dilation simpleDilation = new Dilation();
        Skeletonization skeletonizationProcess = new Skeletonization();
        HoughLineAccumulator linesAcc = new HoughLineAccumulator((int) Math.sqrt(image.getWidth() * image.getWidth() + image.getHeight() * image.getHeight()));
        HoughLine houghLine = new HoughLine(100, linesAccMask);
        HoughCircleAccumulator circleAcc = new HoughCircleAccumulator(100, 200);
        HoughCircle houghCircle = new HoughCircle(1, 180, 190);
        Thresholding simpleThresholding = new Thresholding(0xffaaaaaa);
        NegativeFilter inversion = new NegativeFilter();

        /*NoiseEvaluator noiseEv = new NoiseEvaluator();
        long noiseQty = noiseEv.evaluate(image);
        int blurIt = (int)(noiseQty / 1000000);
        for(int i = 0; i < blurIt; i++)
            allProcesses.add(blurFilter);*/

        allProcesses.add(simpleThresholding);
        allProcesses.add(inversion);
        allProcesses.add(houghLine);

        ImageProcessPipeline pipeline = new ImageProcessPipeline(allProcesses);

        pipeline.process(image);
        pipeline.exportPipelineImages(filesManager, IMAGE_FILENAME);
    }

}
