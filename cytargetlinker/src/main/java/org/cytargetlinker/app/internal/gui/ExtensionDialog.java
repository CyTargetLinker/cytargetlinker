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
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.cytargetlinker.app.internal.CyTargetLinkerProperty;
import org.cytargetlinker.app.internal.Plugin;
import org.cytargetlinker.app.internal.data.DataSource;
import org.cytargetlinker.app.internal.data.DatasourceType;
import org.cytargetlinker.app.internal.data.Direction;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 * @author martina kutmon
 * dialog window for extending a network
 *
 */
public class ExtensionDialog extends JDialog {

	private Plugin plugin;
	private CellConstraints cc = new CellConstraints();
	private JComboBox networkComboBox;
	private JComboBox idAttribute;
	private JTextField dirField;
	private Map<String, File> rins;
	private JComboBox cbDirection;
	
	public ExtensionDialog(Plugin plugin) {
		super(plugin.getCySwingApplication().getJFrame());
        this.plugin = plugin;
        this.setLayout(new BorderLayout());
        this.add(getMainPanel());
        locate();
        this.setResizable(false);
        this.pack();
	}
	
	 private Component getMainPanel() {
		 FormLayout layout = new FormLayout("p", "p, 3dlu, p, 3dlu, p, 3dlu, p");
         PanelBuilder builder = new PanelBuilder(layout);
         builder.setDefaultDialogBorder();
         builder.add(getUserPanel(), cc.xy(1, 1));
         builder.add(getTargetPanel(), cc.xy(1, 3));
         builder.add(getSettings(), cc.xy(1,5));
         builder.add(getCancelOkPanel(), cc.xy(1, 7));
         return builder.getPanel();
	}

