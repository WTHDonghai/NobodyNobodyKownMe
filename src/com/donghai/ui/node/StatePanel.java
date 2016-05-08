package com.donghai.ui.node;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class StatePanel extends Pane
{
	private Label contentLabel;
	
	public StatePanel(String title,String contentText) 
	{
		contentLabel = new Label(title+contentText);
		contentLabel.setTextFill(Color.BLACK);
		contentLabel.setFont(new Font(20));
		contentLabel.setAlignment(Pos.CENTER);
		this.getChildren().addAll(contentLabel);
		this.setStyle("-fx-background:transparent;");
	}
	
	public StatePanel(Image icon,String contentText) 
	{
		contentLabel = new Label("",new ImageView(icon));
		contentLabel.setText(contentText);
		contentLabel.setTextFill(Color.BLACK);
		contentLabel.setFont(new Font(20));
		
//		new Insets(top, right, bottom, left)
		this.setPadding(new Insets(0, 16, 0, 16));
		this.getChildren().addAll(contentLabel);
	}
	
	public void setColor(Color color)
	{
		this.contentLabel.setTextFill(color);
	}
	
	public void setContentText(String contentText)
	{
		this.contentLabel.setText(contentText);
	}
	
	public String getContentText()
	{
		return contentLabel.getText();
	}

}
