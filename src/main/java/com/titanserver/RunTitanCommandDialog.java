package com.titanserver;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.hibernate.Query;
import org.hibernate.Session;

import com.peterswing.advancedswing.enhancedtextarea.EnhancedTextArea;
import com.titanserver.table.InstancePermission;
import com.titanserver.table.InstancePermissionGroup;
import com.titanserver.table.ScreenPermission;
import com.titanserver.table.ScreenPermissionGroup;
import com.titanserver.table.User;

public class RunTitanCommandDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private EnhancedTextArea enhancedTextArea;
	ParameterTableModel parameterTableModel = new ParameterTableModel();
	JTextArea textArea;

	public RunTitanCommandDialog(JFrame f) {
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
					textArea = new JTextArea();
					scrollPane.setViewportView(textArea);
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
						if (textArea.getText().equals("get screen permissions")) {
							Session session = HibernateUtil.openSession();
							Query query = session.createQuery("from ScreenPermission");
							List<ScreenPermission> list = query.list();
							enhancedTextArea.setText(listToString(list));
							session.close();
						} else if (textArea.getText().equals("get instance permissions")) {
							Session session = HibernateUtil.openSession();
							Query query = session.createQuery("from InstancePermission");
							List<InstancePermission> list = query.list();
							enhancedTextArea.setText(listToString(list));
							session.close();
						} else if (textArea.getText().equals("get users")) {
							Session session = HibernateUtil.openSession();
							Query query = session.createQuery("from User");
							List<User> list = query.list();
							enhancedTextArea.setText(listToString(list));
							session.close();
						} else if (textArea.getText().equals("get screen permission groups")) {
							Session session = HibernateUtil.openSession();
							Query query = session.createQuery("from ScreenPermissionGroup");
							List<ScreenPermissionGroup> list = query.list();
							enhancedTextArea.setText(listToString(list));
							session.close();
						} else if (textArea.getText().equals("get instance permission groups")) {
							Session session = HibernateUtil.openSession();
							Query query = session.createQuery("from InstancePermissionGroup");
							List<InstancePermissionGroup> list = query.list();
							enhancedTextArea.setText(listToString(list));
							session.close();
						}
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

	private String listToString(List list) {
		String listString = "";
		for (Object s : list) {
			listString += s.toString() + "\n";
		}
		return listString;
	}
}
