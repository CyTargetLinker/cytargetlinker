package org.cytargetlinker.app.internal.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.cytargetlinker.app.internal.ExtensionManager;
import org.cytargetlinker.app.internal.Plugin;
import org.cytargetlinker.app.internal.Utils;
import org.cytargetlinker.app.internal.data.Result;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.util.swing.CyColorChooser;
import org.cytoscape.view.model.CyNetworkView;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class CyTargetLinkerPanel extends JPanel implements CytoPanelComponent {

	private JPanel contentPanel;
	private JPanel mainPanel;
	private Plugin plugin;
	private JComboBox cbNetworks;
	private JSpinner thresholdSpinner;
	
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
		top.setBackground(Color.WHITE);
		top.setLayout(new GridLayout(3, 2));
		top.add(new JLabel());top.add(new JLabel());
		top.add(new JLabel("Select extended network:"));
		cbNetworks = new JComboBox(getNetworks());
		contentPanel = new JPanel();
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		cbNetworks.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				NetworkName name = (NetworkName) cbNetworks.getSelectedItem();
				System.out.println(name.getName() + "\t" + name.getNetwork());
				updateContentPanel(name);
			}
		});
		top.add(cbNetworks);
		top.add(new JLabel());top.add(new JLabel());
		mainPanel.add(top, BorderLayout.NORTH);

		return mainPanel;
	}
	
	private void updateContentPanel(NetworkName name) {
		ExtensionManager mgr = plugin.getManagers().get(name.getNetwork());
		contentPanel.removeAll();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(getDataColorPanel(mgr), BorderLayout.CENTER);
		
//		contentPanel.add(new JLabel(name.getName() + "\t" + name.getNetwork().getSUID()));
//		contentPanel.add(new JLabel(name.getNetwork().getEdgeCount() + "\t" + name.getNetwork().getNodeCount()));
		contentPanel.repaint();
		contentPanel.revalidate();
	}
	
	private Component getDataColorPanel(ExtensionManager mgr) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.WHITE);
		
		panel.add(fillPanel(mgr), BorderLayout.NORTH);
		
		JScrollPane pane = new JScrollPane(panel);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		return pane;
	}

	private Component fillPanel(final ExtensionManager mgr) {
		CellConstraints cc = new CellConstraints();
		String rowLayout = "5dlu, pref, 5dlu, pref, 15dlu";
		System.out.println("rowLayout start");
		for(int i = 0; i < mgr.getHistory().size(); i++) {
			rowLayout = rowLayout + ", pref, 10dlu, pref, 10dlu";
			for(int j = 0; j < mgr.getHistory().get(i).getResults().size(); j++) {
				rowLayout = rowLayout + ",p,5dlu";
			}
		}
		rowLayout = rowLayout + ", 15dlu, p, 5dlu";
		System.out.println("rowLayout end");
		FormLayout layout = new FormLayout("pref,10dlu, pref, 10dlu, 30dlu, 10dlu, pref", rowLayout);
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		builder.setBackground(Color.WHITE);
		
		SpinnerModel model = new SpinnerNumberModel(1, 1, 100, 1);
		thresholdSpinner = new JSpinner(model);
		thresholdSpinner.setValue(1);
		thresholdSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				Integer i = (Integer) thresholdSpinner.getValue();
				System.out.println("apply threshold " + i);
				// TODO apply threshold
				// update view
			}
		});
		
		builder.addLabel("Overlap threshold:", cc.xy(1, 2));
		builder.add(thresholdSpinner, cc.xy(3, 2));
		
		builder.addSeparator("", cc.xyw(1, 5, 5));
		
		int rowCount = 6;
		for(int i = 0; i < mgr.getHistory().size(); i++) {
			builder.addLabel("<html><b>Step " + mgr.getHistory().get(i).getStepNum() + "</b></html>", cc.xy(1, rowCount));
			rowCount = rowCount+2;
			
			//create a column with the name of the database found in the file selected
	        builder.addLabel("RIN", cc.xy(1, rowCount));
	        builder.addLabel("#", cc.xy(3,rowCount));
	        builder.addLabel("Color", cc.xy(5, rowCount)); 
	        builder.addLabel("Show/Hide", cc.xy(7, rowCount));
	        builder.addSeparator("", cc.xyw(1,rowCount+1,5));
	        rowCount = rowCount+2;
	        
	        System.out.println("Result size for step " + mgr.getHistory().get(i).getStepNum() + " = " + mgr.getHistory().get(i).getResults().size());
	        
	        for(int j = 0; j < mgr.getHistory().get(i).getResults().size(); j++) {
	        	final Result r = mgr.getHistory().get(i).getResults().get(j);
	        	builder.addLabel(mgr.getHistory().get(i).getResults().get(j).getRinName(), cc.xy(1, rowCount));
	        	
	        	JLabel field = new JLabel(r.getEdges().size() + "");
                builder.add(field, cc.xy(3, rowCount));
                
                final JButton button = new JButton();
                button.setBackground(r.getDs().getColor());
                button.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						Color c = CyColorChooser.showDialog(plugin.getCySwingApplication().getJFrame(), "Select color for " + r.getRinName(), r.getDs().getColor());
						r.getDs().setColor(c);
						button.setBackground(c);
						Collection<CyNetworkView> views = plugin.getCyNetworkViewManager().getNetworkViews(mgr.getNetwork());
						CyNetworkView view;
						if(!views.isEmpty()) {
							view = views.iterator().next();
						} else {
							view = plugin.getNetworkViewFactory().createNetworkView(mgr.getNetwork());
							plugin.getCyNetworkViewManager().addNetworkView(view);
						}
						Utils.updateVisualStyle(plugin, view, mgr.getNetwork());
					}
				});
                
	        	builder.add(button, cc.xy(5, rowCount));
	        	
	        	String[] show = { "Show", "Hide" };
                JComboBox box = new JComboBox(show);
                box.setSelectedIndex(0);
				box.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent itemEvent) {
						JComboBox source = (JComboBox) itemEvent.getSource();
						if (source.getSelectedItem().equals("Show")) {
							// TODO "show network"
							System.out.println("Show:\t" + r.getRinName());
						} else {
							// TODO "hide network"
							System.out.println("Hide:\t" + r.getRinName());
						}
					}

				});
                
                builder.add(box, cc.xy(7, rowCount));
	        	
	        	rowCount = rowCount+2;
	        }
		}
		
        
//        int i = 0;
        
        
		
		return builder.getPanel();
	}

	public void update() {
		cbNetworks.removeAllItems();
	    for(NetworkName s : getNetworks()) {
	    	cbNetworks.addItem(s);
	    }
		cbNetworks.repaint();
		cbNetworks.revalidate();
	}
	
	private Vector<NetworkName> getNetworks() {
		Vector<NetworkName> list = new Vector<NetworkName>();
		
		for(CyNetwork network : plugin.getManagers().keySet()) {
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
		return null;
	}

	@Override
	public String getTitle() {
		return "CyTargetLinker";
	}
	

}
