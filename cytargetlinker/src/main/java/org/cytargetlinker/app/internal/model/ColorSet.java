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
import java.util.List;

/**
 * 
 * @author mkutmon
 * returns one of the colors specified OR
 * if more datasources are used, it returns a random color
 *
 */
public class ColorSet {

	private List<Color> colors;
	public static final int COLOR = (int) (Math.random() * 256);
	
	// default color set
	public ColorSet() {
		colors = new ArrayList<Color>();
		Color c = new Color(31, 120, 180);
        colors.add(c);
        // red
        c = new Color(227, 26, 28);
        colors.add(c);
        // violet
        c = new Color(106, 61, 154);
        colors.add(c);
        // orange
        c = new Color(255, 127, 0);
        colors.add(c);
        // green
        c = new Color(51, 160, 44);
        colors.add(c);
        
        // light blue
        c = new Color(166, 206, 227);
        colors.add(c);
        // light red
        c = new Color(251, 154, 153);
        colors.add(c);
        // light violet
        c = new Color(202, 178, 214);
        colors.add(c);
        // yellow
        c = new Color(253, 191, 111);
        colors.add(c);
        // light green
        c = new Color(178, 223, 138);
        colors.add(c);
	}
	
	public Color getColor(int num) {
		if(colors.size() > num) {
			return colors.get(num-1);
		} else {
			return new Color(COLOR, COLOR, COLOR);
		}
	}
}
