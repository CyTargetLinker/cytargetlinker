// CyTargetLinker,
// a Cytoscape plugin to extend biological networks with regulatory interactions and other relationships
//
// Copyright 2011-2018 Department of Bioinformatics - BiGCaT, Maastricht University
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package org.cytargetlinker.app.internal.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author mkutmon
 * returns one of the colors specified OR
 * if more datasources are used, it returns a random color
 *
 */
public class NodeColorSet {

	private Map<String, Color> nodeColorMap;
	private List<Color> colors;
	public static final int COLOR = (int) (Math.random() * 256);
		
	// default color set
	public NodeColorSet() {
		nodeColorMap = new HashMap<String, Color>();
		nodeColorMap.put("initial", new Color(211,211,211));
		nodeColorMap.put("transcriptionFactor", new Color(93, 144, 199));
		nodeColorMap.put("TF", new Color(93, 144, 199));
		nodeColorMap.put("gene", new Color(124,191,182));
		nodeColorMap.put("protein", new Color(124,191,182));
		nodeColorMap.put("target", new Color(124,191,182));
		nodeColorMap.put("microRNA", new Color(245,202,46));
		nodeColorMap.put("drug", new Color(204,133,177));
		nodeColorMap.put("compound", new Color(204,133,177));
		nodeColorMap.put("pathway", new Color(144,111,168));
		nodeColorMap.put("process", new Color(144,111,168));
		
		colors = new ArrayList<Color>();
		// blue
		Color c = new Color(45,85,130);
        colors.add(c);
        // dark green
        c = new Color(75,154,145);
        colors.add(c);
        // dark yellow
        c = new Color(185,146,9);
        colors.add(c);
        // dark pink
        c = new Color(180,75,140);
        colors.add(c);
        // dark purple
        c = new Color(92,67,112);
        colors.add(c);
	}
	
	public Color getColor(int num) {
		if(colors.size() > num) {
			return colors.get(num);
		} else {
			return new Color(COLOR, COLOR, COLOR);
		}
	}
	
	public Map<String, Color> getNodeColorMap() {
		return nodeColorMap;
	}

	public Color getColor(String type) {
		return nodeColorMap.get(type);
	}
}
