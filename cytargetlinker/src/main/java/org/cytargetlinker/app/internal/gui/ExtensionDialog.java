package org.cytargetlinker.app.internal.gui;

import java.util.List;

import javax.swing.JDialog;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTableUtil;

public class ExtensionDialog extends JDialog {

	
	public ExtensionDialog(CyNetwork network) {
		List<CyNode> nodes = CyTableUtil.getNodesInState(network,"selected",true);
		if(nodes.size() != 0) {
			
		}
	}
}
