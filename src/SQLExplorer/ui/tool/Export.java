package SQLExplorer.ui.tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import SQLExplorer.db.Tool;
import SQLExplorer.db.UISQLException;
import SQLExplorer.ui.DatabaseTables;
import SQLExplorer.ui.UI;

public class Export extends Tool implements ActionListener {

	private UI ui;
	private JDialog dialog;
	public JTextField path, file;

	public Export(UI ui) {
		super(ui);
		this.ui = ui;
	}

	private void renderOptions() {
		dialog = new JDialog(ui, "Backup Options", true);
		dialog.setLayout(null);

		final JLabel lerrors = new JLabel("Skip Errors");
		lerrors.setBounds(10, 10, 120, 25);
		dialog.add(lerrors);

		final JCheckBox errors = new JCheckBox();
		errors.setBounds(150, 10, 20, 20);
		errors.setSelected(true);
		dialog.add(errors);		

		final JLabel lfile = new JLabel("File to Import");
		lfile.setBounds(10, 40, 120, 25);
		dialog.add(lfile);

		path = new JTextField(System.getProperty("user.home"));
		path.setBounds(150, 40, 150, 25);
		dialog.add(path);

		final JButton choose = new JButton("Choose");
		choose.setBounds(300, 40, 80, 25);
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

		final JButton create = new JButton("Create");
		create.setBounds(120, 110, 90, 25);
		dialog.add(create);
		create.addActionListener(run);

		final JButton cancel = new JButton("Cancel");
		cancel.setBounds(210, 110, 90, 25);
		dialog.add(cancel);
		cancel.addActionListener(close);

		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setResizable(false);
		dialog.setSize(400, 180);
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
			dialog.setEnabled(false);
			try {
				int completed = restore(Export.this);
				if (completed == 0) {
					dialog.dispose();
					ui.getContentPane().remove(ui.pane);
					ui.renderTableList(new DatabaseTables(), ui.database.getSelectedItem().toString());
					ui.pane.updateUI();
					ui.validate();
					ui.repaint();
					JOptionPane
							.showMessageDialog(
									ui,
									"Database export operation has been finished successfully.",
									"Backup Completed",
									JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (UISQLException ex) {
				dialog.dispose();
				JOptionPane.showMessageDialog(ui, ex.getMessage().toString(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	@Override
	public void actionPerformed(ActionEvent event) {
		renderOptions();
	}
}
