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

import javax.swing.JOptionPane;

import org.cytargetlinker.app.internal.CTLManager;
import org.cytargetlinker.app.internal.ui.ExtensionDialog;
import org.cytoscape.application.swing.AbstractCyAction;

/**
 * 
 * @author mkutmon
 * Adds menu item to Apps -> CyTargetLinker -> Extend Network
 * If no network is opened - warning
 * If network is opened - extension dialog is opened
 *
 */
public class ExtensionAction extends AbstractCyAction {

	private CTLManager manager;
	
	public ExtensionAction(final String menuTitle, CTLManager manager) {
		super(menuTitle);
		setPreferredMenu("Apps.CyTargetLinker");
		this.manager = manager;
	}

	/**
	 * at least one network loaded -> opens extension dialog
	 * no network loaded -> warning
	 */
	public void actionPerformed(ActionEvent e) {
		if(manager.getCurrentNetwork() != null) {
			ExtensionDialog dlg = new ExtensionDialog(manager);
			dlg.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(manager.getApplicationFrame(), "No network in current session.", "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}
}
