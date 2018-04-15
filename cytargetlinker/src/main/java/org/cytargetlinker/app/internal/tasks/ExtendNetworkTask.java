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
package org.cytargetlinker.app.internal.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cytargetlinker.app.internal.CTLManager;
import org.cytargetlinker.app.internal.data.Direction;
import org.cytargetlinker.app.internal.model.ExtensionManager;
import org.cytargetlinker.app.internal.model.ExtensionStep;
import org.cytargetlinker.app.internal.model.LinkSetManager;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.model.subnetwork.CySubNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import org.cytoscape.work.swing.DialogTaskManager;
import org.cytoscape.work.util.ListSingleSelection;

public class ExtendNetworkTask extends AbstractTask {

	private final CTLManager manager;
	
	@Tunable (description="Network to expand. default = current network", context=Tunable.NOGUI_CONTEXT)
	public CyNetwork network;
	
	@Tunable (description="Path to local directory containing linksets used in the extension step (user either linkSetDirectory or linkSetFiles option)", context=Tunable.NOGUI_CONTEXT)
	public String linkSetDirectory;

	@Tunable (description="Path to local files for linksets used in the extension step (user either linkSetDirectory or linkSetFiles option).\n Example: linkSetFiles=\"filetolinkset1.xgmml,filetolinkset2.xgmml,filetolinkset3.xgmml\"", context=Tunable.NOGUI_CONTEXT)
	public String linkSetFiles;
	private List<File> lsFiles;
	
	@Tunable (description="Column name of column containing identifier attribute in the network. default = shared name", context=Tunable.NOGUI_CONTEXT)
	public String idAttribute = "shared name";
	
	@Tunable (description="Direction of extension (source | target | both). default = both", context=Tunable.NOGUI_CONTEXT)
	public ListSingleSelection<String> direction;
	
	// is task started from CTL extension dialog?
	private boolean gui = false;
	
	public ExtendNetworkTask(final CTLManager manager, final CyNetwork network) {
		this.manager = manager;
		
		lsFiles = new ArrayList<File>();
		if(network != null) {
			this.network = network;
		} else if (manager.getCurrentNetwork() != null) {
			this.network = manager.getCurrentNetwork();
		}
		
		List<String> list = new ArrayList<String>();
		list.add(Direction.SOURCES.toString());
		list.add(Direction.TARGETS.toString());
		list.add(Direction.BOTH.toString());
		direction = new ListSingleSelection<String>(list);
		direction.setSelectedValue(Direction.BOTH.toString());
	}
	
//	public ExtendNetworkTask(final CTLManager manager, final CyNetwork network, String linkSetDir, String idAttribute, Direction direction) {
//		this.manager = manager;
//		if(network != null) {
//			this.network = network;
//		} else {
//			this.network = manager.getCurrentNetwork();
//		}		
//		this.idAttribute = idAttribute;
//		
//		linkSetDirectory = linkSetDir;
//		
//		List<String> list = new ArrayList<String>();
//		list.add(Direction.SOURCES.toString());
//		list.add(Direction.TARGETS.toString());
//		list.add(Direction.BOTH.toString());
//		this.direction = new ListSingleSelection<String>(list);
//		this.direction.setSelectedValue(direction.toString());
//	}
	
	public ExtendNetworkTask(final CTLManager manager, final CyNetwork network, List<File> lsFiles, String idAttribute, Direction direction, boolean gui) {
		this.manager = manager;
		this.gui = gui;
		if(network != null) {
			this.network = network;
		} else {
			this.network = manager.getCurrentNetwork();
		}		
		this.idAttribute = idAttribute;
		
		linkSetDirectory = "";
		linkSetFiles = "";
		this.lsFiles = lsFiles;
		
		List<String> list = new ArrayList<String>();
		list.add(Direction.SOURCES.toString());
		list.add(Direction.TARGETS.toString());
		list.add(Direction.BOTH.toString());
		this.direction = new ListSingleSelection<String>(list);
		this.direction.setSelectedValue(direction.toString());
	}

