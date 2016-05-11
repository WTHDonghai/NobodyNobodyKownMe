package com.donghai.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.donghai.core.DifficultyLevel;
import com.donghai.core.SudukuTool;
import com.donghai.ui.node.BoradPanel;
import com.donghai.ui.node.Cell;
import com.donghai.ui.node.SelectButton;
import com.donghai.ui.node.StatePanel;
import com.donghai.util.ConfigUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

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
	private StackPane pause;
	private Pane contentPane;
	private DifficultyLevel[] difficultys;
	private Rectangle paseBackground;
	private String time;// 做题计时
	private BorderPane mainP;// 程序的主面板
	private boolean isStart;// 开始游戏
	private StatePanel timePane;
	private long currentTime;

	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
	private StatePanel curentCount;
	private LogoMenuPane logo;

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Sudoku by Madison");

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {// 关闭按钮逻辑处理

				if (!cheackSolve)
					ConfigUtil.writeRecord(level, count, matrix);
				System.gc();
				System.exit(0);
			}
		});

		contentPane = new Pane();

		logo = new LogoMenuPane();
		logo.setFocusTraversable(true);
		contentPane.getChildren().add(logo);

		mainP = new BorderPane();
		mainP.setStyle("-fx-background:transparent;");
		mainP.setCenter(contentPane);

		HBox stateBox = new HBox();// 显示游戏状态
		stateBox.setFillHeight(true);
		stateBox.setPadding(new Insets(0, 4, 4, 0));

		// show divide
		StatePanel divide = new StatePanel("Sudoku by Madison Wu", "");
		divide.setColor(Color.BLACK);
		stateBox.getChildren().add(divide);

		Pane state = new Pane();
		state.getChildren().add(stateBox);
		mainP.setTop(state);

		Scene scene = new Scene(mainP);// 不指定宽高
		primaryStage.setScene(scene);
		// 没有窗口装饰
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		scene.setFill(null);

		// update time
		EventHandler<ActionEvent> eventHandler = e -> {

			String time = updataTime();

			if (isStart) {
				timePane.setContentText(time);
			}
		};

		Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), eventHandler));
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();

		primaryStage.show();

	}

	/**
	 * 显示游戏状态 关卡难度&时间
	 * 
	 * @throws FileNotFoundException
	 */
	private void showGameState() throws FileNotFoundException {

		HBox stateBox = new HBox();// 显示游戏状态
		stateBox.setFillHeight(true);
		// stateBox.setStyle("-fx-background:transparent;");
		stateBox.setPadding(new Insets(0, 4, 4, 0));

		curentCount = new StatePanel("Dificulty:", level.toString() + "-" + count);
		stateBox.getChildren().add(curentCount);

		// show divide
		StatePanel divide = new StatePanel("                                                       ", "");
		stateBox.getChildren().add(divide);

		timePane = new StatePanel(new Image(new FileInputStream(new File("img/time_icon.png"))), time);
		stateBox.getChildren().add(timePane);

		Pane state = new Pane();
		// top.setStyle("-fx-background:transparent;");
		state.getChildren().add(stateBox);
		mainP.setTop(state);
	}

	/**
	 * 初始化数独界面相关的组件
	 */
	private void initGameBorder() {

		setUpBorad();
		borad = new BoradPanel(matrix, grid_w, grid_h, SCREEN_W, SCREEN_H);
		// ----------------------------content pane
		contentPane.getChildren().add(borad);
		contentPane.getChildren().add(gridPane);

		// ----------------------------pause pane
		pause = new StackPane();
		paseBackground = new Rectangle(SCREEN_W, SCREEN_H);
		paseBackground.setFill(new Color(0.5, 0.5, 0.5, .7));
		pause.getChildren().add(paseBackground);
	}

	// 初始化数据
	{

		currentCol = 0;
		currentRow = 0;

		matrix = new int[9][9];
		pazzle = new int[9][9];
		gridPane = new GridPane();
		grid_h = SCREEN_H / 9;
		grid_w = SCREEN_W / 9;

		difficultys = new DifficultyLevel[] { DifficultyLevel.EASY, DifficultyLevel.MEDIUM, DifficultyLevel.DIFFICULT,
				DifficultyLevel.EVIL };

		// 假设难度为简单
		level = DifficultyLevel.EASY;
	}

	private void setUpBorad() {

		cells = new Cell[9][9];

		gridPane.setAlignment(Pos.CENTER);
		ObservableList<Node> list = gridPane.getChildren();
		gridPane.setFocusTraversable(true);
		gridPane.setOnKeyPressed(e -> {

			if (cheackSolve)
				return;

			// show pause window
			String keyName = e.getCode().getName();

			// 删除操作
			if (keyName.endsWith("Delete")) {

				for (Node node : list) {
					if (node instanceof Cell) {
						Cell c = (Cell) node;
						if (c.getSelect()) {

							if (!c.getText().equals("") && pazzle[c.getRow()][c.getCol()] != 1) {

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

							cheackSolve = SudukuTool.cheackSolve(matrix);

							if (cheackSolve) {
								isStart = false;
								contentPane.getChildren().add(new FinishedPane(this));
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

				label.setFont(new Font("Monaco", 35));
				label.setTextFill(Color.BLACK);

				if (pazzle[i][j] != 0) {
					label.setText("" + matrix[i][j]);
					label.setTextFill(Color.GRAY.darker());
				}

				gridPane.add(label, j, i);
				cells[i][j] = label;
			}
		}
	}

	// 绘画提示颜色
	private void drawTips(ObservableList<Node> list) {

		for (Node node_ : list) {
			Cell c_ = (Cell) node_;
			if (!c_.getText().equals("") && !SudukuTool.isValid(c_.getRow(), c_.getCol(), matrix)) {
				c_.setTextFill(Color.RED);
			} else {

				// 恢复颜色
				c_.setTextFill(Color.GRAY.darker());
				// 如果是由用户填上的数字，则颜色为黑色
				if (pazzle[c_.getRow()][c_.getCol()] != 1)
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

	/**
	 * 主界面
	 * 
	 * @param args
	 */
	public class LogoMenuPane extends StackPane {

		private VBox buttonBox;
		private SelectButton[] difficutlySelections;
		private int currentSelected = -1;

		public LogoMenuPane() {

			try {
				initView();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		public void initView() throws FileNotFoundException {

			String pathname = "img/menuLog.png";
			FileInputStream fis = new FileInputStream(new File(pathname));
			Image img = new Image(fis);
			ImageView background = new ImageView(img);

			Rectangle r = new Rectangle(630, 630);
			r.setFill(Color.WHITE);

			this.getChildren().add(r);
			this.getChildren().add(background);

			buttonBox = new VBox(6.5);
			String[] dificultyText = { "EASY", "NORMAL", "HARD", "EVIL" };
			difficutlySelections = new SelectButton[4];
			for (int i = 0; i < dificultyText.length; i++) {

				SelectButton selectButton = new SelectButton(dificultyText[i]);

				selectButton.setOnMouseEntered(e -> {
					difficutlyMenuClear();
					selectButton.setSelete(true);
				});

				selectButton.setOnMouseExited(e -> {
					selectButton.setSelete(false);
				});

				buttonBox.getChildren().add(selectButton);
				difficutlySelections[i] = selectButton;
			}

			buttonBox.setLayoutX(152);
			buttonBox.setLayoutY(278);

			Pane menuP = new Pane();
			menuP.getChildren().add(buttonBox);

			this.getChildren().add(menuP);
			this.setOnKeyPressed(e -> {

				selectionMenu(e.getCode().getName());

				// startGame
				if ("Enter".equals(e.getCode().getName()) && currentSelected != -1) {

					startGame();
				}
			});

			this.setOnMouseClicked(e -> {

				for (int i = 0; i < difficultys.length; i++) {

					if (difficutlySelections[i].isSeleted()) {
						currentSelected = i;
						break;
					}
				}
				startGame();
			});

		}

		private void startGame() {

			if (currentSelected != -1) {

				level = difficultys[currentSelected];
				// System.out.println(level);
				currentSelected = -1;
				SudukuWindow.this.contentPane.getChildren().remove(this);
				// --------------------------------------------------------------
				// 首先检查当前难度下有没有纪录，如果没有纪录，则重新载入纪录，否则，读取纪录
				File recordDir = new File("record/");
				File[] listFiles = recordDir.listFiles();
				File recordFile = null;
				boolean canRead = false;

				if (listFiles.length != 0) {
					// 获取当前难度纪录的关卡数count
					for (int i = 0; i < listFiles.length; i++) {

						String name = listFiles[i].getName();

						if (level.toString().equals(name.substring(0, name.indexOf("_")))) {// 获取到当前的难度纪录文件
							canRead = true;
							recordFile = listFiles[i];
							count = Integer.valueOf(name.substring(name.indexOf("_") + 1, name.indexOf("_") + 2));
							break;
						}
					}
				}
				if (canRead) {

					// 获取难度文件的路径
					StringBuilder sb = getDifficultyTxtPath();
					// 重新初始化盘面
					reInitPazzle(recordFile, sb);
					recordFile.delete();
				}

				if (!canRead) {
					loadPazzle();
					initGameBorder();
				}

				try {
					showGameState();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				isStart = true;
			}
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

	}// LogMenu

	/**
	 * 更新时间
	 */
	private String updataTime() {

		String time = "00:00:00";
		if (isStart) {
			time = sdf.format(new Date(System.currentTimeMillis() - currentTime - 8 * 60 * 60 * 1000));
		} else {
			currentTime = System.currentTimeMillis();
		}
		return time;
	}

	/**
	 * 下一题
	 */
	public void nextPazzle() {
		this.count++;
		reInitPazzle();
		curentCount.setContentText(level.toString() + "-" + count);
	}

	/**
	 * 返回主界面
	 */
	public void backHome() {
		if (!cheackSolve)
			ConfigUtil.writeRecord(level, count, matrix);

		currentCol = 0;
		currentRow = 0;
		isStart = true;
		cheackSolve = false;
		gridPane.getChildren().clear();
		contentPane.getChildren().clear();
		contentPane.getChildren().add(logo);
	}

	/**
	 * 获得难度文件路径
	 * 
	 * @return
	 */
	private StringBuilder getDifficultyTxtPath() {

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
		return sb;
	}

	/**
	 * 重新初始化盘面
	 * 
	 * @param recordFile
	 * @param sb
	 */
	private void reInitPazzle(File recordFile, StringBuilder sb) {

		pazzle = SudukuTool.scanSuduku(count, sb.toString());
		matrix = ConfigUtil.readRecord(recordFile);
		initGameBorder();

		ObservableList<Node> list = gridPane.getChildren();

		for (Node node : list) {

			if (node instanceof Cell) {

				Cell c = (Cell) node;

				if (pazzle[c.getRow()][c.getCol()] == 0 && matrix[c.getRow()][c.getCol()] != 0) {

					c.setText(matrix[c.getRow()][c.getCol()] + "");
					redrawFocus(list);
					// 显示错误的空格
					drawTips(list);
				}
			}
		}
	}

	private void reInitPazzle() {

		currentCol = 0;
		currentRow = 0;
		isStart = true;
		cheackSolve = false;
		gridPane.getChildren().clear();
		contentPane.getChildren().clear();
		loadPazzle();
		initGameBorder();
		System.gc();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
