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

import java.util.Set;

import org.cytargetlinker.app.internal.data.Direction;
import org.cytargetlinker.app.internal.data.LinkSet;
import org.cytargetlinker.app.internal.data.Result;

/**
 * 
 * @author mkutmon
 * interface which is implemented by different datasource types
 *
 */
public interface ExtensionHandler {

	public Result getNeighbours(Set<String> nodeIds, Direction direction, LinkSet linkSet) throws InvalidLinkSetException;

}
