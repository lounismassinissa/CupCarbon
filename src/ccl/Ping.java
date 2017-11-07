package ccl;

import java.io.PrintStream;

public class Ping  extends Thread{
	private PrintStream out;
	public Ping(PrintStream out){
		this.out = out;
	}

	public void run(){
		try {
			sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		out.println(COMMAND.PING);
	}

}