	@Override
	public void run(TaskMonitor monitor) {
		// check if we have a network
		if (network == null) {
			monitor.showMessage(TaskMonitor.Level.ERROR, "No network to expand");
			return;
		}
		
		monitor.setTitle("CyTargetLinker Extension");
		monitor.setStatusMessage("Extract regulatory interactions from RegINS (this might take a while ...)");
		monitor.setProgress(0.1);
		
		if(linkSetFiles != null) {
			String [] buffer = linkSetFiles.split(",");
			if(buffer.length > 0) {
				for(String s  : buffer) {
					File f = new File(s);
					if(f.getName().endsWith(".xgmml")) {
						lsFiles.add(f);
					}
				}
			}
		} else if(linkSetDirectory != null && new File(linkSetDirectory).exists()) {
			for(File f : new File(linkSetDirectory).listFiles()) {
				if(f.getName().endsWith("xgmml")) {
					lsFiles.add(f);
				}
			}
		} else if(lsFiles.isEmpty()) {
			monitor.showMessage(TaskMonitor.Level.ERROR, "No linksets defined.");
			return;
		}
		
		if(lsFiles.size() == 0) {
			monitor.showMessage(TaskMonitor.Level.ERROR, "No valid linksets defined.");
			return;
		}
		
		if(direction.getSelectedValue().length() == 0) {
			direction.setSelectedValue(Direction.BOTH.toString());
		}
		
		monitor.setTitle("CyTargetLinker Extension");
		monitor.setStatusMessage("Extract regulatory interactions from RegINS (this might take a while ...)");
		monitor.setProgress(0.1);
		
		monitor.showMessage(TaskMonitor.Level.INFO, lsFiles.size() + " LinkSet files selected.");
		
		CyNetwork network2Extend = network;
		if(!manager.getExtensions().containsKey(network2Extend)) {
			CyRootNetwork rootNetwork = ((CySubNetwork)network).getRootNetwork();
			CyNetwork subNetwork = rootNetwork.addSubNetwork(network.getNodeList(), network.getEdgeList());
			subNetwork.getRow(subNetwork).set(CyNetwork.NAME, "CTL_" + network.getRow(network).get(CyNetwork.NAME, String.class));
			manager.getNetworkManager().addNetwork(subNetwork);
			network2Extend = subNetwork;
			if(network.getRow(network).getTable().getColumn("CTL.Net") == null) {
				network.getRow(network).getTable().createColumn("CTL.Net", Boolean.class, false);
			}
			network2Extend.getRow(network2Extend).set("CTL.Net", true);
		}
				
		Set<String> nodeIds = getNetworkNodeIds(network.getNodeList());
		
		ExtensionManager exMgr = manager.getExtensionManager(network2Extend);
		LinkSetManager lsManager = new LinkSetManager();
		lsManager.loadLinkSets(lsFiles);
		ExtensionStep step = exMgr.extendNetwork(nodeIds, lsManager.getLinkSets(), Direction.valueOf(direction.getSelectedValue()), idAttribute);
		monitor.setStatusMessage("Visualizing result network");
		monitor.setProgress(0.7);
		step.execute();
		
		monitor.setStatusMessage("Update network view");
		monitor.setProgress(0.9);
		
		CyNetworkView view = manager.getNetworkView(network2Extend);
		view.updateView();
		
		manager.getService(CyApplicationManager.class).setCurrentNetwork(network2Extend);
		manager.getService(CyApplicationManager.class).setCurrentNetworkView(view);
		
		monitor.setProgress(1.0);
		
		if(gui) {
			ApplyVisualStyleTaskFactory fact = new ApplyVisualStyleTaskFactory(manager);
			TaskIterator it = fact.createTaskIterator(network2Extend, true);
			manager.getService(DialogTaskManager.class).execute(it);
		}
	}
	
	private Set<String> getNetworkNodeIds(List<CyNode> nodes) {
		Set<String> ids = new HashSet<String>();
		for(CyNode node : nodes) {
			String id = network.getRow(node).get(idAttribute, String.class);
			if(id != null && !ids.contains(id)) {
				ids.add(id);
			}
		}
		return ids;
	}
}
