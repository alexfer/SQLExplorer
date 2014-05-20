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
import SQLExplorer.ui.UI;

public class Import extends Tool implements ActionListener {

	private UI ui;
	private JDialog dialog;
	public JTextField path, file;

	public Import(UI ui) {
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

		final JLabel lcache = new JLabel("Disable cache");
		lcache.setBounds(250, 10, 120, 25);
		dialog.add(lcache);

		final JCheckBox cache = new JCheckBox();
		cache.setBounds(350, 10, 20, 20);
		cache.setSelected(true);
		dialog.add(cache);

		final JLabel ldir = new JLabel("Destination Folder");
		ldir.setBounds(10, 40, 120, 25);
		dialog.add(ldir);

		path = new JTextField(System.getProperty("user.home"));
		path.setBounds(150, 40, 150, 25);
		dialog.add(path);

		final JButton dir = new JButton("Choose");
		dir.setBounds(300, 40, 80, 25);
		dialog.add(dir);

		final JLabel lfile = new JLabel("File Name");
		lfile.setBounds(10, 70, 120, 25);
		dialog.add(lfile);

		file = new JTextField(String.format("%s.sql", ui.database
				.getSelectedItem().toString()));
		file.setBounds(150, 70, 230, 25);
		dialog.add(file);

		dir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				DirChooser.getInstance();
				JFileChooser chooser = DirChooser.dialog(true);
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
				int completed = backup(Import.this);
				if (completed == 0) {
					dialog.dispose();
					JOptionPane
							.showMessageDialog(
									ui,
									"Database backup operation has been finished successfully.",
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
