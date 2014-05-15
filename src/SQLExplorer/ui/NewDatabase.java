package SQLExplorer.ui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import SQLExplorer.db.Query;

public class NewDatabase extends JDialog {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 168902726944563410L;
	JButton create;
	MakeUI ui;
	JTextField dbName;
	NewDatabase(MakeUI ui) {		
		super(ui, "Create Database", true);
		this.ui = ui;
		render();		
	}

	private void render() {
		setLayout(null);

		JLabel ldbName = new JLabel("Database Name");
		ldbName.setBounds(10, 10, 120, 25);
		add(ldbName);

		dbName = new JTextField(20);
		dbName.setBounds(160, 10, 230, 25);
		add(dbName);
		
		create = new JButton("Create");
		create.setBounds(130, 140, 120, 25);
		create.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
		add(create);
		create.getActionMap().put("Enter", action);
		create.addActionListener(action);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(400, 200);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private Action action = new AbstractAction() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5692304708975454149L;

		@Override
		public void actionPerformed(ActionEvent e) {
			new Query(ui).addDatabase(dbName.getText().toString());
			ui.database.addItem(dbName.getText());
			ui.database.updateUI();
			ui.database.setSelectedItem(dbName.getText());
			dispose();
		}
	};

}
