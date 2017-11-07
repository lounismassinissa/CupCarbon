package ccl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RealNodeConfig {


	public String ID;
	public String ip;
	public String status;
	public String virtualNode;

	private String filePath;


	public RealNodeConfig(String filePath){
		this.filePath = filePath;

		read(filePath);
	}

	public RealNodeConfig(String ID, String ip, String status, String filePath){
		this.ID = ID;
		this.ip = ip;
		this.status = status;
		this.filePath = filePath;
		this.virtualNode = "null";
	}

	public void read(String filePath){
		BufferedReader br = null;
		FileReader fr = null;

		try {
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);

			String sCurrentLine;
            String[] tab;
			while ((sCurrentLine = br.readLine()) != null) {
				tab = sCurrentLine.split(" ");
				if(tab[0].equals("ID")) ID = tab[1];
				if(tab[0].equals("ip")) ip = tab[1];
				if(tab[0].equals("status")) status = tab[1];
				if(tab[0].equals("virtualNode")) virtualNode = tab[1];
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}

	public void save(){
		try{
		    PrintWriter writer = new PrintWriter(filePath, "UTF-8");
		    writer.println("ID "+ID);
		    writer.println("ip "+ip);
		    writer.println("status "+status);
		    writer.println("virtualNode "+virtualNode);
		    writer.close();
		} catch (IOException e) {
		   // do something
		}
	}

}
