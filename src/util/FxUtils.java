package util;

import javafx.scene.control.Alert;

public class FxUtils {
	public static void showAlert(Alert.AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.setHeaderText(header);
		alert.showAndWait();
	}

	public static void showErrorAlert(String header, String content) {
		showAlert(Alert.AlertType.ERROR, "Error Occurred", header, content);
	}

	public static void showInfoAlert(String header, String content) {
		showAlert(Alert.AlertType.INFORMATION, "City Lodge", header, content);
	}
}
