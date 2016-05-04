package com.donghai.ui.node;

import com.donghai.ui.SudukuWindow;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends Label {

	private boolean isSelected;// 是否被选中
	private Rectangle focus;
	private SudukuWindow sudoku;
	private int row;
	private int col;
	private Rectangle hightLight;
	public boolean isLight;

	public Cell(SudukuWindow sudoku, int row, int col) {

		this.sudoku = sudoku;
		isSelected = false;
		focus = new Rectangle(SudukuWindow.SCREEN_W / 9, SudukuWindow.SCREEN_H / 9);
		focus.setFill(Color.color(0.4, 0.3, 0.5, 0.5));
		focus.setStroke(Color.BLACK);
		focus.setStrokeWidth(4);

		hightLight = new Rectangle(SudukuWindow.SCREEN_W / 9, SudukuWindow.SCREEN_W / 9);
		hightLight.setFill(new Color(.9, .0, .0, .1));

		this.setPrefWidth(sudoku.grid_w);
		this.setPrefHeight(sudoku.grid_h);
		this.setAlignment(Pos.BASELINE_CENTER);

		this.row = row;
		this.col = col;

		this.setOnMouseClicked(e -> handleMouseClick());
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	private void handleMouseClick() {
		clearSelected();
		isSelected = !isSelected;
		drawFocus(isSelected);
		sudoku.drawHighLightArea();
	}

	public void drawFocus(boolean isSelect) {

		if (isSelected) {// 被选择，获得焦点
			if (!getChildren().contains(focus))
			{
				sudoku.currentCol = this.col;
				sudoku.currentRow = this.row;
			}
			if (!getChildren().contains(focus))
				this.getChildren().add(focus);
		} else
			getChildren().remove(focus);
	}

	// 绘画高亮区域
	public void drawHighLight() {

		if (isLight) {// 被选择，获得焦点
			if (!getChildren().contains(hightLight))
				this.getChildren().add(hightLight);
		} else
			getChildren().remove(hightLight);
	}

	public boolean getSelect() {
		return isSelected;
	}

	public void setSelect(boolean select) {
		this.isSelected = select;
	}

	public void setText(char c) {
		if (c >= '1' && c <= '9')
			this.setText(c);
	}

	/**
	 * 清空选中状态
	 */
	public void clearSelected() {
		ObservableList<Node> list = sudoku.gridPane.getChildren();

		for (Node node : list) {

			if (node instanceof Cell) {
				Cell c = (Cell) node;
				c.setSelect(false);
				c.drawFocus(false);
			}
		}
	}

	public String getNumber() {
		return this.getText();
	}

	public void setHighLight(boolean isLight) {

		this.isLight = isLight;

	}

	@Override
	public ObservableList<Node> getChildren() {
		return super.getChildren();
	}

}
