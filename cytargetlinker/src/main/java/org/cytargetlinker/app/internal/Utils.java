package org.cytargetlinker.app.internal;

import java.util.Collection;
import java.util.List;

import org.cytargetlinker.app.internal.data.Result;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.vizmap.VisualStyle;

public class Utils {

	public static void updateVisualStyle(Plugin plugin, CyNetworkView view, CyNetwork network) {
		VisualStyle vs = plugin.getVisualStypeCreator().getVisualStyle(network);
		plugin.getVisualMappingManager().addVisualStyle(vs);
		plugin.getVisualMappingManager().setVisualStyle(vs, view);
		vs.apply(view);
		view.updateView();
	}

	public static void applyThreshold(Plugin plugin, CyNetwork network, CyNetworkView view, Integer threshold) {
		ExtensionManager mgr = plugin.getExtensionManager(network);
		for (CyNode node : network.getNodeList()) {
			view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, true);
		}
		for (CyNode node : network.getNodeList()) {
			List<CyNode> nodes = network.getNeighborList(node, CyEdge.Type.ANY);
			
			for(CyNode neighbor : nodes) {
				List<CyEdge> edges = network.getConnectingEdgeList(node, neighbor, CyEdge.Type.ANY);
				if(edges.size() < threshold) {
					for(CyEdge e : edges) {
						if(mgr.getEdges().containsKey(e)) {
							Result r = mgr.getEdges().get(e);
							System.out.println("THRESHOLD\t HIDE EDGE: " + e.getSUID() + "\t" + e.getSource().getSUID() + " -> " + e.getTarget().getSUID());
							view.getEdgeView(e).setVisualProperty(BasicVisualLexicon.EDGE_VISIBLE, false);
							r.getHiddenEdges().add(e);
						}
					}
				}
			}
        }
		for (CyNode node : network.getNodeList()) {
			boolean needed = false;
			for(CyNode neighbor : network.getNeighborList(node, CyEdge.Type.ANY)) {
				List<CyEdge> edges = network.getConnectingEdgeList(node, neighbor, CyEdge.Type.ANY);
				for(CyEdge e : edges) {
					if(view.getEdgeView(e).getVisualProperty(BasicVisualLexicon.EDGE_VISIBLE)) {
						needed = true;
						break;
					}
				}
			}
			if(!needed) {
                view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, false);
			}
		}
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
