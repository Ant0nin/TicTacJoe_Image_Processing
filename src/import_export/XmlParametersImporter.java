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
package import_export;

import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.io.File;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author Antonin Bernardin <antonin.bernardin at etu.unilim.fr>
 */
public class XmlParametersImporter {

    private final File inputDirPath;
    private final boolean forceUseDefaultConfig;

    public XmlParametersImporter(String inputDirPath, boolean forceUseDefaultConfig) {
        this.inputDirPath = new File(inputDirPath);
        this.forceUseDefaultConfig = forceUseDefaultConfig;
    }

    public Map<String, Integer> importParameter(String imageName) {

        imageName = imageName.split("\\.")[0];

        HashMap<String, Integer> parameters = new HashMap();

        try {

            File configFile = new File(inputDirPath + "\\" + imageName + ".xml");
            if (!configFile.exists() || forceUseDefaultConfig) {
                configFile = new File(inputDirPath + "\\_default.xml");
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(configFile);
            doc.getDocumentElement().normalize();

            Node rootNode = doc.getElementsByTagName("config").item(0);
            NodeList childs = rootNode.getChildNodes();

            for (int i = 0; i < childs.getLength(); i++) {
                Node currentNode = childs.item(i);
                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                    String name = currentNode.getNodeName();
                    String stringValue = currentNode.getFirstChild().getTextContent();
                    Integer value;
                    if(stringValue.length() > 1 && stringValue.charAt(1) == 'x') {
                        stringValue = stringValue.substring(2, stringValue.length()-1);
                        value = Integer.parseInt(stringValue, 16);
                    }
                    else
                        value = Integer.parseInt(stringValue);
                    parameters.put(name, value);
                }
            }
            
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture du fichier XML de config");
        }

        return parameters;
    }

}
