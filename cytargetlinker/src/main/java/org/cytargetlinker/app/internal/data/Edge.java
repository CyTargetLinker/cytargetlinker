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
package org.cytargetlinker.app.internal.data;

import java.util.HashMap;
import java.util.Map;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;

/**
 * 
 * @author mkutmon
 * CyTargetLinker edge object
 *
 */
public class Edge {

	private String id;
	// source and target nodes
	private Node source;
	private Node target;
	
	// data for the edge table
	private Map<String, String> attributes;
	
	// CyNodes in the Cytoscape network
	private CyNode cySource;
	private CyNode cyTarget;
	
	// resulting CyEdge object
	private CyEdge cyEdge;
	
	// Edge was provided by a specific Link set
	private LinkSet ds;
	
	
	public Edge(String id) {
		this.id = id;
		attributes = new HashMap<String, String>();
	}
	
	//////////////////////////////////////
	// SETTERS AND GETTERS
	//////////////////////////////////////
	
	public String getId() {
		return id;
	}
	
	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getTarget() {
		return target;
	}

	public void setTarget(Node target) {
		this.target = target;
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public CyNode getCySource() {
		return cySource;
	}

	public void setCySource(CyNode cySource) {
		this.cySource = cySource;
	}

	public CyNode getCyTarget() {
		return cyTarget;
	}

	public void setCyTarget(CyNode cyTarget) {
		this.cyTarget = cyTarget;
	}
	public CyEdge getCyEdge() {
		return cyEdge;
	}
	public void setCyEdge(CyEdge cyEdge) {
		this.cyEdge = cyEdge;
	}
	public LinkSet getLinkSet() {
		return ds;
	}
	public void setDs(LinkSet ds) {
		this.ds = ds;
	}
}
