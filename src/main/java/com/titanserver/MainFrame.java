package com.titanserver;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

import com.peterswing.CommonLib;
import com.peterswing.advancedswing.enhancedtextarea.EnhancedTextArea;
import com.peterswing.advancedswing.jtable.SortableTableModel;
import com.peterswing.advancedswing.jtable.TableSorterColumnListener;
import com.peterswing.advancedswing.searchtextfield.JSearchTextField;
import com.titanserver.openstack_communication.OpenstackComm;

public class MainFrame extends JFrame {
	private JPanel contentPane;
	CommandTableModel commandTableModel = new CommandTableModel();
	ClientTableModel clientTableModel = new ClientTableModel();
	//	SortableTableModel sortableCommandTableModel = new SortableTableModel(commandTableModel);
	//	TableSorterColumnListener tableSorterColumnListener2;
	final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(commandTableModel);
	private JTable commandTable;

	private JTable titanTable;
	TitanCommandTableModel titanTableModel = new TitanCommandTableModel();
	SortableTableModel sortableTableModel = new SortableTableModel(titanTableModel);
	TableSorterColumnListener tableSorterColumnListener;
	private JTable clientTable;

	private static Logger logger = Logger.getLogger(MainFrame.class);
	private JSearchTextField searchTextField;

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

		JPanel logPanel = new JPanel();
		tabbedPane.addTab("Log", null, logPanel, null);
		logPanel.setLayout(new BorderLayout(0, 0));

		EnhancedTextArea enhancedTextArea = new EnhancedTextArea();
		enhancedTextArea.addTrailListener(new File("titan-server.log"), 300, false);
		logPanel.add(enhancedTextArea);

		JPanel openstackTestPanel = new JPanel();
		tabbedPane.addTab("Openstack test", null, openstackTestPanel, null);
		openstackTestPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		openstackTestPanel.add(scrollPane, BorderLayout.CENTER);

		commandTable = new JTable(commandTableModel);
		commandTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					runCommand();
				}
			}
		});
		commandTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//		tableSorterColumnListener2 = new TableSorterColumnListener(commandTable, sortableCommandTableModel);
		commandTable.getTableHeader().setReorderingAllowed(false);
		//		commandTable.setModel(sortableCommandTableModel);
		//		commandTable.setModel(commandTableModel);
		commandTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//		commandTable.getTableHeader().addMouseListener(tableSorterColumnListener2);
		commandTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		commandTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		commandTable.getColumnModel().getColumn(1).setPreferredWidth(1500);
		//		sortableCommandTableModel.sortByColumn(0, true);

		commandTable.setRowSorter(sorter);
		ArrayList<RowSorter.SortKey> list = new ArrayList<RowSorter.SortKey>();
		list.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		sorter.setSortKeys(list);
		sorter.sort();

		scrollPane.setViewportView(commandTable);

		JPanel panel_1 = new JPanel();
		openstackTestPanel.add(panel_1, BorderLayout.SOUTH);

		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runCommand();
			}
		});
		panel_1.add(btnRun);

		JPanel toolBar = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) toolBar.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		openstackTestPanel.add(toolBar, BorderLayout.NORTH);

		searchTextField = new JSearchTextField();
		searchTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				sorter.setRowFilter(RowFilter.regexFilter(searchTextField.getText()));
			}
		});
		searchTextField.setPreferredSize(new Dimension(200, 25));
		toolBar.add(searchTextField);

		JPanel titanCommandPanel = new JPanel();
		tabbedPane.addTab("Titan command test", null, titanCommandPanel, null);
		titanCommandPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane_1 = new JScrollPane();
		titanCommandPanel.add(scrollPane_1, BorderLayout.CENTER);

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

		JPanel clientPanel = new JPanel();
		tabbedPane.addTab("Client", null, clientPanel, null);
		clientPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		clientPanel.add(panel, BorderLayout.SOUTH);

		JButton refreshClientButton = new JButton("Refresh");
		refreshClientButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clientTableModel.fireTableDataChanged();
			}
		});
		panel.add(refreshClientButton);

		JScrollPane scrollPane_2 = new JScrollPane();
		clientPanel.add(scrollPane_2, BorderLayout.CENTER);

		clientTable = new JTable();
		clientTable.setModel(clientTableModel);
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
			logger.info("testing keystone");
			URL url = new URL(TitanServerSetting.getInstance().keystoneAdminURL);
			if (!CommonLib.portIsOpen(url.getHost(), url.getPort(), 1)) {
				JLabel warningLabel = new JLabel("keystone is down", new ImageIcon(MainFrame.class.getResource("/images/icons/error.png")), SwingConstants.LEFT);
				statusPanel.add(warningLabel);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			logger.info("testing keystone");
			URL url = new URL(TitanServerSetting.getInstance().glanceAdminURL);
			if (!CommonLib.portIsOpen(url.getHost(), url.getPort(), 1)) {
				JLabel warningLabel = new JLabel("glance is down", new ImageIcon(MainFrame.class.getResource("/images/icons/error.png")), SwingConstants.LEFT);
				statusPanel.add(warningLabel);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			logger.info("testing nova");
			URL url = new URL(TitanServerSetting.getInstance().novaAdminURL);
			if (!CommonLib.portIsOpen(url.getHost(), url.getPort(), 1)) {
				JLabel warningLabel = new JLabel("nova is down", new ImageIcon(MainFrame.class.getResource("/images/icons/error.png")), SwingConstants.LEFT);
				statusPanel.add(warningLabel);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			logger.info("testing s3");
			URL url = new URL(TitanServerSetting.getInstance().s3AdminURL);
			if (!CommonLib.portIsOpen(url.getHost(), url.getPort(), 1)) {
				JLabel warningLabel = new JLabel("s3 is down", new ImageIcon(MainFrame.class.getResource("/images/icons/error.png")), SwingConstants.LEFT);
				statusPanel.add(warningLabel);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			logger.info("testing ec2");
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
		if (!commandTable.getValueAt(row, 0).toString().contains("Os ")) {
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
