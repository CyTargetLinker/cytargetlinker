package org.cytargetlinker.app.internal.ui;

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
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.cytargetlinker.app.internal.CTLManager;
import org.cytargetlinker.app.internal.data.Direction;
import org.cytargetlinker.app.internal.model.CyTargetLinkerProperty;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyNetwork;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 * @author mkutmon
 * dialog window for extending a network
 *
 */
public class ExtensionDialog extends JDialog {

	private CTLManager manager;
	private Map<String, CyNetwork> networks;

	private CellConstraints cc = new CellConstraints();
	private JComboBox<String> cbNet;
	private JComboBox<String> idAttribute;
	private JTextField dirField;
	private Map<String, File> linkSets;
	private JComboBox<Direction> cbDirection;
	
	public ExtensionDialog(CTLManager manager) {
		super(manager.getApplicationFrame());
        this.manager = manager;
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
        		if(linkSets.isEmpty()) {
        			JOptionPane.showMessageDialog(manager.getApplicationFrame(), "No xgmml file in this Directory.", "Warning", JOptionPane.WARNING_MESSAGE);
        		} else {
        			String netName = (String) cbNet.getSelectedItem();
        			if(netName != null && !netName.equals("Select Network")) {
	        			CyNetwork network = networks.get(netName);
	        			String idAtt = (String) idAttribute.getSelectedItem().toString();
	        			
	        			Direction dir = (Direction) cbDirection.getSelectedItem();
	        			
	        			if(new File(dirField.getText()).exists()) {
	        				int count = 0;
	        				for(File f : new File(dirField.getText()).listFiles()) {
	        					if(f.getName().endsWith(".xgmml")) {
	        						count++;
	        					}
	        				}
	        				if(count > 0) {
		        				dispose();
		        				LinksetSelectionDlg dlg = new LinksetSelectionDlg(manager, network, idAtt, dir, dirField.getText());
			        			dlg.setVisible(true);
	        				} else {
	        					JOptionPane.showMessageDialog(manager.getApplicationFrame(), "LinkSet directory does not contain any LinkSet files (XGMML files).", "Error", JOptionPane.ERROR_MESSAGE);
	        				}
	        			} else {
	        				JOptionPane.showMessageDialog(manager.getApplicationFrame(), "LinkSet directory does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
	        			}
        			} else {
        				JOptionPane.showMessageDialog(manager.getApplicationFrame(), "Please select a valid network.", "Error", JOptionPane.ERROR_MESSAGE);
        			}
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
        vec.add(Direction.SOURCES);
        vec.add(Direction.TARGETS);
        cbDirection = new JComboBox<Direction>(vec);
        cbDirection.setSelectedIndex(0);
        
        builder.add(new JLabel("Select direction"), cc.xy(1, 2));
        builder.add(cbDirection, cc.xy(3, 2));
        return builder.getPanel();
	}

	private Component getTargetPanel() {
		FormLayout layout = new FormLayout("right:max(72dlu;p), 10dlu, 75dlu, 5dlu, p, 10dlu", "5dlu, p, 10dlu");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.setBorder(new TitledBorder("Directory containg Link Sets"));
        builder.addLabel("Select Link Sets", cc.xy(1, 2));

        dirField = new JTextField();
        if(!CyTargetLinkerProperty.CTL_LINKSET_DIR.equals("")) {
        	dirField.setText(CyTargetLinkerProperty.CTL_LINKSET_DIR);
        	getRegINFiles();
        }
        	
        dirField.setEditable(false);
        builder.add(dirField,cc.xy(3, 2));
        
        
        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(new ActionListener() { 
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		JFileChooser chooser = new JFileChooser();
        		chooser.setDialogTitle("Select directory containing RegINs");
        		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        			File dir = chooser.getSelectedFile();
        			dirField.setText(dir.getAbsolutePath());
        			CyTargetLinkerProperty.updateProperty(dir.getAbsolutePath());
        			getRegINFiles();
        		}
        	}
        });
        builder.add(browseButton, cc.xy(5, 2));
        
        return builder.getPanel();
	}
	
	private void getRegINFiles() {
        linkSets = new HashMap<String, File>();
        File file = new File(dirField.getText());
        if (file != null){
        	File[] files = file.listFiles();
                
        	for (int i = 0; i < files.length; i++) {
        		if (files[i].isFile() && files[i].getName().contains(".xgmml")) {
        			linkSets.put(files[i].getAbsolutePath(), files[i]);
        		}
        	}
        }
	}

	private JComboBox<String> fillCbNet() {
		networks = new HashMap<String, CyNetwork>();
		cbNet = new JComboBox<String>();
		cbNet.addItem("Select Network");
		Set<CyNetwork> nets = manager.getNetworkManager().getNetworkSet();
		for(CyNetwork n : nets) {
			String name = n.getRow(n).get("name", String.class);
			networks.put(name + " (SUID:" + n.getSUID() + ")", n);
			cbNet.addItem(name + " (SUID:" + n.getSUID() + ")");
		}
		return cbNet;
	}
	
	private Component getUserPanel() {
		FormLayout layout = new FormLayout("right:max(72dlu;p), 10dlu, 75dlu, 10dlu", "5dlu, p, 5dlu, p, 5dlu, p, 15dlu");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.setBorder(new TitledBorder("User Network"));
        
        builder.addLabel("Select User Network", cc.xy(1, 2));
        
        JComboBox<String> cbNet = fillCbNet();
        cbNet.setRenderer(new ComboboxToolTipRenderer());
        cbNet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nn = (String) cbNet.getSelectedItem();
				if(!nn.equals("Select Network")) {
					updateIdAttribute(networks.get(nn));
				}
			}
        });
        builder.add(cbNet, cc.xy(3, 2));
        
        builder.addLabel("Select your network attribute", cc.xy(1, 4));
        idAttribute = new JComboBox<String>();
        updateIdAttribute(null);
        builder.add(idAttribute, cc.xy(3, 4));
        
        return builder.getPanel();
	}

	private void updateIdAttribute(CyNetwork network) {
		idAttribute.removeAllItems();
		if(network != null) {
			Collection<CyColumn> columns = network.getDefaultNodeTable().getColumns();
			List<String> list = new ArrayList<String>();
			for(CyColumn col : columns) {
				if(!col.getName().equals("SUID") && !col.getName().equals("selected") && !col.getName().equals("name")) {
					list.add(col.getName());
				}
			}
			Collections.sort(list);
			for(String str : list) {
				idAttribute.addItem(str);
			}
		}
	}
	
//	private Vector<NetworkName> getNetworks() {
//		Vector<NetworkName> list = new Vector<NetworkName>();
//		for(CyNetwork network : plugin.getCyNetworkManager().getNetworkSet()) {
//			String name = network.getRow(network).get(CyNetwork.NAME, String.class);
//			NetworkName nn = new NetworkName(name, network);
//			list.add(nn);
//		}
//		return list;
//	}

	private void locate() {
         int sizeX = 400;
         int sizeY = 400;
         this.setSize(sizeX, sizeY);

         Point cyLocation = manager.getApplicationFrame().getLocation();
         int cyHeight = manager.getApplicationFrame().getHeight();
         int cyWidth = manager.getApplicationFrame().getWidth();

         Point middle = new Point(cyLocation.x + (cyWidth / 2), cyLocation.y + (cyHeight / 2));

         int locX = middle.x - (sizeX / 2);
         int locY = middle.y - (sizeY / 2);
         this.setLocation(new Point(locX, locY));
	 }
}