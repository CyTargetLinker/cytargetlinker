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
package org.cytargetlinker.app.internal.data;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import org.cytoscape.model.CyEdge;

public class DataSource implements Comparable<DataSource> {

	private DatasourceType type;
	private String source;
	private String name;
	private Color color;
	private String sourceName;
	
	private Set<CyEdge> edges;
	private Set<CyEdge> hiddenEdges;
	private boolean show = true;
	
	public DataSource(DatasourceType type, String source) {
		super();
		this.type = type;
		this.source = source;
		edges = new HashSet<CyEdge>();
		hiddenEdges = new HashSet<CyEdge>();
	}
	
	public DatasourceType getType() {
		return type;
	}

	public String getSource() {
		return source;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Set<CyEdge> getEdges() {
		return edges;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public Set<CyEdge> getHiddenEdges() {
		return hiddenEdges;
	}

	@Override
	public int compareTo(DataSource o) {
		Integer s = edges.size();
		Integer s2 = o.getEdges().size();
		return s.compareTo(s2);
	}
}
