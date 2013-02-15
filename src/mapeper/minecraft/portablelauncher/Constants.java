package mapeper.minecraft.portablelauncher;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Constants {
	/**
	 * Hardcoded URL to the minecraft.jar-Launcher (On Amazon S3)
	 */
	public static final String launcherJar = "https://s3.amazonaws.com/MinecraftDownload/launcher/minecraft.jar";
	private static URL launcherJarURL;
	private static URI launcherJarURI;
	public static URL getLauncherJarURL()
	{
		if(launcherJarURL==null)
		{
			try {
				launcherJarURL=new URL(launcherJar);
			} catch (MalformedURLException e) {
				Messages.showError("This Error Should never occur.\n"+e.getMessage());
			}
			
		}
		return launcherJarURL;
	}
	
	public static URI getLauncherJarURI()
	{
		if(launcherJarURI==null)
		{
			try {
				launcherJarURI=new URI(launcherJar);
			} catch (URISyntaxException e) {
				Messages.showError("This Error Should never occur.\n"+e.getMessage());
			}
		}
		return launcherJarURI;
	}
	
	/**
	 * Hardcoded URL to the Minecraft-Download-Page
	 */
	public static final String downloadPage = "http://minecraft.net/download";
	private static URI downloadPageURI;
	public static URI getDownloadPageURI()
	{
		if(downloadPageURI==null)
		{
			try {
				downloadPageURI=new URI(downloadPage);
			} catch (URISyntaxException e) {
				Messages.showError("This Error Should never occur.\n"+e.getMessage());			}
		}
		return downloadPageURI;
	}
}
