package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.HiringRecord;
import model.Room;
import model.exception.MaintenanceException;
import util.FxUtils;
import view.CompleteMaintananceView;
import view.RentRoomView;
import view.ReturnRoomView;

public class RoomDetailController implements Initializable {

	@FXML
	ListView<HBox> hiringRecordsListView;

	@FXML
	SplitPane mainWindowSplitPane;

	@FXML
	Label roomIdLabel;

	@FXML
	Label roomStatusLabel;

	@FXML
	Label roomTypeLabel;

	@FXML
	Label noOfBedsLabel;

	@FXML
	Label featureSummaryLabel;

	@FXML
	ImageView roomImageView;

	private ObservableList<HBox> guiHiringRecords;

	private Room selectedRoom;

	public void setSelectedRoom(Room room) {
		this.selectedRoom = room;
		populateRoomDetails();
	}

	private void populateRoomDetails() {
		roomIdLabel.setText(selectedRoom.getRoomId());
		roomTypeLabel.setText(selectedRoom.getRoomType().toString());
		roomStatusLabel.setText(selectedRoom.getRoomStatus().toString());
		noOfBedsLabel.setText(Integer.toString(selectedRoom.getNoOfBeds()));
		featureSummaryLabel.setText(selectedRoom.getFeatureSummary());
		Image image;
		try {
			image = new Image("/view/images/" + selectedRoom.getRoomImageName());
			if (image.isError()) {
				image = new Image("/view/images/image_not_available.png");
			}
		} catch (Exception ex) {
			image = new Image("/view/images/image_not_available.png");
		}

		roomImageView.setImage(image);

		guiHiringRecords.clear();
		guiHiringRecords.add(createHiringRecordHeaderNode());

		List<HiringRecord> hiringRecords = this.selectedRoom.getHiringRecords();
		if (hiringRecords != null) {
			for (int i = 0; i < hiringRecords.size(); i++) {
				guiHiringRecords.add(createHiringRecordListItemNode(hiringRecords.get(i)));
			}
		}
	}

	private HBox createHiringRecordHeaderNode() {
		HBox hContainer = new HBox();
		GridPane grid = new GridPane();
		Label l = new Label("ID");
		l.setMinWidth(150);
		grid.add(l, 0, 0);

		l = new Label("RentDate");
		l.setMinWidth(100);
		grid.add(l, 1, 0);

		l = new Label("ReturnDate");
		l.setMinWidth(100);
		grid.add(l, 2, 0);

		l = new Label("RentalFee");
		l.setMinWidth(100);
		grid.add(l, 3, 0);

		l = new Label("LateFee");
		l.setMinWidth(100);
		grid.add(l, 4, 0);

		hContainer.getChildren().add(grid);
		return hContainer;
	}

	private HBox createHiringRecordListItemNode(HiringRecord hiringRecord) {

		HBox hContainer = new HBox();
		GridPane grid = new GridPane();
		Label l = new Label(hiringRecord.getRecordId());
		l.setMinWidth(150);
		grid.add(l, 0, 0);

		l = new Label(hiringRecord.getRentDate().toString());
		l.setMinWidth(100);
		grid.add(l, 1, 0);

		String returnDate = (hiringRecord.getActualReturnDate() != null) ? hiringRecord.getActualReturnDate().toString()
				: "-";
		l = new Label(returnDate);
		l.setMinWidth(100);
		grid.add(l, 2, 0);

		String rentalFee = (hiringRecord.getRentalFee() != null) ? String.format("%.2f", hiringRecord.getRentalFee())
				: "-";
		l = new Label(rentalFee);
		l.setMinWidth(100);
		grid.add(l, 3, 0);

		String lateFee = (hiringRecord.getLateFee() != null) ? String.format("%.2f", hiringRecord.getLateFee()) : "-";
		l = new Label(lateFee);
		l.setMinWidth(100);
		grid.add(l, 4, 0);

		hContainer.getChildren().add(grid);
		return hContainer;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		guiHiringRecords = FXCollections.observableArrayList();
		hiringRecordsListView.setItems(guiHiringRecords);
	}

	public void handleRentRoomClick(ActionEvent e) {
		RentRoomView view = new RentRoomView(this.selectedRoom);
		view.loadWindow();
		// Refresh the Details after the RentRoomView is closed
		populateRoomDetails();
	}

	public void handleReturnRoomClick(ActionEvent e) {
		ReturnRoomView view = new ReturnRoomView(this.selectedRoom);
		view.loadWindow();
		// Refresh the Details after the ReturnRoomView is closed
		populateRoomDetails();
	}

	public void handleMaintenanceRoomClick(ActionEvent e) {
		try {
			this.selectedRoom.performMaintenance();
			FxUtils.showInfoAlert("Maintenance has been started for the Room " + this.selectedRoom.getRoomId(), null);
			// Refresh the Details after Maintenance is started
			populateRoomDetails();
		} catch (MaintenanceException ex) {
			FxUtils.showErrorAlert(ex.getLocalizedMessage(), null);
		}
	}

	public void handleCompleteMaintenanceRoomClick(ActionEvent e) {
		CompleteMaintananceView view = new CompleteMaintananceView(this.selectedRoom);
		view.loadWindow();
		// Refresh the Details after the CompleteMaintananceView is closed
		populateRoomDetails();
	}
}
