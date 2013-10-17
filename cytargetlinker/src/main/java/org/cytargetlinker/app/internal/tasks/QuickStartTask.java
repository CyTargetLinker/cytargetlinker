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

/**
 * 
 * @author martina kutmon
 * Quick start task 
 * TODO: fix to make it more generic
 *
 */
public class QuickStartTask extends AbstractTask {
	
	private Plugin plugin;
	
	public QuickStartTask (Plugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run(TaskMonitor tm) throws Exception {
		tm.setTitle("CyTargetLinker Quick Start");
		tm.setStatusMessage("Creating intial network");
		tm.setProgress(0.1);
		CyNetwork net = plugin.getCyNetworkFactory().createNetwork();
		net.getRow(net).set(CyNetwork.NAME, "CyTargetLinker Quick Start");
		
		String id = "1879";	
		String id2 = "55699";
		
		CyNode node1 = net.addNode();
		net.getRow(node1).set(CyNetwork.NAME, id);
		net.getRow(node1).getTable().createColumn("ctl.id", String.class, false);
		net.getRow(node1).set("ctl.id", id);
		
		CyNode node2 = net.addNode();
		net.getRow(node2).set(CyNetwork.NAME, id2);
		net.getRow(node2).set("ctl.id", id2);
		
		File file = new File("/home/mku/networks/hsa-rins");
			
		List<DataSource> datasources = new ArrayList<DataSource>();
		int count = 1;
		for(File f : file.listFiles()) {
			if(f.getName().endsWith(".xgmml")) {
				DataSource ds = new DataSource(DatasourceType.XGMML_FILE, f.getAbsolutePath());
				ds.setColor(new ColorSet().getColor(count));
				datasources.add(ds);
				count++;
			}
		}

		System.out.println("Start");
		ExtensionManager mgr = plugin.getExtensionManager(net);
		tm.setStatusMessage("Extract regulatory interactions from RegINS (this might take a while ...)");
		tm.setProgress(0.2);
		List<String> ids = new ArrayList<String>();
		ids.add(id);
		ids.add(id2);
		ExtensionStep step = mgr.extendNodes(ids, datasources, Direction.SOURCE, "ctl.id");
		
		tm.setStatusMessage("Visualizing result network");
		tm.setProgress(0.7);
		
		step.execute();
		
		System.out.println("Done");
		
		tm.setStatusMessage("Update network view");
		tm.setProgress(0.9);
		
		plugin.getCyNetworkManager().addNetwork(net);
		CyNetworkView view = plugin.getNetworkViewFactory().createNetworkView(net);
		plugin.getCyNetworkViewManager().addNetworkView(view);
		
		VisualStyle vs = plugin.getVisualStypeCreator().getVisualStyle(net);
		plugin.getVisualMappingManager().addVisualStyle(vs);
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
