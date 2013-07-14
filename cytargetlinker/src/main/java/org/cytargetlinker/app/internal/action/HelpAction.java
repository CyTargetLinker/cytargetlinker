package org.cytargetlinker.app.internal.action;

import java.awt.event.ActionEvent;

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.util.swing.OpenBrowser;

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
