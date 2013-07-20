package com.titanserver;

import javax.swing.table.DefaultTableModel;

public class TitanCommandTableModel extends DefaultTableModel {
	String columnNames[] = { "Titan command" };
	String values[] = { "get screen permissions", "get instance permissions", "get users", "get screen permission groups", "get instance permission groups" };

	public TitanCommandTableModel() {
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	public int getColumnCount() {
		if (columnNames == null) {
			return 0;
		}
		return columnNames.length;
	}

	public int getRowCount() {
		if (values == null) {
			return 0;
		}
		return values.length;
	}

	public void setValueAt(Object aValue, int row, int column) {
		this.fireTableDataChanged();
	}

	public Object getValueAt(final int row, int column) {
		return values[row];
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public Class getColumnClass(int columnIndex) {
		if (getValueAt(0, columnIndex) == null) {
			return Object.class;
		}
		return getValueAt(0, columnIndex).getClass();
	}

}
