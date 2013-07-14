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

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.util.swing.OpenBrowser;

/**
 * 
 * @author martina kutmon
 * Adds menu item to Apps -> CyTargetLinker -> Help
 * Opens CyTargetLinker website
 *
 */
public class HelpAction extends AbstractCyAction {

	private OpenBrowser openBrowser;
	private String helpDeskURL = "http://projects.bigcat.unimaas.nl/cytargetlinker/";

	public HelpAction(final String menuTitle, OpenBrowser browser) {
		super(menuTitle);
		setPreferredMenu("Apps.CyTargetLinker");
		this.openBrowser = browser;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		openBrowser.openURL(helpDeskURL);
	}

}
