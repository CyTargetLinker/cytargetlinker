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
package org.cytargetlinker.app.internal.action;

import java.awt.event.ActionEvent;

import org.cytargetlinker.app.internal.Plugin;
import org.cytargetlinker.app.internal.tasks.QuickStartTaskFactory;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.work.TaskIterator;

/**
 * 
 * @author martina kutmon
 * Idea: provide a list of identifiers and labels and extend the nodes in a new network
 * TODO: implement dialog and fix QuickStartTask
 *
 */
public class QuickStartAction extends AbstractCyAction {

	private Plugin plugin;
	
	public QuickStartAction(final String menuTitle, Plugin plugin) {
		
		super(menuTitle);
		setPreferredMenu("Apps.CyTargetLinker");
		this.plugin = plugin;
	}

	public void actionPerformed(ActionEvent e) {
		
		QuickStartTaskFactory fact = new QuickStartTaskFactory(plugin);
		TaskIterator it = fact.createTaskIterator();
		plugin.getDialogTaskManager().execute(it);

	}
}