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

import java.io.File;
import java.util.List;

import org.cytargetlinker.app.internal.CTLManager;
import org.cytargetlinker.app.internal.data.Direction;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.task.AbstractNetworkTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;

public class ExtendNetworkTaskFactory extends AbstractNetworkTaskFactory implements TaskFactory {
	final CTLManager manager;

	public ExtendNetworkTaskFactory(final CTLManager manager) {
		this.manager = manager;
	}

	public TaskIterator createTaskIterator(CyNetwork network) {
		return new TaskIterator(new ExtendNetworkTask(manager, network));
	}
	
	public TaskIterator createTaskIterator() {
		return new TaskIterator(new ExtendNetworkTask(manager, null));
	}
	
	public TaskIterator createTaskIterator(CyNetwork network, List<File> linkSets, String idAttribute, Direction direction, boolean gui) {
		return new TaskIterator(new ExtendNetworkTask(manager, network, linkSets, idAttribute, direction, gui));
	}

	public boolean isReady() {
		return true;
	}

	public boolean isReady(View<CyNode> nodeView, CyNetworkView netView) {
		return true;
	}
}