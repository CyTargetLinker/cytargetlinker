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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author mkutmon
 * Contains the resulting edges for one datasource in one extension step
 *
 */
public class Result {

	private LinkSet linkSet;
	private String linkSetName;
	private String linkSetURL;
	private String linkSetType;
	
	private Direction direction;

	private List<Edge> edges;
	private List<Edge> addedEdges;
	private List<Node> addedNodes;
	
	private boolean show = true;
	
	public Result() {
		edges = new ArrayList<Edge>();
		addedEdges = new ArrayList<Edge>();
		addedNodes = new ArrayList<Node>();
	}
	
	// ////////////////////////////////////
	// SETTERS AND GETTERS
	// ////////////////////////////////////
	
	public String getLinkSetName() {
		return linkSetName;
	}
	public void setLinkSetName(String linkSetName) {
		this.linkSetName = linkSetName;
	}
	public String getLinkSetURL() {
		return linkSetURL;
	}
	public void setLinkSetURL(String linkSetURL) {
		this.linkSetURL = linkSetURL;
	}
	public List<Edge> getEdges() {
		return edges;
	}
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
	public String getLinkSetType() {
		return linkSetType;
	}
	public void setLinkSetType(String linkSetType) {
		this.linkSetType = linkSetType;
	}
	public Direction getDirection() {
		return direction;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	public LinkSet getLinkSet() {
		return linkSet;
	}
	public void setLinkSet(LinkSet linkSet) {
		this.linkSet = linkSet;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	public List<Edge> getAddedEdges() {
		return addedEdges;
	}
	public List<Node> getAddedNodes() {
		return addedNodes;
	}
}
