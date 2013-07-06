package org.cytargetlinker.app.internal.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.cytoscape.application.swing.CyMenuItem;
import org.cytoscape.application.swing.CyNodeViewContextMenuFactory;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;

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