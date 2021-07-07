package view;

import java.io.IOException;

import controller.RoomDetailController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Room;
import util.FxUtils;

public class RoomDetailView {

	private Room selectedRoom;

	public RoomDetailView(Room r) {
		this.selectedRoom = r;
	}

	/*
	 * The method loadWindow loads the RoomDetailViewForm. RoomDetailView.fxml
	 * contains RoomDetailViewForm Layout
	 */
	public void loadWindow() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/RoomDetailView.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root); // temporary file

			RoomDetailController roomDetailFormController = loader.<RoomDetailController>getController();
			roomDetailFormController.setSelectedRoom(this.selectedRoom);

			Stage dialogBox = new Stage();
			dialogBox.setTitle("Room Details"); // Set the stage title
			dialogBox.setScene(scene); // Place the scene in the stage
			dialogBox.initModality(Modality.APPLICATION_MODAL);
			dialogBox.showAndWait();
		} catch (IOException e1) {
			FxUtils.showErrorAlert(e1.getLocalizedMessage(), null);
		}
	}
}
