package view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.FxUtils;

public class MainView {

	/*
	 * The method loadWindow loads the MainView. Main.fxml contains MainVew
	 * Layout
	 */
	public void loadWindow(Stage window) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/Main_Page.fxml"));
			Parent root = loader.load();
			Scene mainProgramWindow = new Scene(root);

			window.setTitle("City Lodge");
			window.setScene(mainProgramWindow);
			window.show();

		} catch (IOException e1) {
			FxUtils.showErrorAlert(e1.getLocalizedMessage(), null);
		}
	}

}
