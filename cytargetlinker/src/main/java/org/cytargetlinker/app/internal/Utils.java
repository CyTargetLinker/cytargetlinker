package org.cytargetlinker.app.internal;

import java.util.Collection;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.vizmap.VisualStyle;

public class Utils {

	public static void updateVisualStyle(Plugin plugin, CyNetworkView view, CyNetwork network) {
		VisualStyle vs = plugin.getVisualStypeCreator().getVisualStyle(network);
		plugin.getVisualMappingManager().setVisualStyle(vs, view);
		vs.apply(view);
		view.updateView();
	}


	public static CyNetworkView getNetworkView(CyNetwork network, Plugin plugin) {
		Collection<CyNetworkView> views = plugin.getCyNetworkViewManager().getNetworkViews(network);
		CyNetworkView view;
		if(!views.isEmpty()) {
			view = views.iterator().next();
		} else {
			view = plugin.getNetworkViewFactory().createNetworkView(network);
			plugin.getCyNetworkViewManager().addNetworkView(view);
		}
		return view;
	}
}
