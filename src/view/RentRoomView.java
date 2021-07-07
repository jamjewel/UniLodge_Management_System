package view;

import java.io.IOException;

import controller.RentRoomController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Room;
import util.FxUtils;

public class RentRoomView {

	private Room selectedRoom;

	public RentRoomView(Room room) {
		this.selectedRoom = room;
	}

	/*
	 * The method loadWindow loads the RentRoomView. rent_room.fxml contains
	 * RentRoomView Layout
	 */
	public void loadWindow() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/rent_room.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root); // temporary file

			loader.<RentRoomController>getController().setSelectedRoom(this.selectedRoom);

			Stage dialogBox = new Stage();
			dialogBox.setTitle("Rent a Room"); // Set the stage title
			dialogBox.setScene(scene); // Place the scene in the stage
			dialogBox.initModality(Modality.APPLICATION_MODAL);
			dialogBox.showAndWait();
		} catch (IOException e1) {
			FxUtils.showErrorAlert(e1.getLocalizedMessage(), null);
		}
	}
}
