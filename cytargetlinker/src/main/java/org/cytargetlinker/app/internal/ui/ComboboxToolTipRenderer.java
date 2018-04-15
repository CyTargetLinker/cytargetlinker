// CyTargetLinker,
package org.cytargetlinker.app.internal.ui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;

/**
 * 
 * copied from http://stackoverflow.com/questions/480261/java-swing-mouseover-text-on-jcombobox-items
 * answer by MountainX
 * adds a tooltip for the elements in a dropdown box in case the name is not completely shown
 *
 */

public class ComboboxToolTipRenderer extends DefaultListCellRenderer {
	

    @SuppressWarnings("rawtypes")
	@Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        JComponent comp = (JComponent) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (-1 < index && null != value) {
        	list.setToolTipText((String)value);
        }
        return comp;
    }
}