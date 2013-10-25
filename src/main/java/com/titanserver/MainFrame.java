package com.titanserver;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.peterswing.CommonLib;
import com.peterswing.advancedswing.enhancedtextarea.EnhancedTextArea;
import com.peterswing.advancedswing.jtable.SortableTableModel;
import com.peterswing.advancedswing.jtable.TableSorterColumnListener;
import com.titanserver.openstack_communication.OpenstackComm;
import java.awt.FlowLayout;

public class MainFrame extends JFrame {
	private JPanel contentPane;
	CommandTableModel commandTableModel = new CommandTableModel();
	SortableTableModel sortableCommandTableModel = new SortableTableModel(commandTableModel);
	TableSorterColumnListener tableSorterColumnListener2;

	private JTable commandTable;
	private JTable titanTable;
	TitanCommandTableModel titanTableModel = new TitanCommandTableModel();
	SortableTableModel sortableTableModel = new SortableTableModel(titanTableModel);
	TableSorterColumnListener tableSorterColumnListener;
	private JTable clientTable;

	public MainFrame() {
		setTitle("Titan server " + Global.version);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 514);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Log", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));

		EnhancedTextArea enhancedTextArea = new EnhancedTextArea();
		enhancedTextArea.addTrailListener(new File("titan-server.log"), 300, true);
		panel.add(enhancedTextArea);

		JPanel panelTest = new JPanel();
		tabbedPane.addTab("Openstack test", null, panelTest, null);
		panelTest.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panelTest.add(scrollPane, BorderLayout.CENTER);

		commandTable = new JTable();
		commandTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					runCommand();
				}
			}
		});
		commandTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableSorterColumnListener2 = new TableSorterColumnListener(commandTable, sortableCommandTableModel);
		commandTable.getTableHeader().setReorderingAllowed(false);
		commandTable.setModel(sortableCommandTableModel);
		commandTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		commandTable.getTableHeader().addMouseListener(tableSorterColumnListener2);
		commandTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		commandTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		commandTable.getColumnModel().getColumn(1).setPreferredWidth(1500);
		sortableCommandTableModel.sortByColumn(0, true);

		scrollPane.setViewportView(commandTable);

		JPanel panel_1 = new JPanel();
		panelTest.add(panel_1, BorderLayout.SOUTH);

		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runCommand();
			}
		});
		panel_1.add(btnRun);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Titan command test", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1, BorderLayout.CENTER);

		titanTable = new JTable();
		titanTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					runTitanCommand();
				}
			}
		});
		titanTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableSorterColumnListener = new TableSorterColumnListener(titanTable, sortableTableModel);
		titanTable.getTableHeader().setReorderingAllowed(false);
		titanTable.setModel(sortableTableModel);
		titanTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		titanTable.getTableHeader().addMouseListener(tableSorterColumnListener);
		titanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		titanTable.getColumnModel().getColumn(0).setPreferredWidth(350);

		scrollPane_1.setViewportView(titanTable);

		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Client", null, panel_3, null);
		panel_3.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane_2 = new JScrollPane();
		panel_3.add(scrollPane_2, BorderLayout.CENTER);

		clientTable = new JTable();
		scrollPane_2.setViewportView(clientTable);

		JPanel statusPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) statusPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(statusPanel, BorderLayout.SOUTH);

		testOpenstack(statusPanel);

		setLocationRelativeTo(null);
	}

	private void testOpenstack(JPanel statusPanel) {
		try {
			URL url = new URL(TitanServerSetting.getInstance().keystoneAdminURL);
			if (!CommonLib.portIsOpen(url.getHost(), url.getPort(), 1)) {
				JLabel warningLabel = new JLabel("keystone is down", new ImageIcon(MainFrame.class.getResource("/images/icons/error.png")), SwingConstants.LEFT);
				statusPanel.add(warningLabel);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			URL url = new URL(TitanServerSetting.getInstance().glanceAdminURL);
			if (!CommonLib.portIsOpen(url.getHost(), url.getPort(), 1)) {
				JLabel warningLabel = new JLabel("glance is down", new ImageIcon(MainFrame.class.getResource("/images/icons/error.png")), SwingConstants.LEFT);
				statusPanel.add(warningLabel);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			URL url = new URL(TitanServerSetting.getInstance().novaAdminURL);
			if (!CommonLib.portIsOpen(url.getHost(), url.getPort(), 1)) {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+url.getHost()+"::"+url.getPort());
				JLabel warningLabel = new JLabel("nova is down", new ImageIcon(MainFrame.class.getResource("/images/icons/error.png")), SwingConstants.LEFT);
				statusPanel.add(warningLabel);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			URL url = new URL(TitanServerSetting.getInstance().s3AdminURL);
			if (!CommonLib.portIsOpen(url.getHost(), url.getPort(), 1)) {
				JLabel warningLabel = new JLabel("s3 is down", new ImageIcon(MainFrame.class.getResource("/images/icons/error.png")), SwingConstants.LEFT);
				statusPanel.add(warningLabel);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			URL url = new URL(TitanServerSetting.getInstance().ec2AdminURL);
			if (!CommonLib.portIsOpen(url.getHost(), url.getPort(), 1)) {
				JLabel warningLabel = new JLabel("ec2 is down", new ImageIcon(MainFrame.class.getResource("/images/icons/error.png")), SwingConstants.LEFT);
				statusPanel.add(warningLabel);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	void runCommand() {
		int row = commandTable.getSelectedRow();
		if (row > 3) {
			RunCommandDialog r = new RunCommandDialog(this);
			r.setTitle((String) commandTable.getValueAt(row, 0));
			r.command = (String) commandTable.getValueAt(row, 1);
			OpenstackComm.initParameters(r.command, r.parameterTableModel);
			r.setVisible(true);
		}
	}

	void runTitanCommand() {
		int row = titanTable.getSelectedRow();
		RunTitanCommandDialog r = new RunTitanCommandDialog(this);
		r.setTitle((String) titanTable.getValueAt(row, 0));
		r.textArea.setText((String) titanTable.getValueAt(row, 0));
		r.setVisible(true);
	}
}
