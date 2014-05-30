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
import SQLExplorer.ui.UI;

public class Export implements ActionListener {

	public UI ui;
	private JDialog d;
	public JTextField path, file;
	public JCheckBox quick, drop, force;
	public ArrayList<String> tblSelected = new ArrayList<String>();

	public Export(UI ui) {
		this.ui = ui;
	}

	private void renderDialog() {
		d = new JDialog(ui, "Export Options", true);
		d.setLayout(null);

		final JLabel lforce = new JLabel("Skip Errors");
		lforce.setBounds(10, 10, 120, 25);
		d.add(lforce);

		force = new JCheckBox();
		force.setBounds(80, 12, 20, 20);
		force.setSelected(true);
		d.add(force);

		final JLabel ldropdb = new JLabel("Drop Database");
		ldropdb.setBounds(135, 10, 120, 25);
		d.add(ldropdb);

		drop = new JCheckBox();
		drop.setBounds(230, 12, 20, 20);
		drop.setSelected(true);
		d.add(drop);

		final JLabel lquick = new JLabel("Disable Cache");
		lquick.setBounds(280, 10, 120, 25);
		d.add(lquick);

		quick = new JCheckBox();
		quick.setBounds(370, 12, 20, 20);
		quick.setSelected(true);
		d.add(quick);

		final JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
		sep.setBounds(10, 35, 380, 5);
		d.add(sep);

		final JLabel ldir = new JLabel("Destination Folder");
		ldir.setBounds(10, 45, 120, 25);
		d.add(ldir);

		path = new JTextField(System.getProperty("user.home"));
		path.setBounds(130, 45, 180, 25);
		d.add(path);

		final JButton dir = new JButton("Choose");
		dir.setBounds(310, 45, 80, 25);
		d.add(dir);

		final JLabel lfile = new JLabel("File Name");
		lfile.setBounds(10, 75, 120, 25);
		d.add(lfile);

		file = new JTextField(String.format("%s.sql", ui.database
				.getSelectedItem().toString()));
		file.setBounds(130, 75, 260, 25);
		d.add(file);

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
		d.add(create);
		create.addActionListener(run);

		final JButton cancel = new JButton("Cancel");
		cancel.setBounds(210, 110, 90, 25);
		d.add(cancel);
		cancel.addActionListener(close);

		d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		d.setResizable(false);
		d.setSize(400, 180);
		d.setLocationRelativeTo(null);
		d.setVisible(true);
	}

	private Action close = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			d.dispose();
		}
	};

	private Action run = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			d.dispose();
			ui.progress.setVisible(true);
			for (int i = 0; i < ui.table.getModel().getRowCount(); i++) {
				if ((Boolean) ui.table.getValueAt(i, 6)) {
					tblSelected.add(ui.table.getValueAt(i, 0).toString());
				}
			}

			Backup runner = new Backup(Export.this, System.currentTimeMillis());
			new Thread(runner).start();
		}
	};

	@Override
	public void actionPerformed(ActionEvent event) {
		renderDialog();
	}

	public void finished(long elapsed) {
		Date date = new Date(elapsed);
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		ui.setTitle(UI.title + " - Backup has been completed.");
		JOptionPane.showMessageDialog(ui,
				"Backup has been completed successfully.\nSpent time: "
						+ formatter.format(date), "Export Completed",
				JOptionPane.INFORMATION_MESSAGE);
		ui.progress.setVisible(false);
	}

}