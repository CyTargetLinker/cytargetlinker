package org.cytargetlinker.app.internal.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.cytargetlinker.app.internal.CTLManager;
import org.cytargetlinker.app.internal.data.LinkSet;
import org.cytargetlinker.app.internal.model.ExtensionManager;
import org.cytargetlinker.app.internal.tasks.ApplyVisualStyleTaskFactory;
import org.cytargetlinker.app.internal.tasks.FilterOverlapTaskFactory;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CytoPanelComponent2;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.events.NetworkAddedEvent;
import org.cytoscape.model.events.NetworkAddedListener;
import org.cytoscape.model.events.NetworkDestroyedEvent;
import org.cytoscape.model.events.NetworkDestroyedListener;
import org.cytoscape.model.events.RowsCreatedEvent;
import org.cytoscape.model.events.RowsCreatedListener;
import org.cytoscape.session.events.SessionLoadedEvent;
import org.cytoscape.session.events.SessionLoadedListener;
import org.cytoscape.util.swing.CyColorChooser;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.swing.DialogTaskManager;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


public class CTLCytoPanel extends JPanel implements CytoPanelComponent2, NetworkDestroyedListener, NetworkAddedListener, SessionLoadedListener, RowsCreatedListener {

	private JPanel topPanel;
	private JPanel bottomPanel;
	private CTLManager manager;
	private Map<String, CyNetwork> networks;
	private JSpinner thresholdSpinner;
	
	public CTLCytoPanel(CTLManager manager) {
		this.manager = manager;
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		topPanel = new JPanel();
		redrawTopPanel();
		this.add(topPanel, BorderLayout.NORTH);
		
		bottomPanel = new JPanel();
		redrawBottomPanel(null);
		this.add(bottomPanel, BorderLayout.CENTER);
		
		this.revalidate();
		this.repaint();
	}
	
