package com.lirfu.thinkboard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.lirfu.thinkboard.Engine.MyTools;
import com.lirfu.thinkboard.components.Block;
import com.lirfu.thinkboard.components.BoardComponent;
import com.lirfu.thinkboard.components.Line;

public class Board extends JPanel {
	public static LinkedList<BoardComponent> components = new LinkedList<>();
	private static LinkedList<Line> lines = new LinkedList<>();
	//	private final int refreshOffset = 100;
	private boolean showLineText = true;

	private BoardComponent lineStart = null;
	private BoardComponent mouseHoldingComponent = null;

	public Board(Window window) {
		setBorder(BorderFactory.createLineBorder(Color.black));
		setBackground(Color.decode(String.valueOf(0xFFAADDAA)));

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 1) {
					BoardComponent temp = onComponentCheck(e.getX(), e.getY());
					// Just moving the component.
					if (Engine.getCurrentTool() == MyTools.MOVE && mouseHoldingComponent == null) {
						mouseHoldingComponent = temp;
						if (temp != null) {
							moveComponent(temp, e.getX(), e.getY());
						}
					}
					// Placing a new block.
					else if (temp == null && Engine.getCurrentTool() == MyTools.BLOCK) {
						placeComponent(e.getX(), e.getY());
						return;
					}
					//// Special tools code
					else if (Engine.getCurrentTool() == MyTools.LINE) {
						if (lineStart == null) {
							System.out.println("#Line started on " + temp.getName());
							lineStart = temp;
						} else {
							if (lineStart.getName().matches(temp.getName()) || temp.getType() == MyTools.LINE) { // If it's the same component or is pointing to
								ditchLine();
								return;
							}

							lineStart.addConnection(temp.getName());
							temp.addConnection(lineStart.getName());
							System.out.println("#Line ended on " + temp.getName());
							lineStart = null;
							repaint();
						}
					} else if (Engine.getCurrentTool() == MyTools.DELETE) {
						deleteComponent(e.getX(), e.getY());
					}

					//// Double clicked...
				} else {
					// Ditch an accidentally started line if any.
					if (lineStart != null) {
						ditchLine();
					}

					BoardComponent component = onComponentCheck(e.getX(), e.getY());
					if (component != null) {
						openComponentDetailsWindow(component);
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				if (mouseHoldingComponent != null)
					System.out.println("Releasing " + mouseHoldingComponent.getName());
				mouseHoldingComponent = null;
			}
		});

		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (mouseHoldingComponent != null) {
					moveComponent(mouseHoldingComponent, e.getX(), e.getY());
				}
			}
		});
	}

	private void ditchLine() {
		lineStart = null;
		System.out.println("#Line ditched!");
	}

	private String getNextName() {
		String name = "C";
		for (int num = 0;; num++) { // Number iterator
			boolean isOK = true;
			for (BoardComponent temp : components) {
				if (temp.getName().matches(name + num)) {
					isOK = false;
				}
			}
			if (isOK)
				return name += num;
		}
	}

	/**
	 * Checks if given coordinates point on any of components.
	 * 
	 * @param x
	 *            pointer coordinate.
	 * @param y
	 *            pointer coordinate.
	 * @return Component on which coordinates point.
	 */
	private BoardComponent onComponentCheck(int x, int y) {
		BoardComponent comp = null;
		for (BoardComponent temp : components) {
			if (temp.containsCoordinate(x, y)) {
				comp = temp;
				break;
			}
		}

		// Check for lines...
		if (comp == null) {
			for (BoardComponent temp : lines) {
				if (temp.containsCoordinate(x, y)) {
					comp = temp;
					break;
				}
			}
		}

		return comp;
	}

	private void placeComponent(int x, int y) {
		BoardComponent comp = Engine.useCurrentTool(getNextName(), x, y);
		try {
			if (comp.getType() == MyTools.LINE)
				return;

			components.add(comp);
			repaint();
			System.out.println("#Adding component: " + comp.getName());
		} catch (NullPointerException e) { // No place able component was chosen.
			ditchLine();
		}
		repaint();
	}

	/**
	 * Moves the object on selected index.
	 * 
	 * @param index
	 *            of the component in components list.
	 * @param newX
	 *            New origin coordinate.
	 * @param newY
	 *            New origin coordinate.
	 */
	private void moveComponent(BoardComponent comp, int newX, int newY) {
		comp.setX(newX);
		comp.setY(newY);
		//		repaint(newX - refreshOffset, newY - refreshOffset, newX + refreshOffset, newY + refreshOffset);
		repaint();
	}

	private void deleteComponent(int x, int y) {
		BoardComponent comp = onComponentCheck(x, y);
		if (comp == null)
			return;

		if (comp.getType() == MyTools.BLOCK) {
			components.remove(comp);
			// Remove connections to this component.
			for (BoardComponent temp : components) {
				for (String str : temp.getConnections()) {
					if (str.matches(comp.getName())) {
						temp.removeConnection(comp.getName());
					}
				}
			}
		} else if (comp.getType() == MyTools.LINE) {
			lines.remove(comp);
			// Remove connection data of both ends.
			BoardComponent[] ends = ((Line) comp).getBothEnds();
			ends[0].removeConnection(ends[1].getName());
			ends[1].removeConnection(ends[0].getName());
		}

		System.out.println("#Deleted component: " + comp.getName());
		repaint();
	}

	public static boolean renameComponent(BoardComponent component, String newName) {
		String oldName = component.getName();
		component.setName(newName);

		int x = 0;
		// If that name already exists.
		for (BoardComponent temp : components) {
			System.out.println("--Checking component: " + temp.getName());
			if (temp.getName().matches(newName)) {
				x++;
				if (x > 1) {
					System.out.println("Name already exists!");
					component.setName(oldName);
					return false;
				}
			}
		}

		// Update all other components connections.
		for (BoardComponent temp : components) {
			if (!temp.getName().matches(oldName)) {
				if (temp.removeConnection(oldName))
					temp.addConnection(newName);
			}
		}

		System.out.println("Component renamed: " + oldName + " to " + component.getName());
		return true;
	}

	@Override
	/**
	 * Automatically produces lines. Removes extras in two way connections to
	 * keep only a single link between components.
	 */
	protected void paintComponent(Graphics g) {
		System.out.println("Repainting...");
		super.paintComponent(g);
		lines.clear();
		for (BoardComponent temp : components) { // Iterate through components.
			for (String connec : temp.getConnections()) { // Each components connections.
				int index = components.indexOf(new Block(connec, 0, 0)); // Test to find the other end of connection.
				BoardComponent comp = components.get(index);
				Line line = new Line(temp.getName() + "<=>" + connec, temp, comp);

				// Check if this line was already added.
				if (!lines.contains(line)) {
					lines.add(line);
					line.setShowText(showLineText);
					line.paintComponent(g);
					//					System.out.println("Found line: " + line.getName());
				}
			}
		}
		for (BoardComponent temp : components) {
			temp.paintComponent(g);
		}
	}

	private void openComponentDetailsWindow(BoardComponent component) {
		DetailsWindow window = new DetailsWindow(component);
		window.setLocation(component.getX() + 10, component.getY() + 10);
		window.setVisible(true);
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				repaint();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				repaint();
			}
		});
	}

	public void setShowLineText(boolean show) {
		this.showLineText = show;
		repaint();
	}
}
