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

import org.cytargetlinker.app.internal.CTLManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.task.AbstractNetworkTaskFactory;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;

public class ApplyLayoutTaskFactory extends AbstractNetworkTaskFactory implements TaskFactory {
	
	final CTLManager manager;

	public ApplyLayoutTaskFactory(final CTLManager manager) {
		this.manager = manager;
	}
	@Override
	public TaskIterator createTaskIterator(CyNetwork network) {
		return new TaskIterator(new ApplyLayoutTask(manager, network));
	}
	@Override
	public TaskIterator createTaskIterator() {
		return new TaskIterator(new ApplyLayoutTask(manager, null));
	}
	@Override
	public boolean isReady() {
		return true;
	}
}
