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
package org.cytargetlinker.app.internal.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cytargetlinker.app.internal.data.Edge;
import org.cytargetlinker.app.internal.data.Node;
import org.cytargetlinker.app.internal.data.Result;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

/**
 * 
 * @author mkutmon
 * One extension step with multiple datasources
 * creates all the necessary CyNodes and CyEdges for the extended network
 *
 */
public class ExtensionStep {

	// result list of edges for each LinkSet (datasource)
	private List<Result> results;
	
	// which extension step level is it
	private int stepNum = 0;
	
	private CyNetwork network;
	private String idAttribute;
		
	// nodes added in this extension step
	private Map<String, CyNode> nodesAdded;
			
	public ExtensionStep(CyNetwork network, String idAttribute) {
		this.network = network;
		this.idAttribute = idAttribute;
		results = new ArrayList<Result>();
		nodesAdded = new HashMap<String, CyNode>();
	}
	
	/**
	 * executes extension step
	 * add CyNodes and CyEdges
	 */
	public void execute() {
		Map<String, CyNode> nodesPresent = new HashMap<String, CyNode>();
		
		for(CyNode node : network.getNodeList()) {
			String value = network.getRow(node).get(idAttribute, String.class);
			nodesPresent.put(value, node);
			
			// add column to indicate if the node was in the original query list
			if(network.getRow(node).getTable().getColumn("CTL.Ext") == null) {
				network.getRow(node).getTable().createColumn("CTL.Ext", String.class, false);
			}
			if(network.getRow(node).get("CTL.Ext", String.class) == null) {
				network.getRow(node).set("CTL.Ext", "initial");
			}
	
			if(network.getRow(node).getTable().getColumn("CTL.Type") == null) {
				network.getRow(node).getTable().createColumn("CTL.Type", String.class, false);
			}
			if(network.getRow(node).get("CTL.Type", String.class) == null) {
				network.getRow(node).set("CTL.Type", "initial");
			}
		}
		
		for(CyEdge edge : network.getEdgeList()) {
			if(network.getRow(edge).getTable().getColumn("CTL.Ext") == null) {
				network.getRow(edge).getTable().createColumn("CTL.Ext", String.class, false);
			}
			if(network.getRow(edge).get("CTL.Ext", String.class) == null) {
				network.getRow(edge).set("CTL.Ext", "initial");
			}
		}
			
		for(Result r : results) {
			for(Edge e : r.getEdges()) {
				Node source = e.getSource();
				Node target = e.getTarget();
				
				for(String id : source.getIdentifiers()) {
					if(nodesPresent.containsKey(id)) {
						addAttributes(source, nodesPresent.get(id));
						e.setCySource(nodesPresent.get(id));
					}
				}
				for(String id : target.getIdentifiers()) {
					if(nodesPresent.containsKey(id)) {
						addAttributes(target, nodesPresent.get(id));
						e.setCyTarget(nodesPresent.get(id));
					}
				}
				
				if(e.getCySource() == null) {
					CyNode s = nodeAdded(source);
					if(s == null) {
						s = addNode(source);
						r.getAddedNodes().add(source);
					}
					e.setCySource(s);
				} else if (e.getCyTarget() == null) {
					CyNode t = nodeAdded(target);
					if(t == null) {
						t = addNode(target);
						r.getAddedNodes().add(target);
					}
					e.setCyTarget(t);
				}
				
				// check if edge from this datasource is already present
				// especially important in the case of e.g. UniProt identifiers
				// because multiple ENS ids link to the same UniProt identifier
				boolean present = false;
				List<CyEdge> edgeList = network.getConnectingEdgeList(e.getCySource(), e.getCyTarget(), CyEdge.Type.ANY);
				
				for(CyEdge edge : edgeList) {
					if(network.getRow(edge).getTable().getColumn("CTL.LinkSet") == null) {
						network.getRow(edge).getTable().createColumn("CTL.LinkSet", String.class, false);
					}
					String ls = network.getRow(edge).get("CTL.LinkSet", String.class);
					if(e.getLinkSet().getName().equals(ls)) {
						present = true;
					}
				}
				
				if(!present) {
					// create edge
					CyEdge cyEdge = network.addEdge(e.getCySource(), e.getCyTarget(), true);
					e.setCyEdge(cyEdge);
					r.getAddedEdges().add(e);
					
					if(network.getRow(cyEdge).getTable().getColumn("CTL.Ext") == null) {
						network.getRow(cyEdge).getTable().createColumn("CTL.Ext", String.class, false);
					}
					network.getRow(cyEdge).set("CTL.Ext", "step " + stepNum);
					
					for(String str : e.getAttributes().keySet()) {
						if(network.getRow(cyEdge).getTable().getColumn("CTL." + str) == null) {
							network.getRow(cyEdge).getTable().createColumn("CTL." + str, String.class, false);
						}
						network.getRow(cyEdge).set("CTL." + str, e.getAttributes().get(str));
					}
					if(network.getRow(cyEdge).getTable().getColumn("CTL.LinkSet") == null) {
						network.getRow(cyEdge).getTable().createColumn("CTL.LinkSet", String.class, false);
					}
					network.getRow(cyEdge).set("CTL.LinkSet", e.getLinkSet().getName());
					
					r.getLinkSet().getEdges().add(cyEdge);
				}
			}
		}
		if(network.getRow(network).getTable().getColumn("CTL.Ext" + stepNum) == null) {	
			network.getRow(network).getTable().createListColumn("CTL.Ext" + stepNum, String.class, false);
		}
		
		List<String> list = new ArrayList<String>();
		for(Result r : results) {
			list.add(r.getLinkSetName() + ":" + r.getAddedEdges().size() + ":" + r.getAddedNodes().size() + ":" + r.getLinkSet().getColor() + ":" + r.getLinkSet().getSource().getAbsolutePath());
		}
		network.getRow(network).set("CTL.Ext" + stepNum, list);
	}
		
