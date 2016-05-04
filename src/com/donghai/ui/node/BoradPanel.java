package com.donghai.ui.node;

import com.donghai.ui.SudukuWindow;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class BoradPanel extends Pane {
	public int[][] matrix;
	public int grid_h, grid_w;
	private int paneW;
	private int paneH;

	public BoradPanel(int[][] matrix, int grid_w, int grid_h,int paneW,int paneH) {
		
		this.grid_w = paneW;
		this.grid_h = paneH;
		
		this.matrix = matrix;
		this.grid_w = grid_w;
		this.grid_h = grid_h;
		// log
		// System.out.println("gird_w:"+grid_w+",grid_h:"+grid_h);
		drawBorad();
	}

	private void drawBorad() {

		Rectangle rect = new Rectangle(SudukuWindow.SCREEN_H, SudukuWindow.SCREEN_W);

		rect.setFill(Color.BEIGE);

		getChildren().add(rect);

		for (int i = 0; i <= matrix.length; i++) {
			int y = i * grid_h;
			Line lineRow = new Line(0, i * grid_h, SudukuWindow.SCREEN_W, i * grid_h);
			int x = i * grid_w;
			Line lineCol = new Line(i * grid_w, 0, i * grid_w, SudukuWindow.SCREEN_H);

			if (i % 3 == 0) {
				lineRow.setStrokeWidth(3);
				lineCol.setStrokeWidth(3);
			} else {
				lineRow.setStrokeWidth(1);
				lineCol.setStrokeWidth(1);
			}
			getChildren().add(lineRow);
			getChildren().add(lineCol);
		}
	}

}
