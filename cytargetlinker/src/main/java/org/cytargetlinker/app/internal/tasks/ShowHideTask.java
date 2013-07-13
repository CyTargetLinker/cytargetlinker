package org.cytargetlinker.app.internal.tasks;

import java.util.Collection;
import java.util.List;

import org.cytargetlinker.app.internal.Plugin;
import org.cytargetlinker.app.internal.Utils;
import org.cytargetlinker.app.internal.data.Edge;
import org.cytargetlinker.app.internal.data.Result;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class ShowHideTask extends AbstractTask {

	private Plugin plugin;
	private CyNetwork network;
	private boolean show;
	private Result result;
	
	public ShowHideTask(Plugin plugin, CyNetwork network, boolean show, Result result) {
		this.plugin = plugin;
		this.network = network;
		this.show = show;
		this.result = result;
	}
	
	@Override
	public void run(TaskMonitor arg0) throws Exception {
		Collection<CyNetworkView> views = plugin.getCyNetworkViewManager().getNetworkViews(network);
		CyNetworkView view;
		if(!views.isEmpty()) {
			view = views.iterator().next();
		} else {
			view = plugin.getNetworkViewFactory().createNetworkView(network);
			plugin.getCyNetworkViewManager().addNetworkView(view);
		}
		
		Integer threshold = plugin.getExtensionManager(network).getThreshold();
		
		if(show) {
			List<CyEdge> list = result.getHiddenEdges();
			for(CyEdge e : list) {
				view.getNodeView(e.getSource()).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, true);
				view.getNodeView(e.getTarget()).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, true);
				view.getEdgeView(e).setVisualProperty(BasicVisualLexicon.EDGE_VISIBLE, true);
			}
			
		} else {
			for(Edge e : result.getEdges()) {
				view.getEdgeView(e.getCyEdge()).setVisualProperty(BasicVisualLexicon.EDGE_VISIBLE, false);
				result.getHiddenEdges().add(e.getCyEdge());
			}
		}
		
		if(show) {
			result.getHiddenEdges().clear();
		}
		Utils.applyThreshold(plugin, network, view, threshold);
		view.updateView();
	}

}
