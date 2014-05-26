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
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import SQLExplorer.db.Tool;
import SQLExplorer.db.UISQLException;
import SQLExplorer.ui.UI;

public class Export extends Tool implements ActionListener {

	private UI ui;
	private JDialog dialog;
	public JTextField path, file;
	public JCheckBox quick, dropDb, force;

	public Export(UI ui) {
		super(ui);
		this.ui = ui;
	}

	private void renderDialog() {
		dialog = new JDialog(ui, "Export Options", true);
		dialog.setLayout(null);

		final JLabel lforce = new JLabel("Skip Errors");
		lforce.setBounds(10, 10, 120, 25);
		dialog.add(lforce);

		force = new JCheckBox();
		force.setBounds(80, 12, 20, 20);
		force.setSelected(true);
		dialog.add(force);
		
		final JLabel ldropdb = new JLabel("Drop Database");
		ldropdb.setBounds(135, 10, 120, 25);
		dialog.add(ldropdb);

		dropDb = new JCheckBox();
		dropDb.setBounds(230, 12, 20, 20);
		dropDb.setSelected(true);
		dialog.add(dropDb);
		
		final JLabel lquick = new JLabel("Disable Cache");
		lquick.setBounds(280, 10, 120, 25);
		dialog.add(lquick);
		
		quick = new JCheckBox();
		quick.setBounds(370, 12, 20, 20);
		quick.setSelected(true);
		dialog.add(quick);
		
		final JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
		sep.setBounds(10, 35, 380, 5);
		dialog.add(sep);

		final JLabel ldir = new JLabel("Destination Folder");
		ldir.setBounds(10, 45, 120, 25);
		dialog.add(ldir);

		path = new JTextField(System.getProperty("user.home"));
		path.setBounds(130, 45, 180, 25);
		dialog.add(path);

		final JButton dir = new JButton("Choose");
		dir.setBounds(310, 45, 80, 25);
		dialog.add(dir);

		final JLabel lfile = new JLabel("File Name");
		lfile.setBounds(10, 75, 120, 25);
		dialog.add(lfile);

		file = new JTextField(String.format("%s.sql", ui.database
				.getSelectedItem().toString()));
		file.setBounds(130, 75, 260, 25);
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

		final JButton create = new JButton("Export");
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
				int completed = export(Export.this);
				if (completed == 0) {
					dialog.dispose();
					JOptionPane
							.showMessageDialog(
									ui,
									"Database import operation has been finished successfully.",
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
		renderDialog();
	}
}
