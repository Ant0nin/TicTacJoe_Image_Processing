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

import image_processing.CustomFilter;
import image_processing.HoughCircle;
import image_processing.NegativeFilter;
import image_processing.ImageProcessPipeline;
import image_processing.Skeletonization;
import image_processing.Thresholding;
import import_export.ImageFilesManager;
import java.awt.image.BufferedImage;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public class TNI_Morpion {

    final static String INPUT_FOLDER_NAME = System.getProperty("user.dir") + "\\res\\img\\" + "input";
    final static String OUTPUT_FOLDER_NAME = System.getProperty("user.dir") + "\\res\\img\\" + "output";
    final static String IMAGE_FILENAME = "morpion003.png";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ImageFilesManager filesManager = new ImageFilesManager(INPUT_FOLDER_NAME, OUTPUT_FOLDER_NAME);
        BufferedImage inputImage = filesManager.importImage(IMAGE_FILENAME);

        ImageProcessPipeline pipeline = new ImageProcessPipeline(
                new CustomFilter("softer"),
                new Thresholding(0xffeeeeee),
                new NegativeFilter(),
                new Skeletonization(),
                new Skeletonization(),
                new Skeletonization(),
                new Skeletonization(),
                new Skeletonization()
        );

        pipeline.process(inputImage);
        pipeline.exportPipelineImages(filesManager, IMAGE_FILENAME);
    }

}
