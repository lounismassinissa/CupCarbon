package cupcarbon;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AboutCupCarbonController implements Initializable {
	
	@FXML
	public Button ok;
	
	@FXML
	public Label label;
	
	@FXML
	public void close() {
		Stage stage = (Stage) ok.getScene().getWindow();
		stage.close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		label.setText("CupCarbon v."+CupCarbonVersion.VERSION + " - " + CupCarbonVersion.YEAR);
	}
}
