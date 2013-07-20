package com.c2.pandoraserver;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class ParameterTableModel extends DefaultTableModel {
	public String columnNames[] = { "Parameter", "Value" };
	public Vector<String> parameters = new Vector<String>();
	public Vector<Object> values = new Vector<Object>();

	public ParameterTableModel() {
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
		if (parameters == null) {
			return 0;
		}
		return parameters.size();
	}

	public void setValueAt(Object aValue, int row, int column) {
		if (column == 1) {
			values.remove(row);
			values.insertElementAt((String) aValue, row);
		}
		this.fireTableDataChanged();
	}

	public Object getValueAt(final int row, int column) {
		if (column == 0) {
			return parameters.get(row);
		} else {
			return values.get(row);
		}
	}

	public Object getValue(final String key) {
		for (int x = 0; x < parameters.size(); x++) {
			if (parameters.get(x).equals(key)) {
				return values.get(x);
			}
		}
		return null;
	}

	public boolean isCellEditable(int row, int column) {
		if (row == 0 && column == 1) {
			return false;
		} else if (column == 1) {
			return true;
		} else {
			return false;
		}
	}

	public Class getColumnClass(int columnIndex) {
		if (getValueAt(0, columnIndex) == null) {
			return Object.class;
		}
		return getValueAt(0, columnIndex).getClass();
	}

}
