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
