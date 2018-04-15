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

import java.util.HashSet;
import java.util.Set;

import org.cytargetlinker.app.internal.CTLManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

public class ApplyLayoutTask extends AbstractTask  {
	private final CTLManager manager;
	
	@Tunable (description="Network to expand. default = current network")
	public CyNetwork network;
	
	public ApplyLayoutTask(CTLManager manager, CyNetwork network) {
		this.manager = manager;
		this.network = network;
	}

	@Override
	public void run(TaskMonitor monitor) throws Exception {
		monitor.setTitle("CyTargetLinker Apply Layout");
		monitor.setStatusMessage("Apply forced-directed layout to CyTargetLinker extended network");
		if(network == null) {
			network = manager.getCurrentNetwork();
		}
		if(network != null) {
			if(network.getNodeList().size() < 10000) {
				CyNetworkView view = manager.getNetworkView(network);
				
				Set<View<CyNode>> nodes = new HashSet<View<CyNode>>();
				CyLayoutAlgorithm layout = manager.getCyLayoutAlgorithmManager().getLayout("force-directed");
				insertTasksAfterCurrentTask(layout.createTaskIterator(view, layout.createLayoutContext(), nodes, null));
				
				view.updateView();
				monitor.setStatusMessage("Layout successfully applied.");
			} else {
				monitor.setStatusMessage("Network too  big (>10,000 nodes). No layout will be applied.");
			}
		} else {
			monitor.setStatusMessage("No network selected.");
		}
	}
}
