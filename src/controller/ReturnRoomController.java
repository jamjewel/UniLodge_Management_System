package controller;

import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Room;
import model.exception.InvalidInputException;
import model.exception.ReturnException;
import util.DateTime;
import util.FxUtils;

public class ReturnRoomController implements Initializable {

	@FXML
	TextField returnDateField;

	private Room selectedRoom;

	public void setSelectedRoom(Room room) {
		this.selectedRoom = room;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Populate with today's date
		DateTime today = new DateTime();
		this.returnDateField.setText(today.getFormattedDate());
	}

	public void handleSubmit(ActionEvent e) {
		try {
			String returnDate = returnDateField.getText();

			if (returnDate == null || returnDate.isEmpty()) {
				throw new InvalidInputException("All fields are mandatory");
			}
			selectedRoom.returnRoom(DateTime.parseFormattedDate(returnDate));
			FxUtils.showInfoAlert("Room " + selectedRoom.getRoomId() + " is now returned", null);
			Node eNode = (Node) e.getSource();
			Stage window = (Stage) eNode.getScene().getWindow();
			window.close();
		} catch (ParseException | ReturnException | InvalidInputException ex) {
			FxUtils.showErrorAlert(ex.getLocalizedMessage(), null);
		}

	}

}
