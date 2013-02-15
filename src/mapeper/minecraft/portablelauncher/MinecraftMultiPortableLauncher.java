package mapeper.minecraft.portablelauncher;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class MinecraftMultiPortableLauncher {

	public static void main(String[] args) {
		File currentDir = new File(".");
		File binDir = new File(currentDir, "bin");
		File minecraftJar = new File(binDir, "minecraft.jar");
		File minecraftLauncherJar = new File(currentDir, "minecraft.jar");
		if (minecraftLauncherJar.isFile()) {
			// Minecraft-Launcher is available
			ClassLoader loader = MinecraftMultiPortableLauncher.class
					.getClassLoader();

			// Load net.minecraft.Util from the minecraft.jar-Launcher
			try {
				Class<?> clazz = loader.loadClass("net.minecraft.Util");
				setFileSingleton(clazz, currentDir);
			} catch (ClassNotFoundException e) {
				// Should not happen
				e.printStackTrace();
				Messages.showError("Wrong minecraft.jar next to this Launcher or classpath incomplete!\n"
						+ "Please use the Launcher you can download from minecraft.net\n");
				return;
			} catch (RuntimeException e) {
				Messages.showError("You use a modified Minecraft-Launcher or a Minecraft-Patch broke this Launcher. \n"
						+ "Please check if this issue already has been reportet.");
				return;
			}

			if (binDir.isDirectory() && minecraftJar.isFile()) {
				// Minecraft-Launcher and bin/minecraft.jar found - we can
				// start!

				// Load net.minecraft.client.Minecraft from bin/minecraft.jar
				try {
					Class<?> clazz = loader
							.loadClass("net.minecraft.client.Minecraft");
					setFileSingleton(clazz, currentDir);
				} catch (ClassNotFoundException e) {
					// Should not happen because bin/minecraft.jar exists
					e.printStackTrace();
					Messages.showError("Class not found!\n"
							+ "Use the Minecraft-Launcher to download the Minecraft files from minecraft.net\n"
							+ "After that restart this Program");
					return;
				} catch (RuntimeException e) {
					Messages.showError("You use a modified Minecraft-Version or a Minecraft-Patch broke this Launcher. \n"
							+ "Please check if this issue already has been reportet.");
					return;
				}
			} else {
				// Minecraft-Launcher found, but bin/minecraft.jar not
				// User has to download Minecraft using the Launcher
				Messages.showWarning("Download Minecraft with the Original-Launcher. \n"
						+ "RESTART THIS PROGRAM WHEN THE DOWNLOAD IS FINISHED!");
			}

			// Start Minecraft-Launcher
			try {
				Class<?> clazz = loader
						.loadClass("net.minecraft.LauncherFrame");
				Method main = clazz.getMethod("main", String[].class);
				main.invoke(null, (Object) new String[] {});
			} catch (Exception e) {
				Messages.showError("An Error occured: " + e.getMessage());
			}
		} else {
			// Minecraft-Launcher not found
			// User has to download Minecraft-Launcher
			Messages.showLauncherDownload(minecraftLauncherJar);
		}

	}

	/**
	 * Scans the class for a <b>static</b> Field of type <b>java.io.File</b>.<br/>
	 * If only one Field is found it will be modified.<br/>
	 * If more or less than one Field match the criteria a RuntimeException will
	 * be thrown If the Field already has a value a Warning will be shown using
	 * {@link #showWarning(String)}
	 * 
	 * @param clazz
	 *            The class-Object to scan
	 * @param file
	 *            the value for the field
	 * @throws RuntimeException
	 *             when the criteria does not result in clear target-Field
	 * 
	 */
	private static void setFileSingleton(Class<?> clazz, File file) {
		LinkedList<Field> possibleFields = new LinkedList<Field>();
		for (Field f : clazz.getDeclaredFields()) {
			if (f.getType() == java.io.File.class
					&& Modifier.isStatic(f.getModifiers())) {
				// Get all static Fields with Type "java.io.File"
				// and add them to possibleFields
				possibleFields.add(f);
			}
		}
		if (possibleFields.size() == 1) {
			// Only found one Field so i can make a clear decision
			Field f = possibleFields.get(0);
			try {
				f.setAccessible(true);
				if (f.get(null) != null) {
					Messages.showWarning("Field already has a Value: " + f.get(null));
				}
				f.set(null, file);
			} catch (Exception e) {
				throw new RuntimeException("An unexpected Error occured", e);
			}
			System.out.println("Inserting successful");
		} else {
			// possibleFields is unclear!
			throw new RuntimeException("Found " + possibleFields.size()
					+ " possible Targets.");
		}
	}





	
	/**
	 * Restarts this Application-Jar<br/>
	 * Thanks to "Veger" on Stackoverflow for this Solution: <a href="http://stackoverflow.com/a/4194224">http://stackoverflow.com/a/4194224</a>
	 * @throws Exception
	 */
	public static void restartApplication() throws Exception
	{
	  final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
	  final File currentJar = new File(MinecraftMultiPortableLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI());

	  /* is it a jar file? */
	  if(!currentJar.getName().endsWith(".jar"))
	    throw new Exception("Program was not started from Jar");

	  /* Build command: java -jar application.jar */
	  final ArrayList<String> command = new ArrayList<String>();
	  command.add(javaBin);
	  command.add("-jar");
	  command.add(currentJar.getPath());

	  final ProcessBuilder builder = new ProcessBuilder(command);
	  builder.start();
	  System.exit(0);
	}
}
