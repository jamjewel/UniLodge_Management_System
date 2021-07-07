package view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.FxUtils;

public class StandardRoomFormView {
	/*
	 * The method loadWindow loads the StandardRoomFormView. standroom_form.fxml
	 * contains RentRoomView Layout
	 */
	public void loadWindow() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/standroom_form.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root); // temporary file

			Stage dialogBox = new Stage();
			dialogBox.setTitle("Add a Standard Room"); // Set the stage title
			dialogBox.setScene(scene); // Place the scene in the stage
			dialogBox.initModality(Modality.APPLICATION_MODAL);
			dialogBox.showAndWait();
		} catch (IOException e1) {
			FxUtils.showErrorAlert(e1.getLocalizedMessage(), null);
		}
	}
}
