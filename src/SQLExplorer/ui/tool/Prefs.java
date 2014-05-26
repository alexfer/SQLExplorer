package SQLExplorer.ui.tool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import SQLExplorer.ui.UI;

public class Prefs extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private UI ui;
	private JDialog dialog;
	private Preferences prefs;
	private JTextField pathToMysqldump, pathToMysql;	

	public Prefs(UI ui) {
		super(ui);
		this.ui = ui;
		prefs = Preferences.userNodeForPackage(UI.class);
	}

	private void renderDialog() {

		dialog = new JDialog(ui, "Preferences", true);
		dialog.setLayout(null);
		
		final JLabel lpathToMysql = new JLabel("Path to mysql");
		lpathToMysql.setBounds(10, 10, 120, 25);
		dialog.add(lpathToMysql);

		pathToMysql = new JTextField(prefs.get("mysql", ""));
		pathToMysql.setBounds(135, 10, 215, 25);
		dialog.add(pathToMysql);
		
		final JButton chooseMysql = new JButton("Overview");
		chooseMysql.setBounds(350, 10, 90, 25);
		dialog.add(chooseMysql);

		chooseMysql.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				DirChooser.getInstance();
				JFileChooser chooser = DirChooser.dialog(false);
				int jfch = chooser.showOpenDialog(ui);
				if (jfch == JFileChooser.APPROVE_OPTION) {
					pathToMysql.setText(chooser.getSelectedFile().toString());
				} else if (jfch == JFileChooser.CANCEL_OPTION) {
					chooser.setVisible(false);
				}
			}
		});
		
		final JLabel lpathToMysqldump = new JLabel("Path to mysqldump");
		lpathToMysqldump.setBounds(10, 40, 120, 25);
		dialog.add(lpathToMysqldump);

		pathToMysqldump = new JTextField(prefs.get("mysqldump", ""));
		pathToMysqldump.setBounds(135, 40, 215, 25);
		dialog.add(pathToMysqldump);
		
		final JButton chooseMysqldump = new JButton("Overview");
		chooseMysqldump.setBounds(350, 40, 90, 25);
		dialog.add(chooseMysqldump);

		chooseMysqldump.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				DirChooser.getInstance();
				JFileChooser chooser = DirChooser.dialog(false);
				int jfch = chooser.showOpenDialog(ui);
				if (jfch == JFileChooser.APPROVE_OPTION) {
					pathToMysqldump.setText(chooser.getSelectedFile().toString());
				} else if (jfch == JFileChooser.CANCEL_OPTION) {
					chooser.setVisible(false);
				}
			}
		});

		final JButton create = new JButton("Save");
		create.setBounds(140, 80, 90, 25);
		dialog.add(create);
		create.addActionListener(save);

		final JButton cancel = new JButton("Cancel");
		cancel.setBounds(230, 80, 90, 25);
		dialog.add(cancel);

		cancel.addActionListener(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});

		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setSize(450, 145);
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	private Action save = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			prefs = Preferences.userNodeForPackage(UI.class);
			prefs.put("mysqldump", pathToMysqldump.getText());
			prefs.put("mysql", pathToMysql.getText());
			dialog.dispose();
		}
	};

	@Override
	public void actionPerformed(ActionEvent e) {		
		renderDialog();
	}
}
