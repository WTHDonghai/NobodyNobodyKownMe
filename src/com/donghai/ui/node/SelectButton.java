package com.donghai.ui.node;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SelectButton extends Pane {

	private Label label;
	private boolean isSeleted;
	private String arrow = "-> ";
	private String mes;
	
	public SelectButton(String text) {
	
		mes = text;
		label = new Label(text);
		label.setFont(new Font(25));
		label.setTextFill(Color.BLACK);
		this.getChildren().add(label);
	}

	public boolean isSeleted() {
		return isSeleted;
	}

	public void setSelete(boolean isSeleted) {
		this.isSeleted = isSeleted;

		if (isSeleted) {
			if (!getChildren().get(0).equals(arrow)) {
				label.setText(arrow + mes);
				label.setTextFill(Color.BROWN);
			}
		} else {

			label.setText(mes);
			label.setTextFill(Color.BLACK);
		}
	}

	public String getText() {
		return label.getText();
	}

}
