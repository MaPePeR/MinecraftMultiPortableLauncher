package mapeper.minecraft.portablelauncher;

import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class DownloadFrame extends JDialog implements ActionListener {
	private static final long serialVersionUID = 6575404131888157486L;
	JProgressBar progressBar = new JProgressBar();
	JButton cancelButton = new JButton("Cancel");
	private boolean downloadFinished=false;
	DownloadRunnable runnable;
	public final Object lock = new Object();
	public DownloadFrame(String title, URL url, File outFile)
	{
		super((Dialog)null,title);
		this.setLocationRelativeTo(getRootPane());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new DownloadWindowListener());
		cancelButton.addActionListener(this);
		progressBar.setStringPainted(true);
		progressBar.setString("Downloading...");
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridy=1;
		this.add(cancelButton,c);
		c.gridy=0;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.weightx=1;
		this.add(progressBar,c);

		this.setVisible(true);
		
		this.pack();
		
		runnable = new DownloadRunnable(progressBar, url, outFile, this);
		
	}
	public void startDownload()
	{
		new Thread(runnable).start();
	}
	public void setDownloadFinished()
	{
		progressBar.setString("Finished");
		cancelButton.setText("Ok");
		downloadFinished=true;
		this.pack();
	}

	private class DownloadWindowListener extends WindowAdapter
	{
		@Override
		public void windowClosing(WindowEvent e) {
			closeWindow();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		closeWindow();
	}
	private void closeWindow()
	{
		if(downloadFinished)
		{
			DownloadFrame.this.dispose();
			synchronized (lock) {
				lock.notifyAll();
			}
		}
		else
		{
			int selection =JOptionPane.showConfirmDialog(DownloadFrame.this, "Do you really want to abort the Download?","Really abort download?",JOptionPane.YES_NO_OPTION);
			if(selection==JOptionPane.YES_OPTION)
			{
				DownloadFrame.this.runnable.run=false;
				DownloadFrame.this.dispose();
				synchronized (lock) {
					lock.notifyAll();
				}
			}
		}
	}
	public static boolean showDownloadFrame(String title, URL url, File outFile)
	{
		DownloadFrame frame = new DownloadFrame(title, url, outFile);
		synchronized (frame.lock) {
			frame.startDownload();
			try {
				frame.lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return frame.downloadFinished;
		
	}
}
