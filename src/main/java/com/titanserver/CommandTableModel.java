package com.titanserver;

import java.util.Map;

import javax.swing.table.DefaultTableModel;

public class CommandTableModel extends DefaultTableModel {
	String columnNames[] = { "Nova command", "Json" };

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
		return TitanServerSetting.getInstance().novaCommands.size() + 4;
	}

	public void setValueAt(Object aValue, int row, int column) {
		this.fireTableDataChanged();
	}

	public Object getValueAt(final int row, int column) {
		if (row == 0) {
			if (column == 0) {
				return "Os username";
			} else {
				return TitanServerSetting.getInstance().novaOsUsername;
			}
		} else if (row == 1) {
			if (column == 0) {
				return "Os password";
			} else {
				return TitanServerSetting.getInstance().novaOsPassword;
			}
		} else if (row == 2) {
			if (column == 0) {
				return "Os tenant name";
			} else {
				return TitanServerSetting.getInstance().novaOsTenantName;
			}
		} else if (row == 3) {
			if (column == 0) {
				return "Os auth url";
			} else {
				return TitanServerSetting.getInstance().novaOsAuthUrl;
			}
		} else {
			Map<String, String> novaCommands = TitanServerSetting.getInstance().novaCommands;
			Object keys[] = novaCommands.keySet().toArray();
			if (column == 0) {
				return keys[row - 4];
			} else if (column == 1) {
				return novaCommands.get(keys[row - 4]);
			} else {
				return "";
			}
		}
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
