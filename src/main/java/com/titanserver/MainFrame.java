package com.c2.pandoraserver;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import com.c2.pandoraserver.openstack_communication.OpenstackComm;
import com.peterswing.advancedswing.enhancedtextarea.EnhancedTextArea;
import com.peterswing.advancedswing.jtable.SortableTableModel;
import com.peterswing.advancedswing.jtable.TableSorterColumnListener;

public class MainFrame extends JFrame {
	private JPanel contentPane;
	CommandTableModel commandTableModel = new CommandTableModel();
	SortableTableModel sortableCommandTableModel = new SortableTableModel(commandTableModel);
	TableSorterColumnListener tableSorterColumnListener2;

	private JTable commandTable;
	private JTable pandoraTable;
	PandoraCommandTableModel pandoraTableModel = new PandoraCommandTableModel();
	SortableTableModel sortableTableModel = new SortableTableModel(pandoraTableModel);
	TableSorterColumnListener tableSorterColumnListener;
	private JTable clientTable;

	public MainFrame() {
		setTitle("Pandora server " + Global.version);
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
		enhancedTextArea.addTrailListener(new File("pandora-server.log"), 300, true);
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
		tabbedPane.addTab("Pandora command test", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1, BorderLayout.CENTER);

		pandoraTable = new JTable();
		pandoraTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					runPandoraCommand();
				}
			}
		});
		pandoraTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableSorterColumnListener = new TableSorterColumnListener(pandoraTable, sortableTableModel);
		pandoraTable.getTableHeader().setReorderingAllowed(false);
		pandoraTable.setModel(sortableTableModel);
		pandoraTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		pandoraTable.getTableHeader().addMouseListener(tableSorterColumnListener);
		pandoraTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pandoraTable.getColumnModel().getColumn(0).setPreferredWidth(350);

		scrollPane_1.setViewportView(pandoraTable);

		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Client", null, panel_3, null);
		panel_3.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane_2 = new JScrollPane();
		panel_3.add(scrollPane_2, BorderLayout.CENTER);

		clientTable = new JTable();
		scrollPane_2.setViewportView(clientTable);

		setLocationRelativeTo(null);
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

	void runPandoraCommand() {
		int row = pandoraTable.getSelectedRow();
		RunPandoraCommandDialog r = new RunPandoraCommandDialog(this);
		r.setTitle((String) pandoraTable.getValueAt(row, 0));
		r.textArea.setText((String) pandoraTable.getValueAt(row, 0));
		r.setVisible(true);
	}
}
