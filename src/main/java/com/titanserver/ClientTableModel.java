package com.titanserver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

public class ClientTableModel extends DefaultTableModel {
	String columnNames[] = { "Client", "Status" };
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			//			for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.anyJobGroup())) {
			//				String jobName = jobKey.getName();
			//				String jobGroup = jobKey.getGroup();
			//				List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
			//				Date nextFireTime = triggers.get(0).getNextFireTime();
			//				System.out.println("[jobName] : " + jobName + " [groupName] : " + jobGroup + " - " + nextFireTime);
			//			}
			return scheduler.getJobKeys(GroupMatcher.anyJobGroup()).size();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void setValueAt(Object aValue, int row, int column) {
		this.fireTableDataChanged();
	}

	public Object getValueAt(final int row, int column) {
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			int x = 0;
			for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.anyJobGroup())) {
				String jobName = jobKey.getName();
				String jobGroup = jobKey.getGroup();
				List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
				Date nextFireTime = triggers.get(0).getNextFireTime();
				//System.out.println("[jobName] : " + jobName + " [groupName] : " + jobGroup + " - " + nextFireTime);
				if (row == x) {
					if (column == 0) {
						return jobName;
					} else {
						return sf.format(nextFireTime);
					}
				}
				x++;
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return "";
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
