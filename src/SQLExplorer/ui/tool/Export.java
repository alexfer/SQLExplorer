package SQLExplorer.ui.tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

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

import SQLExplorer.db.tool.Backup;
import SQLExplorer.db.tool.ToolException;
import SQLExplorer.ui.UI;

public class Export implements ActionListener {

	public UI ui;
	public JDialog dialog;
	public JTextField path, file;
	public JCheckBox quick, dropDb, force;
	public static boolean finished = false;
	private String title;
	public ArrayList<String> tblSelected = new ArrayList<String>();

	public Export(UI ui) {
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
			ui.progress.setVisible(false);
			dialog.dispose();
		}
	};

	private Action run = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			for (int i = 0; i < ui.table.getModel().getRowCount(); i++) {
				if ((Boolean) ui.table.getValueAt(i, 6)) {
					tblSelected.add(ui.table.getValueAt(i, 0).toString());
				}
			}
			
			ui.setEnabled(false);
			title = ui.getTitle();
			ui.setTitle(UI.title + " - Backup is running...");

			Backup backup = new Backup(Export.this, System.currentTimeMillis());

			try {
				final long[] finished = backup.export();
				if (finished[0] == 0) {
					finished(finished[1]);
				}
			} catch (ToolException ex) {
				JOptionPane.showMessageDialog(ui, ex.getMessage().toString(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			ui.setEnabled(true);
			ui.setTitle(title);
		}
	};

	@Override
	public void actionPerformed(ActionEvent event) {
		renderDialog();
	}

	private void finished(long elapsed) {
		Date date = new Date(elapsed);
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		ui.setTitle(UI.title + " - Backup has been completed.");
		JOptionPane.showMessageDialog(ui,
				"Backup has been completed successfully.\nSpent time: "
						+ formatter.format(date), "Export Completed",
				JOptionPane.INFORMATION_MESSAGE);
	}
}