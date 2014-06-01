package SQLExplorer.ui.tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

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
import javax.swing.filechooser.FileNameExtensionFilter;

import SQLExplorer.db.Query;
import SQLExplorer.db.UISQLException;
import SQLExplorer.db.tool.Restore;
import SQLExplorer.ui.UI;
import SQLExplorer.ui.components.FrameFooter;

public class Import implements ActionListener {

	public UI ui;
	private JDialog d;
	public JTextField path, file;
	public JCheckBox force;

	public Import(UI ui) {
		this.ui = ui;
	}

	private void renderDialog() {
		d = new JDialog(ui, "Import Options", true);
		d.setLayout(null);

		final JLabel lforce = new JLabel("Skip Errors");
		lforce.setBounds(10, 10, 120, 25);
		d.add(lforce);

		force = new JCheckBox();
		force.setBounds(95, 12, 20, 20);
		force.setSelected(true);
		d.add(force);

		final JLabel lfile = new JLabel("File to Import");
		lfile.setBounds(10, 40, 120, 25);
		d.add(lfile);

		path = new JTextField(System.getProperty("user.home"));
		path.setBounds(95, 40, 215, 25);
		d.add(path);

		final JButton choose = new JButton("Choose");
		choose.setBounds(310, 40, 80, 25);
		d.add(choose);

		choose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {				
				JFileChooser chooser = DirChooser.dialog(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "SQL", "sql");
				    chooser.setFileFilter(filter);
				final int fch = chooser.showOpenDialog(ui);
				if (fch == JFileChooser.APPROVE_OPTION) {
					path.setText(chooser.getSelectedFile().toString());
				} else if (fch == JFileChooser.CANCEL_OPTION) {
					chooser.setVisible(false);
				}				
			}
		});

		final JButton create = new JButton("Import");
		create.setBounds(120, 75, 90, 25);
		d.add(create);
		create.addActionListener(run);

		final JButton cancel = new JButton("Cancel");
		cancel.setBounds(210, 75, 90, 25);
		d.add(cancel);
		cancel.addActionListener(close);

		d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		d.setResizable(false);
		d.setSize(400, 145);
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
			FrameFooter.progress.setVisible(true);
			Restore runner = new Restore(Import.this,
					System.currentTimeMillis());
			new Thread(runner).start();
		}
	};

	@Override
	public void actionPerformed(ActionEvent event) {
		renderDialog();
	}

	public void finished(long elapsed) {
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
		Date date = new Date(elapsed);
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));		
		JOptionPane.showMessageDialog(ui,
				"Database restore has been completed successfully.\nSpent time: "
						+ formatter.format(date), "Import Completed",
				JOptionPane.INFORMATION_MESSAGE);
		FrameFooter.progress.setVisible(false);		
	}
}
