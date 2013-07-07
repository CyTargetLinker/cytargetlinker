/**
 * copied from http://stackoverflow.com/questions/480261/java-swing-mouseover-text-on-jcombobox-items
 * answer by MountainX
 */

package org.cytargetlinker.app.internal.gui;

import java.awt.Component;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;

public class ComboboxToolTipRenderer extends DefaultListCellRenderer {
	
    private List<String> tooltips;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        JComponent comp = (JComponent) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (-1 < index && null != value && null != tooltips) {
        	list.setToolTipText(tooltips.get(index));
        }
        return comp;
    }

    public void setTooltips(List<String> tooltips) {
        this.tooltips = tooltips;
    }
}