package cupcarbon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import project.Project;

public class SenScriptController implements Initializable{

	@FXML
	private ComboBox<String> txtLoadFileName;
	
	
	@FXML
	private TextField txt_editor;

	@FXML
	private TextField txtFileName;

	@FXML
	private void sendCom() {
		
			String fileName = txtLoadFileName.getValue();
			//String editor = txt_editor.getText();
			
			Project.openFile(null, fileName);
		
	}


	private String correctPath(String path){

		StringBuilder str = new StringBuilder(path);

		char c = 92;
		for(int i = 0 ; i < str.length(); i++){
			if(str.charAt(i)==' '){
				str.insert(i, c);

				i = i + 1;
			}
		}
		return str.toString();
	}


	@FXML
	public void load() {
		if (txtLoadFileName.getSelectionModel().getSelectedIndex() > 0) {
			txtFileName.setText(txtLoadFileName.getSelectionModel().getSelectedItem().toString());
		} else {
			txtFileName.setText("");
		}
	}

	@FXML
	public void save() {
		if(!txtFileName.getText().equals("")) {
				String fileName = txtFileName.getText();

				txtFileName.setText("");

				File scriptFiles = new File(Project.getProjectScriptPath());
				File script = new File(Project.getScriptFileFromName(fileName));
				 try {
					if (!script.createNewFile()) System.out.println("File already exists.");
					String editor = txt_editor.getText();
					Project.openFile(editor, fileName);
				  } catch (IOException e) {
					e.printStackTrace();
				  }

				String[] c = scriptFiles.list();
				txtLoadFileName.getItems().removeAll(txtLoadFileName.getItems());
				for (int i = 0; i < c.length; i++) {
					txtLoadFileName.getItems().add(c[i]);
				}
				CupCarbon.cupCarbonController.initScriptRealNodeGpsEventComboBoxes();

		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initComboBox();
	}

	public void initComboBox() {
		File scriptFiles = new File(Project.getProjectScriptPath());
		String[] c = scriptFiles.list();
		txtLoadFileName.getItems().removeAll(txtLoadFileName.getItems());
		if (scriptFiles.isDirectory() && c != null) {
			for (int i = 0; i < c.length; i++) {
				txtLoadFileName.getItems().add(c[i]);
			}
		}
	}
}
