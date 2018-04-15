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

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cytargetlinker.app.internal.data.LinkSet;
import org.cytargetlinker.app.internal.data.LinkSetType;

public class LinkSetManager {

	private Set<LinkSet> linkSets;
	
	public LinkSetManager() {
		linkSets = new HashSet<LinkSet>();
	}
	
	public void loadLinkSets(List<File> lsFiles) {
		int count = 1;
		for(File f : lsFiles) {
			if(f.getName().endsWith(".xgmml")) {
				LinkSet datasource = new LinkSet(LinkSetType.XGMML_FILE, f);
				datasource.setSourceName(f.getName());
				datasource.setColor(new ColorSet().getColor(count));
				linkSets.add(datasource);
				count++;
			}
		}	
	}
	
	public void loadLinkSets(File directory) {
		int count = 1;
		for(File f : directory.listFiles()) {
			if(f.getName().endsWith(".xgmml")) {
				LinkSet linkSet = new LinkSet(LinkSetType.XGMML_FILE, f);
				linkSet.setSourceName(f.getName());
				linkSet.setColor(new ColorSet().getColor(count));
				linkSets.add(linkSet);
				count++;
			}
		}
	}

	public Set<LinkSet> getLinkSets() {
		return linkSets;
	}
}
