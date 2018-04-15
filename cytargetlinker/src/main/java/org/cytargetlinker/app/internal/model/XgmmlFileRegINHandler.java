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

package org.cytargetlinker.app.internal.model;

import java.util.Set;

import org.cytargetlinker.app.internal.data.Direction;
import org.cytargetlinker.app.internal.data.LinkSet;
import org.cytargetlinker.app.internal.data.LinkSetType;
import org.cytargetlinker.app.internal.data.Result;
import org.cytargetlinker.app.internal.io.XgmmlParser;

/**
 * 
 * @author mkutmon
 * extension handler for XGMML files
 *
 */
public class XgmmlFileRegINHandler implements ExtensionHandler {

	@Override
	public Result getNeighbours(Set<String> nodeIds, Direction direction, LinkSet linkSet) throws InvalidLinkSetException {
		if(linkSet.getSource().exists()) {
			try {
				XgmmlParser parser = new XgmmlParser();
				Result res = parser.parseXgmmlFile(linkSet.getSource(), nodeIds, direction, linkSet);
				linkSet.setName(res.getLinkSetName());
				res.setLinkSet(linkSet);
				return res;
			} catch (Exception e) {
				throw new InvalidLinkSetException(linkSet, "WARN: Could not parse LinkSet: " + linkSet.getSource().getAbsolutePath());
			}
		} else {
			throw new InvalidLinkSetException(linkSet, "Could not parse LinkSet: " + linkSet.getSource().getAbsolutePath());
		}
	}

	public static LinkSetType getLinkSetType() {
		return LinkSetType.XGMML_FILE;
	}
}
