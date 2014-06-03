package SQLExplorer.ui.tool;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import SQLExplorer.db.Query;
import SQLExplorer.db.UISQLException;
import SQLExplorer.ui.UI;

public class NewDatabase extends JDialog {

	private static final long serialVersionUID = 1L;
	private UI ui;
	private JTextField dbName;
	private JComboBox<Object> collation;	

	public NewDatabase(final UI ui) {
		super(ui, "Create Database", true);
		this.ui = ui;
		render();
	}

	private void render() {
		setLayout(null);

		final JLabel ldbName = new JLabel("Database Name");
		ldbName.setBounds(10, 10, 120, 25);
		add(ldbName);

		dbName = new JTextField(20);
		dbName.setBounds(120, 10, 270, 25);
		add(dbName);

		final JLabel lcollation = new JLabel("Collation");
		lcollation.setBounds(10, 40, 120, 25);
		add(lcollation);

		try {
			List<Object> charactesr = new Query(ui).getCharactes();
			collation = new JComboBox<Object>(new DefaultComboBoxModel<Object>(
					charactesr.toArray()));
		} catch (UISQLException e) {
			JOptionPane.showMessageDialog(ui, e.getMessage().toString(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		collation.setBounds(120, 40, 270, 25);
		add(collation);
		collation.setSelectedItem("utf8");

		final JButton create = new JButton("Create");
		create.setBounds(120, 80, 90, 25);
		create.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
		add(create);

		create.getActionMap().put("Enter", add);
		create.addActionListener(add);

		JButton cancel = new JButton("Cancel");
		cancel.setBounds(210, 80, 90, 25);
		add(cancel);
		cancel.addActionListener(close);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(400, 150);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private Action close = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			close();
		}
	};

	private Action add = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Object name = dbName.getText();
			ComboBoxModel<?> model = ui.database.getModel();
			boolean success = true;
			String message = null;
			boolean exists = false;

			for (int i = 0; i < model.getSize(); i++) {
				if (model.getElementAt(i).equals(name)) {
					exists = true;
					break;
				}
			}
			if (name.equals("")) {
				success = false;
				message = "Database Name can not be empty.";
			}
			if (exists) {
				success = false;
				message = "Database '" + name
						+ "' already exists. Please choose another name.";
				dbName.setText("");
			}
			if (!success) {
				JOptionPane.showMessageDialog(ui, message, "Error",
						JOptionPane.ERROR_MESSAGE);				
			} else {
				try {
					new Query(ui).addDatabase(name.toString(), collation
							.getSelectedItem().toString());
				} catch (UISQLException ex) {
					JOptionPane.showMessageDialog(ui, ex.getMessage()
							.toString(), "Error", JOptionPane.ERROR_MESSAGE);					
				}
				ui.database.addItem(name);
				ui.database.updateUI();
				ui.database.setSelectedItem(name);
				close();
			}
		}
	};

	private void close() {
		getContentPane().removeAll();
		dispose();
	}

}
