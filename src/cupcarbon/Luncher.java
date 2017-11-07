package cupcarbon;

public class Luncher extends Thread{
	
	
	private CupCarbon app;
	private String[] args;
	public Luncher(CupCarbon app, String[] args){
		this.app =app;
		this.args =args;
	}
	
	public void run(){
		app.launch(args);
	}

}
