package org.cytargetlinker.app.internal.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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

import org.cytargetlinker.app.internal.CTLManager;
import org.cytargetlinker.app.internal.data.Direction;
import org.cytargetlinker.app.internal.tasks.ExtendNetworkTaskFactory;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.swing.DialogTaskManager;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 * @author mkutmon
 * dialog to choose which LinkSets should be used
 *
 */
public class LinksetSelectionDlg extends JDialog {

	private Map<String, File> map;
	private List<File> linkSets;
	private List<File> selectedLinkSets;
	private Map<String, JCheckBox> cbMap;
	private CTLManager manager;
	private CyNetwork network;
	private String idAttribute;
	private Direction direction;
	
	private CellConstraints cc;
	
	public LinksetSelectionDlg(CTLManager manager, CyNetwork network, String idAttribute, Direction direction, String lsDir) {
		super(manager.getApplicationFrame());
		cc = new CellConstraints();
		map = new HashMap<String, File>();
		this.manager = manager;
		this.network = network;
		this.idAttribute = idAttribute;
		this.direction = direction;
		selectedLinkSets = new ArrayList<File>();
		linkSets = new ArrayList<File>();

		for(File f : new File(lsDir).listFiles()) {
			if(f.getName().endsWith(".xgmml")) {
				linkSets.add(f);
				map.put(f.getName(), f);
			}
		}

		cbMap = new HashMap<String, JCheckBox>();		
		
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
		builder.add(new JLabel("Linkset Files"), cc.xy(2, 2));
		builder.addSeparator("", cc.xyw(2, 3, 3));
		int i = 0;
		
		for(File f : linkSets) {
			builder.add(new JLabel(f.getName()), cc.xy(2, 2*i+5));
			JCheckBox box = new JCheckBox();
			box.setSelected(true);
			builder.add(box, cc.xy(4, 2*i+5));
			cbMap.put(f.getName(), box);
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
						selectedLinkSets.add(map.get(str));
					}
				}
				dispose();
				{
					ExtendNetworkTaskFactory fact = new ExtendNetworkTaskFactory(manager);
					TaskIterator it = fact.createTaskIterator(network, selectedLinkSets, idAttribute, direction, true);
					manager.getService(DialogTaskManager.class).execute(it);
				}
			}
		});
	
		builder.add(button2, cc.xy(3, 2));
		return builder.getPanel();
	}
	
	private String getRowLayout() {
		String str = "10dlu, p, 15dlu, 5dlu";
		for(int i = 0; i < linkSets.size(); i++) {
			str = str + ",p,5dlu";
		}
		str = str + ",10dlu";
		return str;
	}
	
	private void locate() {
		int sizeX = 400;
		int sizeY = 400;
		this.setPreferredSize(new Dimension(sizeX, sizeY));

		Point cyLocation = manager.getApplicationFrame().getLocation();
		int cyHeight = manager.getApplicationFrame().getHeight();
		int cyWidth = manager.getApplicationFrame().getWidth();

		Point middle = new Point(cyLocation.x + (cyWidth / 2), cyLocation.y + (cyHeight / 2));

		int locX = middle.x - (sizeX / 2);
		int locY = middle.y - (sizeY / 2);
		this.setLocation(new Point(locX, locY));
	}
}