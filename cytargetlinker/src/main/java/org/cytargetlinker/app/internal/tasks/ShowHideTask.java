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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cytargetlinker.app.internal.ExtensionManager;
import org.cytargetlinker.app.internal.Plugin;
import org.cytargetlinker.app.internal.Utils;
import org.cytargetlinker.app.internal.data.DataSource;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

/**
 * 
 * @author martina kutmon
 * Task that shows and hides edges based on threshold and show/hide option
 *
 */
public class ShowHideTask extends AbstractTask {

	private Plugin plugin;
	private CyNetwork network;
	private ExtensionManager exMgr;
	
	public ShowHideTask(Plugin plugin, ExtensionManager exMgr) {
		this.plugin = plugin;
		this.exMgr = exMgr;
		this.network = exMgr.getNetwork();
	}
	
	@Override
	public void run(TaskMonitor arg0) throws Exception {
		CyNetworkView view = Utils.getNetworkView(network, plugin);
		Integer threshold = plugin.getExtensionManager(network).getThreshold();
		for(DataSource ds : exMgr.getDatasources()) {
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
			if(network.getRow(node).get("ctl.type", String.class).equals("query")) {
				query = true;
			}
			boolean hide = true;
			List<CyNode> nodes = network.getNeighborList(node, CyEdge.Type.ANY);
			for(CyNode neighbor : nodes) {
				List<CyEdge> edges = network.getConnectingEdgeList(node, neighbor, CyEdge.Type.ANY);
				int count = 0;
				for(CyEdge edge : edges) {
					DataSource ds = exMgr.getDataSourceByName(network.getRow(edge).get("datasource", String.class));
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
						DataSource ds = exMgr.getDataSourceByName(network.getRow(edge).get("datasource", String.class));
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
		plugin.getPanel().updateView();
		view.updateView();
	}

}
