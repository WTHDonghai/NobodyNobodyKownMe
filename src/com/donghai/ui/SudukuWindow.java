package com.donghai.ui;

import java.io.File;
import com.donghai.core.DifficultyLevel;
import com.donghai.core.SudukuTool;
import com.donghai.ui.node.BoradPanel;
import com.donghai.ui.node.Cell;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SudukuWindow extends Application {
	
	public static final int SCREEN_W = 630;
	public static final int SCREEN_H = 630;
	public static final String SUDOKU_FOLDER_NAME = "boards";
	private int[][] matrix;// 数独终盘矩阵
	// 小格子的宽高
	public int grid_h;
	public int grid_w;
	private BoradPanel borad;
	public GridPane gridPane;
	private int[][] pazzle;
	private int count = 1;// 关卡数
	private DifficultyLevel level;// 难度
	public int currentRow, currentCol;// 当前的行和列
	public Cell[][] cells;
	private boolean cheackSolve;
private Rectangle pause;
private BorderPane contentPane;

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Sudoku by Madison");
		// primaryStage.setResizable(false);

		setUpBorad();
		borad = new BoradPanel(matrix, grid_w, grid_h, SCREEN_W, SCREEN_H);

		//----------------------------content pane
		
		contentPane = new BorderPane();
		contentPane.setCenter(borad);
		contentPane.setCenter(gridPane);

		//----------------------------pause pane
		 pause = new Rectangle(SCREEN_W, SCREEN_H);
		 pause.setFill(new Color(0.5, 0.5, 0.5,.7));
		 
		Scene scene = new Scene(contentPane, SCREEN_W, SCREEN_H);

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {// 关闭按钮逻辑处理

			}
		});

		primaryStage.setScene(scene);
		primaryStage.show();

	}

	// 初始化数据
	{
		
//		cheackSolve = true;
		currentCol = 0;
		currentRow = 0;

		matrix = new int[9][9];
		pazzle = new int[9][9];
		gridPane = new GridPane();
		grid_h = SCREEN_H / 9;
		grid_w = SCREEN_W / 9;

		// 假设难度为简单
		// level = DifficultyLevel.EASY;
		level = DifficultyLevel.MEDIUM;

		loadPazzle();

	}

	private void setUpBorad() {

		cells = new Cell[9][9];

		gridPane.setAlignment(Pos.CENTER);
		ObservableList<Node> list = gridPane.getChildren();
		gridPane.setFocusTraversable(true);

		gridPane.setOnKeyPressed(e -> {

			if(cheackSolve)
				return;
				
			String keyName = e.getCode().getName();

			// 删除操作
			if (keyName.endsWith("Delete")) {

				for (Node node : list) {
					if (node instanceof Cell) {
						Cell c = (Cell) node;
						if (c.getSelect()) {
							if (!c.getText().equals("")&&pazzle[c.getRow()][c.getCol()]!=1) {
								
								matrix[c.getRow()][c.getCol()] = Integer.valueOf(0);

								// 清除错误提示
								drawTips(list);
								
								c.setText("");
							}
							break;
						}
					}
				}
			}

			moveFocus(list, keyName);

			for (Node node : list) {

				if (node instanceof Cell) {

					Cell c = (Cell) node;
					if (c.getSelect()) {
						KeyCode code = e.getCode();
						// 是否是数字
						if (code.isDigitKey() && !code.getName().equals("0") && pazzle[c.getRow()][c.getCol()] == 0) {
							c.setText(code.getName());
							redrawFocus(list);
							matrix[c.getRow()][c.getCol()] = Integer.valueOf(code.getName());

							// 显示错误的空格
							drawTips(list);

							// printMatrix();

							cheackSolve = SudukuTool.cheackSolve(matrix);
							
							if(cheackSolve)
							{
								//绘画pause background and win text
								contentPane.setCenter(pause);
							    Label text = new Label("Congratulation,You Finsh It\nClick to next");
							    text.setTextFill(Color.WHITE);
							    text.setFont(Font.font(STYLESHEET_MODENA, 35));
							    text.setAlignment(Pos.CENTER);
							    
							    contentPane.getChildren().add(text);
							}

							break;
						}
					}
				}
			}

		});

		for (int i = 0; i < matrix.length; i++) {

			gridPane.getColumnConstraints().add(new ColumnConstraints(grid_w));
			for (int j = 0; j < matrix.length; j++) {

				Cell label = new Cell(this, i, j);

				if (pazzle[i][j] != 0) {
					label.setText("" + matrix[i][j]);
					label.setTextFill(Color.GRAY.darker());
					label.setFont(new Font(20));

				} else 
				{
					label.setFont(new Font("Monaco", 20));
				}

				gridPane.add(label, j, i);
				cells[i][j] = label;
			}
		}
	}

	//绘画提示颜色
	private void drawTips(ObservableList<Node> list)
	{
		for (Node node_ : list) {
			Cell c_ = (Cell) node_;
			if (!c_.getText().equals("")&&!SudukuTool.isValid(c_.getRow(), c_.getCol(), matrix)) {
				c_.setTextFill(Color.RED);
			} else {
				
				//恢复颜色
				c_.setTextFill(Color.GRAY.darker());
				//如果是由用户填上的数字，则颜色为黑色
				if(pazzle[c_.getRow()][c_.getCol()]!=1)
					c_.setTextFill(Color.BLACK);
			}
		}
	}

	/**
	 * 移动焦点
	 * 
	 * @param list
	 * @param keyName
	 */
	private void moveFocus(ObservableList<Node> list, String keyName) {

		switch (keyName) {

		case "Up":

			SudukuWindow.this.currentRow -= 1;

			if (SudukuWindow.this.currentRow < 0)
				SudukuWindow.this.currentRow = 8;

			drawHighLightArea();
			redrawFocus(list);

			break;
		case "Right":

			SudukuWindow.this.currentCol += 1;

			if (SudukuWindow.this.currentCol > 8)
				SudukuWindow.this.currentCol = 0;

			drawHighLightArea();
			redrawFocus(list);

			break;
		case "Left":

			SudukuWindow.this.currentCol -= 1;

			if (SudukuWindow.this.currentCol < 0)
				SudukuWindow.this.currentCol = 8;

			drawHighLightArea();
			redrawFocus(list);

			break;
		case "Down":

			SudukuWindow.this.currentRow += 1;

			if (SudukuWindow.this.currentRow > 8)
				SudukuWindow.this.currentRow = 0;

			drawHighLightArea();
			redrawFocus(list);

			break;

		}
	}

	/**
	 * 重绘焦点
	 * 
	 * @param list
	 */
	private void redrawFocus(ObservableList<Node> list) {

		for (Node node : list) {

			if (node instanceof Cell) {
				Cell c = (Cell) node;

				if (c.getRow() == currentRow && c.getCol() == currentCol) {
					currentRow = c.getRow();
					currentCol = c.getCol();
					c.clearSelected();
					c.setSelect(true);
					c.drawFocus(true);
					break;
				}
			}
		}
	}

	// 载入数独谜题
	private void loadPazzle() {
		File file = new File(SUDOKU_FOLDER_NAME);

		if (!file.exists()) {// 生成题目
			file.mkdirs();
			SudukuTool.loadSuduPazzle();
		}

		initPazzle();
	}

	/**
	 * 初始化谜题
	 */
	private void initPazzle() {
		String fileName = "boards/" + count + ".su";
		// 扫描谜题
		matrix = SudukuTool.scanSuduku(count, fileName);

		StringBuilder sb = new StringBuilder();
		sb.append(SUDOKU_FOLDER_NAME);
		sb.append("/");
		sb.append(count);

		switch (level) {
		case EASY:

			sb.append("_easy.txt");

			break;
		case MEDIUM:

			sb.append("_medium.txt");
			break;
		case DIFFICULT:

			sb.append("_difficult.txt");
			break;
		case EVIL:

			sb.append("_evil.txt");
			break;
		}

		pazzle = SudukuTool.scanSuduku(count, sb.toString());

		for (int i = 0; i < pazzle.length; i++) {

			for (int j = 0; j < pazzle[0].length; j++) {

				if (pazzle[i][j] == 0)
					matrix[i][j] = 0;
			}
		}
	}

	/**
	 * 绘画高亮区域
	 * 
	 * @param args
	 */
	public void drawHighLightArea() {
		// 通过当前的行和列确定是第几个小共格，第几行，第几列
		int N_NGridCol = currentCol / 3 * 3;
		int N_NGridRow = currentRow / 3 * 3;

		if (pazzle[currentRow][currentCol] == 1) {
			clearHighLight();
			return;
		}

		clearHighLight();

		// 当前的行高亮
		for (int col = 0; col < 9; col++) {

			cells[currentRow][col].isLight = true;
			cells[currentRow][col].drawHighLight();
		}

		// 当前的行高亮
		for (int row = 0; row < 9; row++) {

			cells[row][currentCol].isLight = true;
			cells[row][currentCol].drawHighLight();
		}

		for (int i = 0; i < 3; i++) {

			for (int j = 0; j < 3; j++) {

				cells[N_NGridRow + i][N_NGridCol + j].isLight = true;
				cells[N_NGridRow + i][N_NGridCol + j].drawHighLight();
			}
		}
	}

	/**
	 * 清空高亮状态
	 */
	public void clearHighLight() {

		ObservableList<Node> list = gridPane.getChildren();
		for (Node node : list) {
			if (node instanceof Cell) {
				Cell c = (Cell) node;
				c.setHighLight(false);
				c.drawHighLight();
			}
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
