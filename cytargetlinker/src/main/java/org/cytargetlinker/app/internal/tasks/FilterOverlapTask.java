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
import java.util.List;
import java.util.Set;

import org.cytargetlinker.app.internal.CTLManager;
import org.cytargetlinker.app.internal.data.LinkSet;
import org.cytargetlinker.app.internal.model.ExtensionManager;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

public class FilterOverlapTask extends AbstractTask {

	private final CTLManager manager;
	
	@Tunable (description="Number of link sets that need to support an interactions", context=Tunable.NOGUI_CONTEXT)
	public Integer overlap;
	
	@Tunable (description="Network to expand. default = current network")
	public CyNetwork network;
	
	public FilterOverlapTask(final CTLManager manager, final CyNetwork network) {
		this.network = network;
		this.manager = manager;
		overlap = 1;
	}
	
	public FilterOverlapTask(final CTLManager manager, final CyNetwork network, int overlap) {
		this.network = network;
		this.manager = manager;
		this.overlap = overlap;
	}
	
	@Override
	public void run(TaskMonitor monitor) throws Exception {
		ExtensionManager exMgr = manager.getExtensionManager(network);
		
		CyNetworkView view = manager.getNetworkView(network);
		Integer threshold = exMgr.getThreshold();
		
		for(LinkSet ds : exMgr.getLinkSets()) {
			ds.getHiddenEdges().clear();
		}
		for (CyNode node : network.getNodeList()) {
			view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, true);
		}
		for (CyEdge edge : network.getEdgeList()) {
			view.getEdgeView(edge).setVisualProperty(BasicVisualLexicon.EDGE_VISIBLE, true);
		}
		Set<CyNode> hideNodes = new HashSet<CyNode>();
		for(CyNode node : network.getNodeList()) {
			boolean query = false;
			if(network.getRow(node).get("CTL.Ext", String.class).equals("initial")) {
				query = true;
			}
			boolean hide = true;
			List<CyNode> nodes = network.getNeighborList(node, CyEdge.Type.ANY);
			for(CyNode neighbor : nodes) {
				List<CyEdge> edges = network.getConnectingEdgeList(node, neighbor, CyEdge.Type.ANY);
				int count = 0;
				for(CyEdge edge : edges) {
					LinkSet ds = exMgr.getLinkSetByName(network.getRow(edge).get("CTL.LinkSet", String.class));
					if(ds != null) {
						if(!ds.isShow()) {
							ds.getHiddenEdges().add(edge);
							view.getEdgeView(edge).setVisualProperty(BasicVisualLexicon.EDGE_VISIBLE, false);
						} else {
							count++;
						}
					}
				}
				if(count >= threshold) {
					hide = false;
				} else {
					for(CyEdge edge : edges) {
						LinkSet ds = exMgr.getLinkSetByName(network.getRow(edge).get("CTL.LinkSet", String.class));
						if(ds != null) {
							ds.getHiddenEdges().add(edge);
							view.getEdgeView(edge).setVisualProperty(BasicVisualLexicon.EDGE_VISIBLE, false);
						}
					}
				}
			}
			if(hide) {
				if(!query) {
					hideNodes.add(node);
				}
			}
		}
		
		for(CyNode node : hideNodes) {
			view.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, false);
		}
		view.updateView();
	}
}
