package view;

import java.io.IOException;

import controller.CompleteMaintenanceController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Room;
import util.FxUtils;

public class CompleteMaintananceView {

	private Room selectedRoom;

	public CompleteMaintananceView(Room room) {
		this.selectedRoom = room;
	}

	/*
	 * The method loadWindow loads the CompleteMaintananceView.
	 * complete_maintenance.fxml contains CompleteMaintananceView Layout
	 */
	public void loadWindow() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/complete_maintenance.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root); // temporary file

			loader.<CompleteMaintenanceController>getController().setSelectedRoom(this.selectedRoom);

			Stage dialogBox = new Stage();
			dialogBox.setTitle("Maintenance Completion"); // Set the stage title
			dialogBox.setScene(scene); // Place the scene in the stage
			dialogBox.initModality(Modality.APPLICATION_MODAL);
			dialogBox.showAndWait();
		} catch (IOException e1) {
			FxUtils.showErrorAlert(e1.getLocalizedMessage(), null);
		}
	}
}
