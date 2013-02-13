package mapeper.minecraft.portablelauncher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class DownloadRunnable implements Runnable{

	private final static int BUFSIZE=10*1024;
	JProgressBar progressBar;
	URL url;
	File outFile;
	DownloadFrame downloadFrame;
	public DownloadRunnable(JProgressBar progressBar, URL url, File outFile, DownloadFrame downloadFrame)
	{
		this.url=url;
		this.progressBar=progressBar;
		this.outFile=outFile;
		this.downloadFrame=downloadFrame;
		if(progressBar==null)
			throw new NullPointerException();
	}
	public volatile boolean run=true;
	public boolean successfull=false;
	@Override
	public void run() {
		int size;
		int sumCount=0;
		try {
			URLConnection connection = url.openConnection();
			connection.connect();
			
			size=connection.getContentLength();
			if(size==-1)
			{
				progressBar.setIndeterminate(true);
			}
			else
			{
				progressBar.setMaximum(size);
				progressBar.setString(null);
			}

			
			InputStream is = connection.getInputStream();
			FileOutputStream fout = new FileOutputStream(outFile);
			int count;
			byte buffer[]=new byte[BUFSIZE];
			while(run&&(count=is.read(buffer))!=-1)
			{
				fout.write(buffer, 0, count);
				sumCount+=count;
				progressBar.setValue(sumCount);
			}
			fout.close();
			if(!run)//We were aborted -> outfile is useLess/broken - delete it
				outFile.delete();
			else
				downloadFrame.setDownloadFinished();
			is.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(downloadFrame, e.getMessage());
			e.printStackTrace();
		}
		
	}
}
