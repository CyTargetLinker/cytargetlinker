// CyTargetLinker,
// a Cytoscape plugin to extend biological networks with regulatory interaction
//
// Copyright 2011-2013 Department of Bioinformatics - BiGCaT, Maastricht University
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
package org.cytargetlinker.app.internal.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ColorSet {

	private List<Color> colors;
	public static final int COLOR = (int) (Math.random() * 256);
	
	public ColorSet() {
		colors = new ArrayList<Color>();
		colors.add(new Color(228,26,28));
		colors.add(new Color(55,26,184));
		colors.add(new Color(77,175,74));
		colors.add(new Color(152,78,163));
		colors.add(new Color(255,127,0));
		colors.add(new Color(166,86,40));
		colors.add(new Color(247,129,191));
		
	}
	
	public Color getColor(int num) {
		if(colors.size() > num) {
			return colors.get(num-1);
		} else {
			return new Color(COLOR, COLOR, COLOR);
		}
	}
}
