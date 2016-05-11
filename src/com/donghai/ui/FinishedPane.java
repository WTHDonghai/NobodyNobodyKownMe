package com.donghai.ui;


import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class FinishedPane extends Pane {
	
	private StackPane content;
	private Rectangle paseBackground;
	private StackPane[] items = new StackPane[2];
	private int position=-1;
	SudukuWindow game;
	
	public FinishedPane(SudukuWindow game) {
		initView();
		initListener();
		this.game = game;
		
		this.getChildren().add(content);
	}

	private void initListener() 
	{
		content.setOnMouseClicked(e->{
			
			if(position<0)
				return;
			
			String com = ((Label)(items[position].getChildren().get(0))).getText();
			
			switch (com) {
			case "Back":
				back();
				break;
				
			case "Next":
				next();
				break;
			}
			
		});
		
	}

	public void initView() {
		// ----------------------------pause pane
		content = new StackPane();
		paseBackground = new Rectangle(630, 630);
		paseBackground.setFill(new Color(0.5, 0.5, 0.5, .7));
		content.getChildren().add(paseBackground);

		Label text = new Label("Congratulation,You Finsh It");
		text.setTextFill(Color.WHITE);
		text.setFont(Font.font(35));
		text.setAlignment(Pos.CENTER);
		text.setTextAlignment(TextAlignment.CENTER);
		
		
		VBox vbox = new VBox(16.0);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().add(text);

		HBox item = initItem();
		vbox.getChildren().add(item);

		item.setFocusTraversable(true);
		item.setOnKeyPressed(e -> {

			String name = e.getCode().getName();

			clear();
			if (name.equals("Left")) {

				addFocus(items[position]);

				if (position == 0)
					position = 1;
				else
					position--;

			} else if (name.equals("Right")) {

				addFocus(items[position]);

				if (position == 1)
					position = 0;
				else
					position++;
			}
		});
		content.getChildren().add(vbox);
		clear();
	}

	private void addFocus(StackPane item) {

		Label label = (Label) item.getChildren().get(0);
		label.setTextFill(Color.BROWN);
	}

	/**
	 * clear
	 */
	public void clear() {
		for (StackPane stackPane : items) {

			Label node = (Label) stackPane.getChildren().get(0);
			node.setTextFill(Color.WHITE);
		}
	}

	/**
	 * 初始化两个选项
	 */
	public HBox initItem() {

		// 返回
		Label back = new Label("Back");
		back.setTextFill(Color.WHITE);
		back.setFont(Font.font(35));
		StackPane backP = new StackPane();
		backP.getChildren().add(back);

		back.setOnMouseEntered(e -> {
			clear();
			position = 0;
			addFocus(backP);
		});

		back.setOnMouseExited(e -> {
			position = -1;
			clear();
		});

		// 下一个按钮
		Label next = new Label("Next");
		next.setTextFill(Color.WHITE);
		next.setFont(Font.font(35));
		StackPane nextP = new StackPane();
		nextP.getChildren().add(next);

		next.setOnMouseEntered(e -> {
			clear();
			position = 1;
			addFocus(nextP);
		});

		next.setOnMouseExited(e -> {
			position = -1;
			clear();
		});

		HBox hbox = new HBox(120);
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().add(backP);
		hbox.getChildren().add(nextP);

		items[0] = backP;
		items[1] = nextP;

		return hbox;
	}

	
	protected void next()
	{
		game.nextPazzle();
	}
	protected void back()
	{
		game.backHome();
	}
	
	public static void main(String[] args) {

		Application.launch(args);
	}
}
