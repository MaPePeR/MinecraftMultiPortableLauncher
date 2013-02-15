package mapeper.minecraft.portablelauncher;

import java.io.File;
import java.util.ArrayList;

public class StartWithJVMArguments {

	public static void main(String[] args) {
		/*Code inspired by net.minecraft.MinecraftLauncher*/
		if ((Runtime.getRuntime().maxMemory() / 1024L / 1024L) > 511.0F)
		{
			System.out.println("No need for more Memory");
			MinecraftMultiPortableLauncher.main(new String[0]);
		}
		else
		{
			try {
				StringBuilder sb = new StringBuilder(
						StartWithJVMArguments.class.getProtectionDomain()
								.getCodeSource().getLocation().toURI()
								.getPath());
				for (String s : ". minecraft.jar bin/minecraft.jar bin/lwjgl_util.jar bin/lwjgl.jar bin/jinput.jar"
						.split(" ")) {
					sb.append(File.separator).append(s);
				}
				String classpath = sb.toString();

				ArrayList<String> commandline = new ArrayList<String>();

				if (System.getProperty("os.name").toLowerCase().contains("win"))
					commandline.add("javaw");
				else {
					commandline.add("java");
				}
				commandline.add("-Xmx1024m");
				commandline.add("-Dsun.java2d.noddraw=true");
				commandline.add("-Dsun.java2d.d3d=false");
				commandline.add("-Dsun.java2d.opengl=false");
				commandline.add("-Dsun.java2d.pmoffscreen=false");

				commandline.add("-classpath");
				commandline.add(classpath);
				commandline.add(MinecraftMultiPortableLauncher.class.getClass().getName());
				ProcessBuilder localProcessBuilder = new ProcessBuilder(
						commandline);
				Process localProcess = localProcessBuilder.start();
				if (localProcess == null)
					throw new Exception("Error starting Process");
			} catch (Exception e) {
				e.printStackTrace();
				MinecraftMultiPortableLauncher.main(new String[0]);
			}
		}
	}

}
