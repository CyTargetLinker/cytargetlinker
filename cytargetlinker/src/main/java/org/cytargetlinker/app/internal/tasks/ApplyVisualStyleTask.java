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

import org.cytargetlinker.app.internal.CTLManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import org.cytoscape.work.swing.DialogTaskManager;

public class ApplyVisualStyleTask extends AbstractTask {

	private final CTLManager manager;
	
	@Tunable (description="Network to expand. default = current network")
	public CyNetwork network;
	
	// is task started from gui?
	private boolean gui = false;
	
	public ApplyVisualStyleTask(CTLManager manager, CyNetwork network) {
		this.manager = manager;
		this.network = network;
	}
	
	public ApplyVisualStyleTask(CTLManager manager, CyNetwork network, boolean gui) {
		this.manager = manager;
		this.network = network;
		this.gui = gui;
	}

	@Override
	public void run(TaskMonitor monitor) {
		monitor.setTitle("CyTargetLinker Apply Visualstyle");
		monitor.setStatusMessage("Apply visual style to CyTargetLinker extended network");
		
		if(network == null) {
			network = manager.getCurrentNetwork();
			if(!manager.isCTLNetwork(network)) {
				network = null;
			}
		}
		if(network != null) {
			VisualStyle vs = manager.getVisualStypeCreator().getVisualStyle(network);
			
			CyNetworkView view = manager.getNetworkView(network);
					
			manager.getVisualMappingManager().setVisualStyle(vs, view);
			vs.apply(view);
			view.updateView();
			monitor.setStatusMessage("Visualstyle successfully applied.");
			if(gui) {
				ApplyLayoutTaskFactory fact = new ApplyLayoutTaskFactory(manager);
				TaskIterator it = fact.createTaskIterator(network);
				manager.getService(DialogTaskManager.class).execute(it);
			}
		} else {
			monitor.showMessage(TaskMonitor.Level.ERROR, "No or invalid network selected.");
		}
	}
}
