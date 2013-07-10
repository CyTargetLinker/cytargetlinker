package org.cytargetlinker.app.internal.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.cytargetlinker.app.internal.Plugin;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.CyNetwork;

public class CyTargetLinkerPanel extends JPanel implements CytoPanelComponent {

	private JPanel contentPanel;
	private JPanel mainPanel;
	private Plugin plugin;
	private JComboBox cbNetworks;
	
	public CyTargetLinkerPanel(Plugin plugin) {
		super();
		this.plugin = plugin;
		plugin.setPanel(this);
	}
	
	@Override
	public Component getComponent() {
		System.out.println("GET COMPONENT");
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.white);
		mainPanel.setLayout(new BorderLayout());
		
		JPanel top = new JPanel();
//		FormLayout layoutTop = new FormLayout("5dlu,pref,5dlu,75dlu,5dlu", "5dlu,pref,5dlu");
//		top.setLayout(layoutTop);
		top.setLayout(new GridLayout(1, 2));
		top.add(new JLabel("Select extended network:"));
		cbNetworks = new JComboBox(getNetworks());
		contentPanel = new JPanel();
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		cbNetworks.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				contentPanel.removeAll();
				
				NetworkName name = (NetworkName) cbNetworks.getSelectedItem();
				contentPanel.add(new JLabel(name.getName() + "\t" + name.getNetwork().getSUID()));
				contentPanel.add(new JLabel(name.getNetwork().getEdgeCount() + "\t" + name.getNetwork().getNodeCount()));
				contentPanel.repaint();
				contentPanel.revalidate();
			}
		});
		top.add(cbNetworks);

		mainPanel.add(top, BorderLayout.NORTH);

		return mainPanel;
	}
	
	public void update() {
		System.out.println("update combo box");
		cbNetworks.removeAllItems();
	    for(NetworkName s : getNetworks()) {
	    	cbNetworks.addItem(s);
	    }
		cbNetworks.repaint();
		cbNetworks.revalidate();
	}
	
	private Vector<NetworkName> getNetworks() {
		Vector<NetworkName> list = new Vector<NetworkName>();
		for(CyNetwork network : plugin.getCyNetworkManager().getNetworkSet()) {
			String name = network.getRow(network).get(CyNetwork.NAME, String.class);
			NetworkName nn = new NetworkName(name, network);
			list.add(nn);
		}
		return list;
	}

	@Override
	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.WEST;
	}

	@Override
	public Icon getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		return "CyTargetLinker";
	}
	

}
