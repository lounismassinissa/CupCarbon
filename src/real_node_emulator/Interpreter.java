package real_node_emulator;

import java.io.File;

import org.python.util.PythonInterpreter;

import project.Project;

public class Interpreter extends Thread {


	private PythonInterpreter interp;
	private String script;
	private Receiver com;


	public Interpreter(Receiver r,String script){
		this.script = script;
		this.com = r;
	}

	public void run(){
		interp = new PythonInterpreter();
		System.out.println("Script to run: "+Project.getProjectScriptPath()+File.separator + script);
		interp.execfile(Project.getProjectScriptPath()+File.separator+script);
	}

}
