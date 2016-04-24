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

import import_export.ImageFilesManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public class ImageProcessPipeline extends AbstractImageProcess {

    private static int count = 0;
    
    private final List<AbstractImageProcess> tasks;
    private final List<BufferedImage> intermediateImages;
    
    public ImageProcessPipeline(AbstractImageProcess... imageProcesses) {
        this(Arrays.asList(imageProcesses));
    }
    
    public ImageProcessPipeline(List<AbstractImageProcess> imageProcesses) {
        super();
        intermediateImages = new ArrayList<>(imageProcesses.size());
        tasks = imageProcesses;
    }
    
    @Override
    public BufferedImage process(BufferedImage input) {
        
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
        intermediateImages.add(input);
        
        for(AbstractImageProcess p : tasks) {
            output = p.process(input);
            intermediateImages.add(output);
            input = output;
        }

        return output;
    }
    
    public void exportPipelineImages(ImageFilesManager fileManager, String filename) {
        
        String[] split = filename.split("\\.");
        StringBuilder fullpath;
        
        for(BufferedImage image : intermediateImages) {
            fullpath = new StringBuilder();
            fullpath.append(split[0]).append('\\').append(count).append('.').append(split[1]);
            fileManager.exportImage(image, fullpath.toString());
            count++;
        }
    }
    
}
