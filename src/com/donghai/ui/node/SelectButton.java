package com.donghai.ui.node;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SelectButton extends FlowPane
{
	
	private Label label;
	private Label arrow;
	private boolean isSeleted;

	public SelectButton(String text) 
	{
		label = new Label(text);
		label.setFont(new Font(25));
		arrow = new Label("-> ");
		arrow.setFont(new Font(25));
		
		this.getChildren().add(label);
	}
	
	{
		isSeleted = false;
	}
	
	public boolean isSeleted()
	{
		return isSeleted;
	}
	
	public void setSelete(boolean isSeleted)
	{
		this.isSeleted = isSeleted;
		
		if(isSeleted)
		{
			if(!getChildren().get(0).equals(arrow))
			{
				getChildren().add(0, arrow);
				label.setTextFill(Color.BROWN);
			}
		}
		else
		{
			if(getChildren().contains(arrow))
			{
				getChildren().remove(0);
				label.setTextFill(Color.BLACK);
			}
		}
	}

	public String getText() {
		return label.getText();
	}
	
}
