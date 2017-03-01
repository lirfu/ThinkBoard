package com.lirfu.thinkboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.lirfu.thinkboard.Engine.MyTools;

public class Window extends JFrame {
	private final JFrame context = this;
	private static String selToolIntro = "Selected tool: ";
	private static JTextArea selTool;
	private final Board currentBoard;
	
	public Window() {
		setTitle("ThinkBoard - New Session");
		setMinimumSize(new Dimension(400, 200));
		setLayout(new BorderLayout());
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Engine.saveCurrentBoard();
			}
			
			@Override
			public void windowOpened(WindowEvent e) {
				Engine.loadCurrentBoard();
			}
		});
		
		// Shows which tool is selected.
		selTool = new JTextArea();
		
		currentBoard = new Board(this);
		
		add(initMenuBar(), BorderLayout.NORTH);
		add(selTool, BorderLayout.SOUTH);
		add(initTools(), BorderLayout.WEST);
		add(currentBoard, BorderLayout.CENTER);
	}
	
	public static void changeTool(MyTools tool) {
		selTool.setText(selToolIntro + Engine.parseToolName(tool));
	}
	
	private JScrollPane initTools() {
		JPanel tools = new JPanel();
		tools.setLayout(new BoxLayout(tools, BoxLayout.Y_AXIS));
		
		JScrollPane scrollPane = new JScrollPane(tools);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		ToolButton move = new ToolButton(Engine.parseToolName(MyTools.MOVE));
		move.setToolTipText("Move nodes around the board.");
		move.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Engine.setCurrentTool(MyTools.MOVE);
			}
		});
		
		ToolButton block = new ToolButton(Engine.parseToolName(MyTools.BLOCK));
		block.setToolTipText("Place a new node.");
		block.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Engine.setCurrentTool(MyTools.BLOCK);
			}
		});
		
		ToolButton line = new ToolButton(Engine.parseToolName(MyTools.LINE));
		line.setToolTipText("Connect two nodes with a line.");
		line.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Engine.setCurrentTool(MyTools.LINE);
			}
		});
		
		ToolButton delete = new ToolButton(Engine.parseToolName(MyTools.DELETE));
		delete.setToolTipText("Delete the selected object.");
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Engine.setCurrentTool(MyTools.DELETE);
			}
		});
		
		tools.add(move);
		tools.add(block);
		tools.add(line);
		tools.add(delete);
		return scrollPane;
	}
	
	private class ToolButton extends JButton {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		protected ToolButton(String title) {
			super(title);
			
			int width = 70;
			int height = 40;
			
			setPreferredSize(new Dimension(width, height));
			setMinimumSize(new Dimension(width, height));
			setMaximumSize(new Dimension(width, height));
			setBackground(new Color(0x88AA88));
		}
	}
	
	private JMenuBar initMenuBar() {
		// Where the GUI is created:
		JMenuBar menuBar = new JMenuBar();
		
		//// FILE
		JMenu file = new JMenu("File");
		
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Engine.saveCurrentBoard();
				selTool.setText("Board saved!");
			}
		});
		JMenuItem load = new JMenuItem("Load");
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Engine.loadCurrentBoard();
				selTool.setText("Board loaded!");
			}
		});
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispatchEvent(new WindowEvent(context, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		file.add(save);
		file.add(load);
		file.addSeparator();
		file.add(exit);
		
		//// VIEW
		JMenu view = new JMenu("View");
		
//		view.addSeparator();
		JCheckBoxMenuItem showLineText = new JCheckBoxMenuItem("Show line text");
		showLineText.setSelected(true);
		showLineText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentBoard.setShowLineText(showLineText.isSelected());
			}
		});
		
		view.add(showLineText);
		
		menuBar.add(file);
		menuBar.add(view);
		return menuBar;
	}
}
