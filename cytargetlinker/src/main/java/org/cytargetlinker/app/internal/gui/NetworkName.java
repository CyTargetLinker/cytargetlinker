package org.cytargetlinker.app.internal.gui;

import org.cytoscape.model.CyNetwork;

public class NetworkName {

	private String name;
	private CyNetwork network;

	public NetworkName(String name, CyNetwork network) {
		this.name = name;
		this.network = network;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CyNetwork getNetwork() {
		return network;
	}
	public void setNetwork(CyNetwork network) {
		this.network = network;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
