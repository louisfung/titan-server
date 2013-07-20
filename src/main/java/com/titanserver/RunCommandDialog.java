package com.c2.pandoraserver;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import net.sf.json.JSONObject;

import com.c2.pandoraserver.openstack_communication.OpenstackComm;
import com.peterswing.advancedswing.enhancedtextarea.EnhancedTextArea;

public class RunCommandDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public String command;
	private EnhancedTextArea enhancedTextArea;
	private JTable parameterTable;
	ParameterTableModel parameterTableModel = new ParameterTableModel();

	public RunCommandDialog(JFrame f) {
		super(f, true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JSplitPane splitPane = new JSplitPane();
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			splitPane.setDividerLocation(200);
			contentPanel.add(splitPane, BorderLayout.CENTER);
			{
				JScrollPane scrollPane = new JScrollPane();
				splitPane.setLeftComponent(scrollPane);
				{
					parameterTable = new JTable();
					parameterTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					parameterTable.setModel(parameterTableModel);
					parameterTable.getColumnModel().getColumn(0).setPreferredWidth(100);
					parameterTable.getColumnModel().getColumn(1).setPreferredWidth(1500);
					scrollPane.setViewportView(parameterTable);
				}
			}
			{
				enhancedTextArea = new EnhancedTextArea();
				splitPane.setRightComponent(enhancedTextArea);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Run");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						OpenstackComm.execute(command, parameterTableModel, enhancedTextArea);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}

		setSize(800, 600);
		setLocationRelativeTo(null);
	}

}
