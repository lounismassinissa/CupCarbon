package ccl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import project.Project;

public class FileTransmeter extends Thread {
	private Socket data_socket;
	
	private String realNodeId;
	
	public FileTransmeter(Socket data_socket, String id) {
		
		this.data_socket = data_socket;
		realNodeId = id;
	}
	
	
	public void run() {
		 BufferedReader fileReader;
		  try {
			PrintStream data_out = new PrintStream(data_socket.getOutputStream());
			String sCurrentLine;

			File scriptsFolderFile = new File(Project.getProjectScriptPath());
			String[] scripts = scriptsFolderFile.list();
			for (int i = 0; i < scripts.length; i++) {
				if (scripts[i] != null)
					if (!scripts[i].startsWith(".")){
						fileReader = new BufferedReader(new FileReader(Project.getProjectScriptPath()+File.separator+scripts[i]));
						data_out.println("file "+scripts[i]);
						while ((sCurrentLine = fileReader.readLine()) != null) {
							data_out.println(sCurrentLine);
						}
						data_out.println("EOF");
					}

			}
			data_out.println("EOD");
		  } catch (IOException e) {
			  e.printStackTrace();
		  }
	}

}