	private Component getCancelOkPanel() {
		FormLayout layout = new FormLayout("right:max(72dlu;p), 10dlu, 72dlu, 10dlu", "5dlu, p, 5dlu");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        JButton button1 = new JButton("Cancel");
        button1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                                dispose();
                }
        });
        builder.add(button1, cc.xy(1, 2));
        JButton button2 = new JButton("Ok");
        button2.addActionListener(new ActionListener(){
        	public void  actionPerformed(ActionEvent e) {
        		if(rins.isEmpty()) {
        			JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "No xgmml file in this Directory.", "Warning", JOptionPane.WARNING_MESSAGE);
        		} else {
        			NetworkName nn = (NetworkName) networkComboBox.getSelectedItem();
        			CyNetwork network = nn.getNetwork();
        			String idAtt = (String) idAttribute.getSelectedItem().toString();
        			
        			// TODO: check if attribute is valid
//        			if(!network.getDefaultNodeTable().getColumn(idAtt).getType().equals(String.class)) {
        				List<CyNode> nodes = network.getNodeList();
        				Direction dir = (Direction) cbDirection.getSelectedItem();
        				
        				List<DataSource> datasources = new ArrayList<DataSource>();
        				int count = 1;
        				for(File f : new File(dirField.getText()).listFiles()) {
        					if(f.getName().endsWith(".xgmml")) {
        						DataSource ds = new DataSource(DatasourceType.XGMML_FILE, f.getAbsolutePath());
        						ds.setSourceName(f.getName());
        						ds.setColor(new ColorSet().getColor(count));
        						datasources.add(ds);
        						count++;
        					}
        				}
        				dispose();
        				
        				RINSelectionDlg dlg = new RINSelectionDlg(plugin, network, idAtt, dir, nodes, datasources);
        				dlg.setVisible(true);
//        			} else {
//                        JOptionPane.showMessageDialog(plugin.getCySwingApplication().getJFrame(), "Invalid identifier attribute. Please specify a String attribute.", "Warning", JOptionPane.WARNING_MESSAGE);
//        			}
        		}
        	}    
        });
        builder.add(button2, cc.xy(3, 2));
        return builder.getPanel();
	}

	private Component getSettings() {
		FormLayout layout = new FormLayout("right:max(72dlu;p), 10dlu, 75dlu, 10dlu", "5dlu, p, 5dlu");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.setBorder(new TitledBorder("Settings"));
        
        Vector<Direction> vec = new Vector<Direction>();
        vec.add(Direction.BOTH);
        vec.add(Direction.SOURCE);
        vec.add(Direction.TARGET);
        cbDirection = new JComboBox(vec);
        cbDirection.setSelectedIndex(0);
        
        builder.add(new JLabel("Select direction"), cc.xy(1, 2));
        builder.add(cbDirection, cc.xy(3, 2));
        return builder.getPanel();
	}

	private Component getTargetPanel() {
		FormLayout layout = new FormLayout("right:max(72dlu;p), 10dlu, 75dlu, 5dlu, p, 10dlu", "5dlu, p, 10dlu");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.setBorder(new TitledBorder("Directory containg RINs"));
        builder.addLabel("Select RINs", cc.xy(1, 2));

        dirField = new JTextField();
        if(!CyTargetLinkerProperty.CTL_RIN_DIRECTORY.equals("")) {
        	dirField.setText(CyTargetLinkerProperty.CTL_RIN_DIRECTORY);
        	getRINFiles();
        }
        	
        dirField.setEditable(false);
        builder.add(dirField,cc.xy(3, 2));
        
        
        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(new ActionListener() { 
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		JFileChooser chooser = new JFileChooser();
        		chooser.setDialogTitle("Select directory containing RINs");
        		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        			File dir = chooser.getSelectedFile();
        			dirField.setText(dir.getAbsolutePath());
        			CyTargetLinkerProperty.updateProperty(dir.getAbsolutePath());
        			getRINFiles();
        		}
        	}
        });
        builder.add(browseButton, cc.xy(5, 2));
        
        return builder.getPanel();
	}
	
	private void getRINFiles() {
        rins = new HashMap<String, File>();
        File file = new File(dirField.getText());
        if (file != null){
        	File[] files = file.listFiles();
                
        	for (int i = 0; i < files.length; i++) {
        		if (files[i].isFile() && files[i].getName().contains(".xgmml")) {
        			rins.put(files[i].getAbsolutePath(), files[i]);
        		}
        	}
        }
	}

	private Component getUserPanel() {
		FormLayout layout = new FormLayout("right:max(72dlu;p), 10dlu, 75dlu, 10dlu", "5dlu, p, 5dlu, p, 5dlu, p, 15dlu");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.setBorder(new TitledBorder("User Network"));
        
        builder.addLabel("Select User Network", cc.xy(1, 2));
        Vector<NetworkName> vec = getNetworks();
        int current = 0;
        for(int i = 0; i < vec.size(); i++) {
        	if(plugin.getCyApplicationManager().getCurrentNetwork().equals(vec.get(i).getNetwork())) {
        		current = i;
        		break;
        	}
        }
        networkComboBox = new JComboBox(vec);
        networkComboBox.setSelectedIndex(current);
        ComboboxToolTipRenderer rendererNetwork = new ComboboxToolTipRenderer();
        networkComboBox.setRenderer(rendererNetwork);
        rendererNetwork.setTooltips(getTooltips(vec));
        networkComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				NetworkName nn = (NetworkName) networkComboBox.getSelectedItem();
				updateIdAttribute(nn.getNetwork());
			}
        });
        builder.add(networkComboBox, cc.xy(3, 2));
        
        builder.addLabel("Select your network attribute", cc.xy(1, 4));
        idAttribute = new JComboBox();
        updateIdAttribute(vec.get(current).getNetwork());
        builder.add(idAttribute, cc.xy(3, 4));
        
        return builder.getPanel();
	}
	
	private List<String> getTooltips(Vector<NetworkName> vec) {
		List<String> list = new ArrayList<String>();
		for(NetworkName nn : vec) {
			list.add(nn.getName());
		}
		return list;
	}

	private void updateIdAttribute(CyNetwork network) {
		idAttribute.removeAllItems();
		Collection<CyColumn> columns = network.getDefaultNodeTable().getColumns();
		List<String> list = new ArrayList<String>();
		for(CyColumn col : columns) {
			if(!col.getName().equals("SUID") && !col.getName().equals("selected")) {
				list.add(col.getName());
			}
		}
		Collections.sort(list);
		for(String str : list) {
			idAttribute.addItem(str);
		}
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

	private void locate() {
         int sizeX = 400;
         int sizeY = 400;
         this.setSize(sizeX, sizeY);

         Point cyLocation = plugin.getCySwingApplication().getJFrame().getLocation();
         int cyHeight = plugin.getCySwingApplication().getJFrame().getHeight();
         int cyWidth = plugin.getCySwingApplication().getJFrame().getWidth();

         Point middle = new Point(cyLocation.x + (cyWidth / 2), cyLocation.y + (cyHeight / 2));

         int locX = middle.x - (sizeX / 2);
         int locY = middle.y - (sizeY / 2);
         this.setLocation(new Point(locX, locY));
	 }
}
