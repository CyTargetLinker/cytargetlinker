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
package org.cytargetlinker.app.internal.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.cytargetlinker.app.internal.Plugin;
import org.cytargetlinker.app.internal.data.DataSource;
import org.cytargetlinker.app.internal.data.Direction;
import org.cytargetlinker.app.internal.tasks.ExtensionTaskFactory;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.work.TaskIterator;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 * @author mkutmon
 * dialog to choose which RegINs should be used
 *
 */
public class RegINSelectionDlg extends JDialog {

	private Map<String, DataSource> networkFiles;
	private List<DataSource> selectedDs;
	private Map<String, JCheckBox> cbMap;
	private Plugin plugin;
	private CyNetwork network;
	private String idAttribute;
	private Direction direction;
	private List<CyNode> nodes;
	
	private CellConstraints cc = new CellConstraints();
	
	public RegINSelectionDlg(Plugin plugin, CyNetwork network, String idAttribute, Direction direction, List<CyNode> nodes, List<DataSource> datasources) {
		super(plugin.getCySwingApplication().getJFrame());
		this.plugin = plugin;
		this.network = network;
		this.idAttribute = idAttribute;
		this.direction = direction;
		this.nodes = nodes;
		selectedDs = new ArrayList<DataSource>();
		networkFiles = new HashMap<String, DataSource>();
		cbMap = new HashMap<String, JCheckBox>();
		
		for(DataSource ds : datasources) {
			networkFiles.put(ds.getSourceName(), ds);
		}
		
		this.add(getContent());
		locate();
		this.setResizable(false);
		this.pack();
	}
	
	private JPanel getContent() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new BorderLayout(1,1));

		panel.add(getMainPanel(), BorderLayout.CENTER);
		panel.add(getCancelOkPanel(), BorderLayout.SOUTH);
		return panel;
	}
	
	private Component getMainPanel() {
		FormLayout layout = new FormLayout("10dlu, p, 5dlu, p, 10dlu", getRowLayout());
		PanelBuilder builder = new PanelBuilder(layout);
		builder.add(new JLabel("RegIN Files"), cc.xy(2, 2));
		builder.addSeparator("", cc.xyw(2, 3, 3));
		int i = 0;
		
		for(String str : networkFiles.keySet()) {
			builder.add(new JLabel(networkFiles.get(str).getSourceName()), cc.xy(2, 2*i+5));
			JCheckBox box = new JCheckBox();
			box.setSelected(true);
			builder.add(box, cc.xy(4, 2*i+5));
			cbMap.put(str, box);
			i++;
		}

		JScrollPane pane = new JScrollPane(builder.getPanel());
		return pane;
	}
	
	private JPanel getCancelOkPanel() {
		FormLayout layout = new FormLayout("right:max(72dlu;p), 10dlu, 72dlu, 10dlu", "5dlu, p, 5dlu");
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		JButton button1 = new JButton("Cancel");
		button1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});

		builder.add(button1, cc.xy(1, 2));
		JButton button2 = new JButton("Ok");
		button2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(String str : cbMap.keySet()) {
					if(cbMap.get(str).isSelected()) {
						selectedDs.add(networkFiles.get(str));
					}
				}
				dispose();
				ExtensionTaskFactory factory = new ExtensionTaskFactory(plugin, network, idAttribute, direction, nodes, selectedDs);
				TaskIterator it = factory.createTaskIterator();
				plugin.getDialogTaskManager().execute(it);
			}
		});
	
		builder.add(button2, cc.xy(3, 2));
		return builder.getPanel();
	}
	
	private String getRowLayout() {
		String str = "10dlu, p, 15dlu, 5dlu";
		for(int i = 0; i < networkFiles.size(); i++) {
			str = str + ",p,5dlu";
		}
		str = str + ",10dlu";
		return str;
	}
	
	private void locate() {
		int sizeX = 400;
		int sizeY = 400;
		this.setPreferredSize(new Dimension(sizeX, sizeY));

		Point cyLocation = plugin.getCySwingApplication().getJFrame().getLocation();
		int cyHeight = plugin.getCySwingApplication().getJFrame().getHeight();
		int cyWidth = plugin.getCySwingApplication().getJFrame().getWidth();

		Point middle = new Point(cyLocation.x + (cyWidth / 2), cyLocation.y + (cyHeight / 2));

		int locX = middle.x - (sizeX / 2);
		int locY = middle.y - (sizeY / 2);
		this.setLocation(new Point(locX, locY));
	}
}
