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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author martina kutmon
 * Contains the resulting edges for one datasource in one extension step
 *
 */
public class Result {

	private DataSource ds;
	private String rinName;
	private String rinUrl;
	private String rinType;
	
	private Direction dir;

	private List<Edge> edges;
	
	private boolean show = true;
	
	public Result() {
		edges = new ArrayList<Edge>();
	}
	
	public String getRinName() {
		return rinName;
	}
	public void setRinName(String rinName) {
		this.rinName = rinName;
	}
	public String getRinUrl() {
		return rinUrl;
	}
	public void setRinUrl(String rinUrl) {
		this.rinUrl = rinUrl;
	}
	public List<Edge> getEdges() {
		return edges;
	}
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}
	public String getRinType() {
		return rinType;
	}
	public void setRinType(String rinType) {
		this.rinType = rinType;
	}
	public Direction getDir() {
		return dir;
	}
	public void setDir(Direction dir) {
		this.dir = dir;
	}

	public DataSource getDs() {
		return ds;
	}
	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}
}
