package ccl;

import java.awt.BorderLayout;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class LogWindow extends JFrame {
	
	private LinkedList<String> logs;
	private JPanel pane;
	private JScrollPane sc;
	public LogWindow(LinkedList<String> logs) {
		this.logs = logs;
		this.setSize(500,400);
		JList list = new JList(logs.toArray());
		sc = new JScrollPane(list);
		this.getContentPane().add(sc, BorderLayout.CENTER);
		
	}
	
	public void setTitle(String title) {
		super.setTitle(title);
	}
	public void setVisible() {
		super.setVisible(true);
	}
	public void validate() {
		JList list = new JList(logs.toArray());
		sc.setViewportView(list);
		super.validate();
		this.repaint();
	}
	

}
