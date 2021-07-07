package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.CityLodge;
import model.exception.InvalidIdException;
import model.exception.InvalidInputException;
import util.FxUtils;

public class StandardRoomFormController implements Initializable {

	@FXML
	private TextField roomIdField;

	@FXML
	private TextField noOfBedsField;

	@FXML
	private TextField featureSummaryField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void handleSubmitClick(ActionEvent event) {
		Node eNode = (Node) event.getSource();
		Stage window = (Stage) eNode.getScene().getWindow();
		String inputRoomId = roomIdField.getText().trim();
		String inputNoOfBeds = noOfBedsField.getText().trim();
		String inputFeatureSummary = featureSummaryField.getText().trim();
		try {
			if (inputRoomId.length() == 0 || inputNoOfBeds.length() == 0 || inputFeatureSummary.length() == 0) {
				throw new InvalidInputException("Error: Fields can not be empty.");
			}

			int intNoOfBeds = Integer.parseInt(inputNoOfBeds);

			CityLodge.getInstance().performAddStandardRoom(inputRoomId, intNoOfBeds, inputFeatureSummary);
			FxUtils.showInfoAlert("Standard Room Added Successfully", null);
			window.close();
		} catch (InvalidInputException | InvalidIdException e) {
			FxUtils.showErrorAlert(e.getLocalizedMessage(), null);
		}
	}

	public void handleCancelClick(ActionEvent event) {
		Node eNode = (Node) event.getSource();
		Stage window = (Stage) eNode.getScene().getWindow();
		window.close();
	}

}