	private void addNetworkList() {
		CyNetwork network = manager.getCurrentNetwork();
		if(network == null) {
			topPanel.add(new JLabel("No networks loaded."));
			return;
		}
		
		JComboBox<String> cbNet = fillCbNet();
		if(cbNet.getItemCount() == 1) {
			topPanel.add(new JLabel("No extended CyTargetLinker networks loaded."));
			return;
		}
		JLabel label = new JLabel("Select extended network:");
		Font newLabelFont=new Font(label.getFont().getName(),Font.BOLD,label.getFont().getSize()+2);
		label.setFont(newLabelFont);
		topPanel.add(label,BorderLayout.NORTH);
		topPanel.add(cbNet, BorderLayout.CENTER);
		cbNet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String netName = (String) cbNet.getSelectedItem();
				if(netName != null && !netName.equals("Select Network")) {	
					manager.getService(CyApplicationManager.class).setCurrentNetworkView(manager.getNetworkView(networks.get(netName)));;
					redrawBottomPanel(networks.get(netName));
				} else if (netName.equals("Select Network")) {
					redrawBottomPanel(null);
				}
			}
		});
	}
	
	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public CytoPanelName getCytoPanelName() {
		return CytoPanelName.EAST;
	}
	
	private void redrawBottomPanel(CyNetwork net) {
		if(net == null) {
			bottomPanel.removeAll();
			bottomPanel.setBackground(Color.WHITE);
		} else {
			bottomPanel.removeAll();
			bottomPanel.setBackground(Color.WHITE);
			bottomPanel.setLayout(new BorderLayout());
			ExtensionManager mgr = manager.getExtensionManager(net);
			bottomPanel.add(getDataColorPanel(mgr), BorderLayout.CENTER);
		}

		bottomPanel.repaint();
		bottomPanel.revalidate();
	}
	
	private Component getDataColorPanel(ExtensionManager mgr) {
		System.out.println("DRAW DATA COLOR PANEL");
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.WHITE);
		
		JPanel content = fillPanel(mgr);
		content.setBackground(Color.white);
		panel.add(content, BorderLayout.NORTH);
		
		JScrollPane pane = new JScrollPane(panel);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		return pane;
	}
	
	/**
	 * creates content of CTL panel for one extended network
	 */
	private JPanel fillPanel(final ExtensionManager mgr) {
		CellConstraints cc = new CellConstraints();
		String rowLayout = "5dlu, pref, 5dlu, pref, 15dlu";

		for(int i = 0; i < mgr.getLinkSets().size(); i++) {
			rowLayout = rowLayout + ",p,5dlu";
		}

		rowLayout = rowLayout + ", 15dlu, p, 5dlu";
		
		FormLayout layout = new FormLayout("pref,10dlu, pref, 10dlu, pref, 10dlu, pref", rowLayout);
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		SpinnerModel model = new SpinnerNumberModel(1, 1, 100, 1);
		thresholdSpinner = new JSpinner(model);
		thresholdSpinner.setValue(mgr.getThreshold());
		thresholdSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				Integer i = (Integer) thresholdSpinner.getValue();
				FilterOverlapTaskFactory fact = new FilterOverlapTaskFactory(manager);
				TaskIterator it = fact.createTaskIterator(mgr.getNetwork(), i);
				manager.getService(DialogTaskManager.class).execute(it);
				redrawBottomPanel(mgr.getNetwork());
			}
		});
		
		builder.addLabel("Overlap threshold:", cc.xy(1, 2));
		builder.add(thresholdSpinner, cc.xy(3, 2));
		
		builder.addSeparator("", cc.xyw(1, 5, 7));
		
		int rowCount = 6;
		
		builder.addLabel("LinkSet", cc.xy(1, rowCount));
		builder.addLabel("#", cc.xy(3,rowCount));
	    builder.addLabel("Color", cc.xy(5, rowCount)); 
	    builder.addLabel("Show/Hide", cc.xy(7, rowCount));
	    builder.addSeparator("", cc.xyw(1,rowCount+1,7));
	    
	    rowCount = rowCount+2;
	    Collections.sort(mgr.getLinkSets());
		for(int i = 0; i < mgr.getLinkSets().size(); i++) {
			final LinkSet ls = mgr.getLinkSets().get(i);
		    builder.addLabel(ls.getName(), cc.xy(1, rowCount));
		    		    
		    int visible = ls.getEdges().size() - ls.getHiddenEdges().size();
		    JLabel field = new JLabel(visible + "/" + ls.getEdges().size());
            builder.add(field, cc.xy(3, rowCount));
             
            final JButton button = new JButton();
            button.setBackground(ls.getColor());
            button.setContentAreaFilled(true);
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
            	@Override
				public void actionPerformed(ActionEvent arg0) {
            		Color c = CyColorChooser.showDialog(manager.getApplicationFrame(), "Select color for " + ls.getName(), ls.getColor());
            		ls.setColor(c);
					button.setBackground(c);
					button.setOpaque(true);
										
					{
						ApplyVisualStyleTaskFactory fact = new ApplyVisualStyleTaskFactory(manager);
						TaskIterator it = fact.createTaskIterator(mgr.getNetwork());
						manager.getService(DialogTaskManager.class).execute(it);
					}
				}
			});
             
	        builder.add(button, cc.xy(5, rowCount));
	        	
	        String[] show = { "Show", "Hide" };
            JComboBox<String> box = new JComboBox<String>(show);
            if(ls.isShow()) {
            	box.setSelectedIndex(0);
            } else {
            	box.setSelectedIndex(1);
            }
            box.addActionListener(new ActionListener() {
            	@Override
				public void actionPerformed(ActionEvent event) {
					@SuppressWarnings("unchecked")
					JComboBox<String> source = (JComboBox<String>) event.getSource();
						
					if (source.getSelectedItem().equals("Show")) {
						ls.setShow(true);
					} else {
						ls.setShow(false);
					}
					
					{					
						FilterOverlapTaskFactory fact = new FilterOverlapTaskFactory(manager);
						TaskIterator it = fact.createTaskIterator(mgr.getNetwork(), mgr.getThreshold());
						manager.getService(DialogTaskManager.class).execute(it);
						
						redrawBottomPanel(mgr.getNetwork());
					}
				}
			});
             
            builder.add(box, cc.xy(7, rowCount));
	        	
	        rowCount = rowCount+2;
		}
		
		return builder.getPanel();
	}

	private void redrawTopPanel() {
		topPanel.removeAll();
		topPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
		topPanel.setBackground(Color.WHITE);
		topPanel.setLayout(new BorderLayout());
		addNetworkList();
	}
	
	@Override
	public Icon getIcon() {
		return null;
	}

	@Override
	public String getTitle() {
		return "CyTargetLinker";
	}

	@Override
	public String getIdentifier() {
		return "org.cytargetlinker.app";
	}

	@Override
	public void handleEvent(NetworkAddedEvent arg0) {		
		redrawTopPanel();
		redrawBottomPanel(null);
		
		this.revalidate();
		this.repaint();
	}

	@Override
	public void handleEvent(NetworkDestroyedEvent arg0) {		
		redrawTopPanel();
		redrawBottomPanel(null);

		this.revalidate();
		this.repaint();
	}
	
	private JComboBox<String> fillCbNet() {
		networks = new HashMap<String, CyNetwork>();
		JComboBox<String> cbNet = new JComboBox<String>();
        cbNet.setRenderer(new ComboboxToolTipRenderer());
		cbNet.addItem("Select Network");
		Set<CyNetwork> nets = manager.getNetworkManager().getNetworkSet();
		for(CyNetwork n : nets) {
			if(manager.isCTLNetwork(n)) {
				String name = n.getRow(n).get("name", String.class);
				networks.put(name + " (SUID:" + n.getSUID() + ")", n);
				cbNet.addItem(name + " (SUID:" + n.getSUID() + ")");
			}
		}
		return cbNet;
	}
	
	@Override
	public void handleEvent(SessionLoadedEvent arg0) {		
		redrawTopPanel();
		redrawBottomPanel(null);
		
		this.revalidate();
		this.repaint();
	}

	@Override
	public void handleEvent(RowsCreatedEvent arg0) {		
		redrawTopPanel();
		redrawBottomPanel(null);
		
		this.revalidate();
		this.repaint();
	}
}
