package org.cytargetlinker.app.internal;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.vizmap.VisualStyle;

public class Utils {

	public static void updateVisualStyle(Plugin plugin, CyNetworkView view, CyNetwork network) {
		VisualStyle vs = plugin.getVisualStypeCreator().getVisualStyle(network);
		plugin.getVisualMappingManager().addVisualStyle(vs);
		plugin.getVisualMappingManager().setVisualStyle(vs, view);
		vs.apply(view);
		view.updateView();
	}

}
