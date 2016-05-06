package com.donghai.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.donghai.ui.node.SelectButton;

public class Logo extends StackPane {

	private VBox buttonBox;
	private SelectButton[] difficutlySelections;
	private int currentSelected;

	public Logo() {

		try {
			initView();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void initView() throws FileNotFoundException {

		String pathname = "menuLog.png";
		FileInputStream fis = new FileInputStream(new File(pathname));
		Image img = new Image(fis);
		ImageView background = new ImageView(img);

		// StackPane contentP = new StackPane();

		Rectangle r = new Rectangle(630, 630);
		r.setFill(Color.WHITE);

		this.getChildren().add(r);
		this.getChildren().add(background);

		buttonBox = new VBox(6.5);
		String[] dificultyText = { "EASY", "NORMAL", "HARD", "EVIL" };
		difficutlySelections = new SelectButton[4];
		for (int i = 0; i < dificultyText.length; i++) {

			SelectButton selectButton = new SelectButton(dificultyText[i]);

			selectButton.setOnMouseClicked(e -> {

				difficutlyMenuClear();
				selectButton.setSelete(true);

			});

			if (i == 0) {
				selectButton.setSelete(true);
				currentSelected = 0;
			}

			buttonBox.getChildren().add(selectButton);
			difficutlySelections[i] = selectButton;
		}

		buttonBox.setLayoutX(152);
		buttonBox.setLayoutY(278);

		Pane menuP = new Pane();
		menuP.getChildren().add(buttonBox);

		this.getChildren().add(menuP);
		menuP.setFocusTraversable(true);
		menuP.setOnKeyPressed(e -> {

			selectionMenu(e.getCode().getName());

			// startGame
			if ("Enter".equals(e.getCode().getName()) && currentSelected != -1) {
				// get current difficulty
				System.out.println(difficutlySelections[currentSelected].getText());
			}

		});
	}

	private void selectionMenu(String name) {

		difficutlyMenuClear();

		switch (name) {
		case "Up":

			currentSelected--;

			if (currentSelected < 0)
				currentSelected = 4 - 1;

			difficutlySelections[currentSelected].setSelete(true);

			break;
		case "Down":

			currentSelected++;
			difficutlySelections[currentSelected %= difficutlySelections.length].setSelete(true);

			break;
		case "Left":
		case "Right":
			currentSelected = -1;
			break;
		}

	}

	private void difficutlyMenuClear() {
		for (SelectButton selectBtn : difficutlySelections) {
			selectBtn.setSelete(false);
		}
	}
}
