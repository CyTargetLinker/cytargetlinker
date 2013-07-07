package org.cytargetlinker.app.internal.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cytargetlinker.app.internal.ExtensionManager;
import org.cytargetlinker.app.internal.Plugin;
import org.cytargetlinker.app.internal.data.DataSource;
import org.cytargetlinker.app.internal.data.DatasourceType;
import org.cytargetlinker.app.internal.data.Direction;
import org.cytargetlinker.app.internal.data.ExtensionStep;
import org.cytargetlinker.app.internal.gui.ColorSet;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class ExtensionTask extends AbstractTask  {

	private Plugin plugin;
	private CyNetwork network;
	private String idAttribute;
	private Direction direction;
	private List<CyNode> nodes;
	private List<DataSource> datasources;
	
	public ExtensionTask(Plugin plugin, CyNetwork network, String idAttribute, Direction direction, List<CyNode> nodes, List<DataSource> datasources) {
		this.plugin = plugin;
		this.network = network;
		this.idAttribute = idAttribute;
		this.direction = direction;
		this.nodes = nodes;
		this.datasources = datasources;
	}
	
	@Override
	public void run(TaskMonitor tm) throws Exception {
		tm.setTitle("CyTargetLinker Extension");
		tm.setStatusMessage("Extract regulatory interactions from RINS (this might take a while ...)");
		tm.setProgress(0.1);
		
		List<String> ids = new ArrayList<String>();
		for(CyNode node : nodes) {
			String id = network.getRow(node).get(idAttribute, String.class);
			if(id != null && !ids.contains(id)) {
				ids.add(id);
			}
		}
		
		System.out.println("Start");
		ExtensionManager mgr = plugin.getExtensionManager(network);
		ExtensionStep step = mgr.extendNodes(ids, datasources, direction, idAttribute);
		tm.setStatusMessage("Visualizing result network");
		tm.setProgress(0.7);
		step.execute();
		
		System.out.println("Done");
		
		tm.setStatusMessage("Update network view");
		tm.setProgress(0.9);
		
		VisualStyle vs = plugin.getVisualStypeCreator().getVisualStyle(network);
		plugin.getVmmServiceRef().addVisualStyle(vs);
		
		plugin.getCyNetMgr().addNetwork(network);

		Collection<CyNetworkView> views = plugin.getCyNetworkViewManager().getNetworkViews(network);
		CyNetworkView view;
		if(!views.isEmpty()) {
			view = views.iterator().next();
		} else {
			view = plugin.getNetworkViewFactory().createNetworkView(network);
			plugin.getCyNetworkViewManager().addNetworkView(view);
		}
	
		vs.apply(view);
		
		tm.setStatusMessage("Applying layout");
		tm.setProgress(0.95);
		Set<View<CyNode>> nodes = new HashSet<View<CyNode>>();
		CyLayoutAlgorithm layout = plugin.getCyAlgorithmManager().getLayout("force-directed");
		insertTasksAfterCurrentTask(layout.createTaskIterator(view, layout.createLayoutContext(), nodes, null));
		
		view.updateView();
	
		tm.setProgress(1.0);
	}

}
