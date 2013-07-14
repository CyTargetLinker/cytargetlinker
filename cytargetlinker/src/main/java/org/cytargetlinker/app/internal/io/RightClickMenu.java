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
package org.cytargetlinker.app.internal.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.cytoscape.application.swing.CyMenuItem;
import org.cytoscape.application.swing.CyNodeViewContextMenuFactory;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;

/**
 * 
 * @author martina kutmon
 * TODO: not working yet - test with Java 7
 * ClassNotFound exception for JMenuItem
 *
 */
public class RightClickMenu implements CyNodeViewContextMenuFactory {

	@Override
	public CyMenuItem createMenuItem(CyNetworkView view, View<CyNode> nodeView) {
		JMenuItem item = new JMenuItem("Extend with CyTargetLinker");
		item.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(e.getSource());
			}
		});
		float gravity = 1.0f; 
		CyMenuItem addAsSource = new CyMenuItem(item, gravity); 
		return addAsSource;
	}


}