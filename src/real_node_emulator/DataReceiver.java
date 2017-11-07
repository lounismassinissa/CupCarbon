package real_node_emulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class DataReceiver implements Runnable {


	private Socket s;
	private Thread _t;
	private BufferedReader data_in;
	private PrintWriter fileWriter;
	private Receiver r;

	public DataReceiver(Receiver r,Socket s){
		this.r = r;
		this.s = s;
		_t = new Thread(this);
	    _t.start();
	}

	@Override
	public void run() {
		try {
			data_in= new BufferedReader(new InputStreamReader(s.getInputStream()));

			String sCurrentLine;
			boolean stop = false;
			String[] tab;
			System.out.println("**************************************************************************");
			while (!stop && (sCurrentLine = data_in.readLine()) != null) {

				if(sCurrentLine.equals("EOD")){
					stop = true;
					r.endOfData();

				}else{
					tab = sCurrentLine.split(" ");
					if(tab[0].equals("file")){
						System.out.println("receive file:"+tab[1]);
						fileWriter = new PrintWriter(r.client.path+File.separator+tab[1], "UTF-8");

					}else{
						if(sCurrentLine.equals("EOF")){
							fileWriter.close();
						}else{
							fileWriter.println(sCurrentLine);
						}
					}
				}
			}
			System.out.println("**************************************************************************");

		  } catch (IOException e) {
			  System.out.println("data receiver !!!");
		  }

	}
	public void close(){
		try {

			data_in.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void stop(){
		_t.interrupt();
	}

}
