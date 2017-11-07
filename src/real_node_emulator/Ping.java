package real_node_emulator;

import java.io.PrintStream;

public class Ping extends Thread{
	private PrintStream out;
	public Ping(PrintStream out){
		this.out = out;

	}

	public void run(){

		while(true){
			out.println(COMMAND.PING);

			try {
				this.sleep(2000);
			} catch (InterruptedException e) {
			}
		}
	}

}
