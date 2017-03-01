package com.lirfu.thinkboard;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.lirfu.thinkboard.Engine.MyTools;
import com.lirfu.thinkboard.components.BoardComponent;
import com.lirfu.thinkboard.components.Line;

public class DetailsWindow extends JFrame {
	public DetailsWindow(BoardComponent component) {
		setTitle("ThinkBoard - New Session");
		setMinimumSize(new Dimension(300, 200));
		setLayout(new GridLayout(4, 2));

		JLabel nameText = new JLabel("Name:");
		JTextField name = new JTextField(component.getName());

		JLabel detailsText = new JLabel("Details:");
		JTextField details = new JTextField(component.getDescription());

		JLabel colorText = new JLabel("Color:");
		JTextField color = new JTextField(component.getColor());

		JLabel listText = new JLabel("Connections:");

		LinkedList<String> entries = new LinkedList<>();
		for (String temp : component.getConnections()) {
			entries.add(temp);
		}
		JList<String> list = new JList<String>(entries.toArray(new String[] {}));
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		JLabel thicknessText = new JLabel("Is thick?");
		JCheckBox thicknessCheck = new JCheckBox();

		JButton save_BTN = new JButton("SAVE");
		save_BTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (component.getType() == MyTools.BLOCK) {
					// If there was no problems in renaming.
					if (Board.renameComponent(component, name.getText())) {
						component.setDescription(details.getText());
						component.setColor(color.getText());
						dispose();
						return;
					}
					// The name already existed.
					JOptionPane.showMessageDialog(getContentPane(), "The name " + name.getText() + " already extsts!", "Error", JOptionPane.INFORMATION_MESSAGE);
				} else if (component.getType() == MyTools.LINE) {
					component.setDescription(details.getText());
					component.setColor(color.getText());
					((Line) component).setThickness(thicknessCheck.isSelected());
					return;
				}
			}
		});

		add(nameText);
		add(name);
		add(detailsText);
		add(details);
		add(colorText);
		add(color);
		add(listText);
		add(listScroller);
		add(new JPanel());
		add(save_BTN);

		if (component.getType() == MyTools.LINE) {
			add(thicknessText);
			add(thicknessCheck);
		}
	}
}
