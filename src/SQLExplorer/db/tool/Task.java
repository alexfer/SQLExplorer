package SQLExplorer.db.tool;

import javax.swing.SwingUtilities;

import SQLExplorer.ui.UI;

public class Task extends Thread {
	UI ui;

	public Task(UI ui) {
		super();
		this.ui = ui;
	}

	public void run() {
		for (int i = 0; i <= 100; i += 10) {
			final int progress = i;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					//ui.progressBar.setValue(progress);
				}
			});
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				UI.logger.info(e.getMessage());
			}
		}
	}
}
