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
import model.exception.RentException;
import util.DateTime;
import util.FxUtils;

public class RentRoomController implements Initializable {

	@FXML
	TextField numberOfDaysField;

	@FXML
	TextField customerIdField;

	@FXML
	TextField rentDateField;

	private Room selectedRoom;

	public void setSelectedRoom(Room room) {
		this.selectedRoom = room;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Populate with today's date
		DateTime today = new DateTime();
		this.rentDateField.setText(today.getFormattedDate());
	}

	public void handleSubmit(ActionEvent e) throws InvalidInputException {
		try {
			String customerId = customerIdField.getText();
			String rentDate = rentDateField.getText();
			String numberOfDays = numberOfDaysField.getText();
			if (customerId == null || customerId.isEmpty() || rentDate == null || rentDate.isEmpty()
					|| numberOfDays == null || numberOfDays.isEmpty()) {
				throw new InvalidInputException("All fields are mandatory");
			}
			this.selectedRoom.rent(customerId, DateTime.parseFormattedDate(rentDate), Integer.parseInt(numberOfDays));
			FxUtils.showInfoAlert("Room " + this.selectedRoom.getRoomId() + " is now rented by Customer " + customerId,
					null);
			Node eNode = (Node) e.getSource();
			Stage window = (Stage) eNode.getScene().getWindow();
			window.close();
		} catch (ParseException | NumberFormatException | RentException ex) {
			FxUtils.showErrorAlert(ex.getLocalizedMessage(), null);
		}
	}

}