	private CyNode addNode(Node node) {
		CyNode cyNode = network.addNode();
		network.getRow(cyNode).set(idAttribute, node.getId());

		addAttributes(node, cyNode);
		if(node.getAttributes().containsKey("label")) {
			network.getRow(cyNode).set("name", node.getAttributes().get("label"));
			if(network.getRow(cyNode).getTable().getColumn("display name") != null) {
				network.getRow(cyNode).set("display name", node.getAttributes().get("label"));
			}			
		} else {
			network.getRow(cyNode).set("name", node.getId());
			if(network.getRow(cyNode).getTable().getColumn("display name") != null) {
				network.getRow(cyNode).set("display name", node.getId());
			}	
		}
		
		network.getRow(cyNode).set("CTL.Ext", "step " + stepNum);
		
		for(String str : node.getIdentifiers()) {
			nodesAdded.put(str, cyNode);
		}
		return cyNode;
	}
	
	private void addAttributes(Node node, CyNode cyNode) {
		for(String str : node.getAttributes().keySet()) {
			if(str.equals("biologicalType") || str.equals("type")) {
				if(network.getRow(cyNode).get("CTL.Type", String.class) == null) {
					network.getRow(cyNode).set("CTL.Type", node.getAttributes().get(str));
				}
			} else {
				if(network.getRow(cyNode).getTable().getColumn("CTL." + str) == null) {
					network.getRow(cyNode).getTable().createColumn("CTL." + str, String.class, false);
				}
				network.getRow(cyNode).set("CTL." + str, node.getAttributes().get(str));
			}
		}
	}

	private CyNode nodeAdded(Node node) {
		for(String id : node.getIdentifiers()) {
			if(nodesAdded.keySet().contains(id)) {
				return nodesAdded.get(id);
			}
		}
		return null;
	}
	
	// ////////////////////////////////////
	// SETTERS AND GETTERS
	// ////////////////////////////////////

	public List<Result> getResults() {
		return results;
	}

	public int getStepNum() {
		return stepNum;
	}

	public void setStepNum(int stepNum) {
		this.stepNum = stepNum;
	}
}
