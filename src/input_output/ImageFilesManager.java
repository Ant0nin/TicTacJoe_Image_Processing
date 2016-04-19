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
package input_output;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public class ImageFilesManager {

    final private File inputDirectory;
    final private File outputDirectory;
    
    public ImageFilesManager(String inputDirPath, String outputDirPath) {
        inputDirectory = new File(inputDirPath);
        outputDirectory = new File(outputDirPath);
    }
    
    public BufferedImage importImage(String imageName) {
        
        StringBuilder fullPathName = new StringBuilder();
        BufferedImage inputImage = null;
        
        try {
            fullPathName.append(inputDirectory).append('\\').append(imageName);
            File imageFile = new File(fullPathName.toString());
            inputImage = ImageIO.read(imageFile);
            
        } 
        catch (IOException ex) {
            System.err.println("Impossible d'importer le fichier image "+fullPathName.toString());
        }
        return inputImage;
    }
    
    public void exportImage(BufferedImage outputImage, String fileName) {
         
        StringBuilder fullPathName = new StringBuilder();
        
        try {
            String[] split = fileName.split("\\.");
            String name = split[0];
            String extend = split[1];
            
            fullPathName.append(outputDirectory).append('\\').append(name).append('.').append(extend);
            
            File outputFile = new File(fullPathName.toString());
            ImageIO.write(outputImage, extend, outputFile);
            
        } catch (IOException ex) {
            System.out.println("Impossible d'exporter le fichier image "+fullPathName.toString());
        }
    }    
}
