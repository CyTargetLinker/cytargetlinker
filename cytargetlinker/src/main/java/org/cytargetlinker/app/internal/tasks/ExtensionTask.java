package org.cytargetlinker.app.internal.tasks;

import java.io.File;
import java.util.ArrayList;
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
	private File directory;
	
	public ExtensionTask(Plugin plugin, CyNetwork network, String idAttribute, Direction direction, List<CyNode> nodes, File directory) {
		this.plugin = plugin;
		this.network = network;
		this.idAttribute = idAttribute;
		this.direction = direction;
		this.nodes = nodes;
		this.directory = directory;
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
		
		List<DataSource> datasources = new ArrayList<DataSource>();
		int count = 1;
		for(File f : directory.listFiles()) {
			if(f.getName().endsWith(".xgmml")) {
				DataSource ds = new DataSource(DatasourceType.XGMML_FILE, f.getAbsolutePath());
				ds.setColor(new ColorSet().getColor(count));
				datasources.add(ds);
				count++;
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
		
		plugin.getCyNetMgr().addNetwork(network);
		CyNetworkView view = plugin.getNetworkViewFactory().createNetworkView(network);
		plugin.getCyNetViewMgr().addNetworkView(view);
		
		VisualStyle vs = plugin.getVisualStypeCreator().getVisualStyle(network);
		plugin.getVmmServiceRef().addVisualStyle(vs);
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
