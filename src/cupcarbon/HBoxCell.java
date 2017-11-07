package cupcarbon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


import ccl.RealNodeConfig;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import map.MapLayer;
import project.Project;

public class HBoxCell extends HBox {

    private Button label = new Button();
    private ComboBox realNodeslist = new ComboBox();
    public Button run = new Button();
    public Button stop = new Button();
    private CupCarbonController app;
    private ObservableList myItems;

    private ImageView connectionStat;
    private String virtualNodeId;
    private boolean first = false;

    HBoxCell(String virtualNodeId, ObservableList allItems) {
         super();
         this.app =app;

         this.virtualNodeId = virtualNodeId;
         String current;

			try {
				current = new java.io.File( "." ).getCanonicalPath();
				FileInputStream imageStream = new FileInputStream(current+File.separator+"tiles"+File.separator+"offline.png");
				Image image = new Image (imageStream );
				this.connectionStat = new ImageView(image);
				connectionStat.setFitHeight(20);
				connectionStat.setFitWidth(20);

			} catch (IOException e) {
				System.err.println("error load icon image");
			}


         myItems = FXCollections.observableArrayList (allItems);

         label.setText("S"+virtualNodeId);
         label.setStyle("-fx-background-color: #73889e");


         run.setText("Run");
         stop.setText("Stop");
         stop.setDisable(true);

         // Handle run Button event.
         run.setOnAction((event) -> {
        	 if(isRealNodeConnected()){
        		 run.setDisable(true);
        		 String realNodeCurrentID = getSelectedElementCurrentID();
        		 if(realNodeCurrentID.equals("virtual")){
        			 if(!CupCarbon.server.isEmulatedNode(virtualNodeId)){
        				 CupCarbon.server.createRealNodeEmulator(virtualNodeId);
        			 }
        			 Starter start =  new Starter(virtualNodeId);
        			 start.start();


        		 }else{
	        		 CupCarbon.server.synchroRealAndVirtualNode(realNodeCurrentID, virtualNodeId);
	        		 CupCarbon.server.sendScript(realNodeCurrentID);
	        	 }
        		 stop.setDisable(false);

        	 }
         });

        // Handle stop Button event.
         stop.setOnAction((event) -> {
        	 if(isRealNodeConnected()){
        		 stop.setDisable(true);
        		 String realNodeCurrentID = getSelectedElementCurrentID();
        		 if(realNodeCurrentID.equals("virtual")){
        			 CupCarbon.server.stopScript(virtualNodeId);
        			 CupCarbon.cupCarbonController.getSensorNodeById(Integer.valueOf(virtualNodeId)).setMarked(false);
        		 }else{
        			 CupCarbon.server.stopScript(realNodeCurrentID);
        			 CupCarbon.cupCarbonController.getSensorNodeById(Integer.valueOf(virtualNodeId)).setMarked(false);
        		 }
        		 run.setDisable(false);
        	 }
        	 MapLayer.repaint();
         });

         realNodeslist = new ComboBox(myItems);
         realNodeslist.setPrefWidth(100);
         realNodeslist.getSelectionModel().select(itemPos("virtual"));

         this.setHgrow(label, Priority.ALWAYS);

         this.setSpacing(3);

         this.getChildren().addAll(label, realNodeslist, run, stop,connectionStat);
         chooseRealNode();
    }

    public String getSelectedElementCurrentID(){
    	String name = (String) realNodeslist.getValue();

	    if(!name.equals("virtual")){
	    		String path = Project.getRealNodePath();
	    		String realNodeFileName = path +File.separator+ name;
	    		RealNodeConfig config= new RealNodeConfig(realNodeFileName);
	    		return config.ID;
	    }else{
	    		return name;
	    }
    }

    public boolean isRealNodeConnected(){

    	String name = (String) realNodeslist.getValue();
    	if(name.equals("virtual")){
    		return true;
    	}else{
	    	String path = Project.getRealNodePath();
			String realNodeFileName = path +File.separator+ name;
	    	RealNodeConfig config= new RealNodeConfig(realNodeFileName);
	    	if(config.status.equals("offline")){
	    		Alert alert = new Alert(AlertType.ERROR);
	    		alert.setTitle("Error:");
	    		alert.setHeaderText("The Selected Device is Not Connected");
	    		alert.setContentText("Connect Device "+config.ID+" and try again");
	    		alert.showAndWait();
	    		return false;
	    	}
	    	return true;
    	}

    }

    public String getVirtualNodeId(){
    	return virtualNodeId;
    }

    public void chooseRealNode(){
    	File realNodeFiles = new File(Project.getRealNodePath());
		String[] s = realNodeFiles.list();
		String node = "";
		if (s == null)
			s = new String[1];

		for (int i = 0; i < s.length; i++) {
			if (s[i] != null)
				if (!s[i].startsWith(".")){
						node = s[i].substring(0, s[i].length()-3);

						if(virtualNodeId.equals(node)){
							realNodeslist.getSelectionModel().select(itemPos(s[i]));
						}
				}
		}
    }


    public void setSelectedItem(String item){
    	this.myItems.add(item);
    	realNodeslist.getSelectionModel().select(itemPos(item));
    }

    private int itemPos(String item){
    	for(int i = 0; i < myItems.size(); i++ ){
    		if(item.equals(myItems.get(i))) return i;
    	}
    	return 0;
    }

    public void updateConnection(){
    	String current;

    	FileInputStream imageStream;
    	String name = (String) realNodeslist.getValue();
    	try {
    		current = new java.io.File( "." ).getCanonicalPath();
    		if(!name.equals("virtual")){
    			String path = Project.getRealNodePath();
    			String realNodeFileName = path +File.separator+ name;
    			RealNodeConfig config= new RealNodeConfig(realNodeFileName);

				if(config.status.equals("offline")){
					imageStream = new FileInputStream(current+File.separator+"tiles"+File.separator+"offline.png");
				}else{
					imageStream = new FileInputStream(current+File.separator+"tiles"+File.separator+"online.png");
				}
				Image image = new Image (imageStream );
				this.connectionStat.setImage(image);

	    	}else{
	    		imageStream = new FileInputStream(current+File.separator+"tiles"+File.separator+"online.png");
	    		Image image = new Image (imageStream );
				this.connectionStat.setImage(image);

	    	}
    	} catch (IOException e) {
			System.err.println("error load icon image");
		}
    }


}