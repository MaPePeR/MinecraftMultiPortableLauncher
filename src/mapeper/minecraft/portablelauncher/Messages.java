package mapeper.minecraft.portablelauncher;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.swing.JOptionPane;

public class Messages {
	/**
	 * Shows a {@link JOptionPane#WARNING_MESSAGE WARNING_MESSAGE} using
	 * {@link JOptionPane}.<br>
	 * If {@link GraphicsEnvironment#isHeadless()} is true the message is
	 * printed to {@link System#out}
	 * 
	 * @param warning
	 *            Message
	 */
	public static void showWarning(String warning) {
		if (GraphicsEnvironment.isHeadless())
			System.out.println("WARNING: " + warning);
		else
			JOptionPane.showMessageDialog(null, warning,
					"Minecraft Multi Portable Launcher",
					JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Shows a {@link JOptionPane#ERROR_MESSAGE ERROR_MESSAGE} using
	 * {@link JOptionPane}.<br>
	 * If {@link GraphicsEnvironment#isHeadless()} is true the message is
	 * printed to {@link System#err}
	 * 
	 * @param error
	 *            Message
	 */
	public static void showError(String error) {
		if (GraphicsEnvironment.isHeadless())
			System.err.println("ERROR: " + error);
		else
			JOptionPane.showMessageDialog(null, error,
					"Minecraft Multi Portable Launcher",
					JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Presents a Dialog with the possibilities to:
	 * <ul>
	 * <li>Open the Minecraft <a href="http://minecraft.net/download">Download
	 * Page</a> in the Browser</li>
	 * <li>Open the
	 * Download-Link(https://s3.amazonaws.com/MinecraftDownload/launcher
	 * /minecraft.jar) in the Browser</li>
	 * <li>Download the Launcher using the internal Downloader</li>
	 * </ul>
	 * The Default-Browser is launched using {@link Desktop#browse(URI)}
	 * 
	 * @param launcherFile
	 *            where to save the File when using the internal Downloader
	 * @see JOptionPane#showOptionDialog(java.awt.Component, Object, String,
	 *      int, int, javax.swing.Icon, Object[], Object)
	 * @see DownloadFrame#showDownloadFrame(String, URL, File)
	 */
	public static void showLauncherDownload(File launcherFile) {
		String[] options = new String[] { "Open Download Page",
				"Download with Browser", "Download" };
		int selection = JOptionPane
				.showOptionDialog(
						null,
						"You need to download the minecraft.jar-Launcher and place it next to this program.\nHow do you want to do this?",
						"Minecraft Multi Portable Launcher",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, null);
		// TODO: check for Headless
		if (selection == 0) {//Open Download Page
			try {
				Desktop.getDesktop().browse(Constants.getDownloadPageURI());
			} catch (IOException e) {
				showError("Failed to launch your Default-Browser");
				e.printStackTrace();
			}
		} else if (selection == 1) {//Download with Browser
			try {
				Desktop.getDesktop().browse(Constants.getLauncherJarURI());
			} catch (IOException e) {
				showError("Failed to launch your Default-Browser");
				e.printStackTrace();
			}
		} else if (selection == 2) {//Download
			if(DownloadFrame.showDownloadFrame("Downloading minecraft.jar-Launcher", Constants.getLauncherJarURL(), launcherFile))
			{
				try {
					MinecraftMultiPortableLauncher.restartApplication();
				}
				catch(Exception e)
				{
					showError("Restarting Application Failed!\n"+e.getMessage());
				}
			}
		}
	}
}
