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
import model.exception.MaintenanceException;
import util.DateTime;
import util.FxUtils;

public class CompleteMaintenanceController implements Initializable {

	@FXML
	TextField completionDateField;

	private Room selectedRoom;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Populate with today's date
		DateTime today = new DateTime();
		this.completionDateField.setText(today.getFormattedDate());
	}

	public void setSelectedRoom(Room room) {
		this.selectedRoom = room;
	}

	public void handleSubmit(ActionEvent e) {
		try {
			String completionDate = completionDateField.getText();

			if (completionDate == null || completionDate.isEmpty()) {
				throw new InvalidInputException("All fields are mandatory");
			}
			this.selectedRoom.completeMaintenance(DateTime.parseFormattedDate(completionDate));
			FxUtils.showInfoAlert(selectedRoom.getRoomType() + " Room " + this.selectedRoom.getRoomId()
					+ " has all maintenance operations completed and is now ready for rent", null);
			Node eNode = (Node) e.getSource();
			Stage window = (Stage) eNode.getScene().getWindow();
			window.close();
		} catch (ParseException | MaintenanceException | InvalidInputException ex) {
			FxUtils.showErrorAlert(ex.getLocalizedMessage(), null);
		}

	}

}
