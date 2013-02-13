package mapeper.minecraft.portablelauncher;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class MinecraftMultiPortableLauncher {

	public static void main(String[] args) {
		File currentDir = new File(".");
		File binDir = new File(currentDir, "bin");
		File minecraftJar = new File(binDir, "minecraft.jar");
		File minecraftLauncherJar=new File(currentDir,"minecraft.jar");
		if(minecraftLauncherJar.isFile())
		{
			//Minecraft-Launcher is available
			ClassLoader loader=MinecraftMultiPortableLauncher.class.getClassLoader();
			
			//Load net.minecraft.Util from the minecraft.jar-Launcher
			try {
				Class<?> clazz = loader.loadClass("net.minecraft.Util");
				setFileSingleton(clazz, currentDir);
			} catch (ClassNotFoundException e) {
				//Should not happen
				e.printStackTrace();
				showError("Wrong minecraft.jar next to this Launcher!\nPlease use the Launcher you can download from minecraft.net");
				return;
			}
				
			if(binDir.isDirectory()&&minecraftJar.isFile())
			{
				//Minecraft-Launcher and bin/minecraft.jar found - we can start!
					
				//Load net.minecraft.client.Minecraft from bin/minecraft.jar
				try {
					Class<?> clazz = loader.loadClass("net.minecraft.client.Minecraft");
					setFileSingleton(clazz, currentDir);
				} catch (ClassNotFoundException e) {
					//Should not happen because bin/minecraft.jar exists
					e.printStackTrace();
					showError("Class not found!\nUse the Minecraft-Launcher to download the Minecraft files from minecraft.net\nAfter that restart this Program");
					return;
				}
			}
			else
			{
				//Minecraft-Launcher found, but bin/minecraft.jar not
				//User has to download Minecraft using the Launcher 
				showWarning("Download the Minecraft-Files with the Original-Launcher. \nAfter that restart this Program.");
			}
			
			//Start Minecraft-Launcher
			try {
				Class<?> clazz = loader.loadClass("net.minecraft.LauncherFrame");
				Method main = clazz.getMethod("main", String[].class);
				main.invoke(null, (Object)new String[]{});
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			//Minecraft-Launcher not found
			//User has to download Minecraft-Launcher
			showLauncherDownload(minecraftLauncherJar);
		}

			
	}
	private static boolean setFileSingleton(Class<?> clazz, File file)
	{
		LinkedList<Field> possibleFields = new LinkedList<Field>();
		for(Field f: clazz.getDeclaredFields())
		{
			if(f.getType()==java.io.File.class&&Modifier.isStatic(f.getModifiers()))
			{
				possibleFields.add(f);
			}
		}
		if(possibleFields.size()==1)
		{
			Field f = possibleFields.get(0);
			f.setAccessible(true);
			try {
				if(f.get(null)!=null)
				{
					showWarning("Field already has a Value: "+f.get(null));
				}
				f.set(null, file);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			System.out.println("Inserting successful");
			return true;
		}
		else
		{
			showError("Inserting File Failed!\nFound "+possibleFields.size()+" possible Targets.");
			return false;
		}
	}
	private static void showWarning(String warning)
	{
		if(GraphicsEnvironment.isHeadless())
			System.out.println("WARNING: "+warning);
		else
			JOptionPane.showMessageDialog(null, warning,"Minecraft Multi Portable Launcher",JOptionPane.WARNING_MESSAGE);
	}
	private static void showError(String error)
	{
		if(GraphicsEnvironment.isHeadless())
			System.out.println("ERROR: "+error);
		else
			JOptionPane.showMessageDialog(null, error,"Minecraft Multi Portable Launcher",JOptionPane.ERROR_MESSAGE);
	}
	private static void showLauncherDownload(File launcherFile)
	{
		String[] options = new String[] {"Open Download Page","Download with Browser", "Download" };
		int selection = JOptionPane.showOptionDialog(null, "You need to download the minecraft.jar-Launcher and place it next to this program.\nHow do you want to do this?", "Minecraft Multi Portable Launcher", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,options , null);
		//TODO: check for Headless
		if(selection==0)
		{
			try {
				Desktop.getDesktop().browse(new URI(downloadPage));
			} catch (IOException e) {
				showError("Failed to launch your Default-Browser");
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		else if(selection==1)
		{
			try {
				Desktop.getDesktop().browse(new URI(launcherJar));
			} catch (IOException e) {
				showError("Failed to launch your Default-Browser");
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		else if(selection==2)
		{
			try {
				DownloadFrame.showDownloadFrame("Downloading minecraft.jar-Launcher", new URL(launcherJar),launcherFile );
			} catch (MalformedURLException e) {
				JOptionPane.showMessageDialog(null, "Could not open Downloader","Minecraft Multi Portable Launcher",JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}
	public static final String downloadPage= "http://minecraft.net/download";
	public static final String launcherJar = "https://s3.amazonaws.com/MinecraftDownload/launcher/minecraft.jar";

}
