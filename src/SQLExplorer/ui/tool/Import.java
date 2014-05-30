package SQLExplorer.ui.tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import SQLExplorer.db.Query;
import SQLExplorer.db.UISQLException;
import SQLExplorer.db.tool.Restore;
import SQLExplorer.ui.UI;

public class Import implements ActionListener {

	public UI ui;
	public JDialog dialog;
	public JTextField path, file;
	public JCheckBox force;

	public Import(UI ui) {
		this.ui = ui;
	}

	private void renderDialog() {
		dialog = new JDialog(ui, "Import Options", true);
		dialog.setLayout(null);

		final JLabel lforce = new JLabel("Skip Errors");
		lforce.setBounds(10, 10, 120, 25);
		dialog.add(lforce);

		force = new JCheckBox();
		force.setBounds(95, 12, 20, 20);
		force.setSelected(true);
		dialog.add(force);

		final JLabel lfile = new JLabel("File to Import");
		lfile.setBounds(10, 40, 120, 25);
		dialog.add(lfile);

		path = new JTextField(System.getProperty("user.home"));
		path.setBounds(95, 40, 215, 25);
		dialog.add(path);

		final JButton choose = new JButton("Choose");
		choose.setBounds(310, 40, 80, 25);
		dialog.add(choose);

		choose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				DirChooser.getInstance();
				JFileChooser chooser = DirChooser.dialog(false);
				int jfch = chooser.showOpenDialog(ui);
				if (jfch == JFileChooser.APPROVE_OPTION) {
					path.setText(chooser.getSelectedFile().toString());
				} else if (jfch == JFileChooser.CANCEL_OPTION) {
					chooser.setVisible(false);
				}
			}
		});

		final JButton create = new JButton("Import");
		create.setBounds(120, 75, 90, 25);
		dialog.add(create);
		create.addActionListener(run);

		final JButton cancel = new JButton("Cancel");
		cancel.setBounds(210, 75, 90, 25);
		dialog.add(cancel);
		cancel.addActionListener(close);

		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setResizable(false);
		dialog.setSize(400, 145);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	private Action close = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			dialog.dispose();
		}
	};

	private Action run = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Restore restore = new Restore(Import.this);
			restore.start();
		}
	};

	@Override
	public void actionPerformed(ActionEvent event) {
		renderDialog();
	}

	public void finished() {
		try {
			// Rendering new a list of databases
			List<Object> dbs = new Query(ui).listDatabases();
			DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<Object>(
					dbs.toArray());
			ui.database.setModel(model);
			ui.database.setSelectedIndex(0);
		} catch (UISQLException ex) {
			JOptionPane.showMessageDialog(ui, ex.getMessage().toString(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		JOptionPane.showMessageDialog(ui,
				"Database export operation has been finished successfully.",
				"Backup Completed", JOptionPane.INFORMATION_MESSAGE);		
	}
}
