package com.donghai.core;

import java.util.Random;

/**
 * 数独谜题制造工厂
 * 
 * @author donghai
 *
 */
public class SudukuPazzleFactory {

	public static final int MAX_RUN_TIMES = 220;// 程序执行的阀值
	private int currentTimes;
	private Random random = new Random();

	public int[][] makePazzle() {
		// 随机的数独数组
		int[][] randomMatrix = new int[9][9];

		for (int row = 0; row < 9; row++) {

			if (row == 0) {
				randomMatrix[0] = buildRandomArray();
				currentTimes = 0;
			} else {

				int[] tempAry = buildRandomArray();

				// 开始检查该行是否有效
				for (int col = 0; col < 9; col++) {

					if (currentTimes < MAX_RUN_TIMES) {
						// 当前的行无效
						if (!isValid(randomMatrix, tempAry, row, col)) {
							resetCurrentRowToZero(randomMatrix, row);
							col = 8;
							row -= 1;
							tempAry = buildRandomArray();
						}
					} else {// 始终没有解，一切重新开始

						row = -1;
						col = 8;
						resetValue(randomMatrix);
						currentTimes = 0;
					}
				}
			}

		}

		return randomMatrix;
	}

	private void resetValue(int[][] randomAry) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				randomAry[i][j] = 0;
			}
		}

	}

	private void resetCurrentRowToZero(int[][] randomMatrix, int row) {

		for (int i = 0; i < 9; i++) {
			randomMatrix[row][i] = 0;
		}
	}

	private boolean isValid(int[][] randomMatrix, int[] tempAry, int row, int col) {
		for (int i = 0; i < tempAry.length; i++) {

			randomMatrix[row][col] = tempAry[i];
			if (isValid(randomMatrix, row, col))
				return true;
		}

		return false;
	}

	/**
	 * 检查数组的有效性
	 * 
	 * @param randomMatrix
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean isValid(int[][] randomMatrix, int row, int col) {
		return isColValid(randomMatrix, row, col) && isRowValid(randomMatrix, row, col)
				&& isBlockValid(randomMatrix, row, col);
	}

	/**
	 * 检查9宫格的有效性
	 * 
	 * @param randomMatrix
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean isBlockValid(int[][] randomMatrix, int row, int col) {

		int baseRow = row / 3 * 3;
		int baseCol = col / 3 * 3;

		for (int rowNum = 0; rowNum < 8; rowNum++) {
			if (randomMatrix[baseRow + rowNum / 3][baseCol + rowNum % 3] == 0)
				continue;

			for (int colNum = rowNum + 1; colNum < 9; colNum++) {
				if (randomMatrix[baseRow + rowNum / 3][baseCol
						+ rowNum % 3] == randomMatrix[baseRow + colNum / 3][baseCol + colNum % 3])
					return false;
			}
		}

		return true;
	}

	/**
	 * 检查行的有效性
	 * 
	 * @param randomMatrix
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean isRowValid(int[][] randomMatrix, int row, int col) {

		int currentValue = randomMatrix[row][col];

		for (int i = 0; i < col; i++) {
			if (currentValue == randomMatrix[row][i])
				return false;
		}

		return true;
	}

	/**
	 * 检查列的合法性
	 * 
	 * @param randomMatrix
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean isColValid(int[][] randomMatrix, int row, int col) {

		int currentValue = randomMatrix[row][col];
		for (int i = 0; i < row; i++) {

			if (currentValue == randomMatrix[i][col])
				return false;
		}

		return true;
	}

	/**
	 * 返回一个有1到9九个数随机排列的一维数组,
	 */
	private int[] buildRandomArray() {
		currentTimes++;
		int[] array = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int randomInt = 0;
		/*
		 * 随机产生一个1到8的随机数，使得该下标的数值与下标为0的数值交换，
		 * 
		 * 处理20次，能够获取一个有1到9九个数随机排列的一维数组,
		 */
		for (int i = 0; i < 20; i++) {
			randomInt = random.nextInt(8) + 1;
			int temp = array[0];
			array[0] = array[randomInt];
			array[randomInt] = temp;
		}

		return array;
	}

	public static void main(String[] args) {

		SudukuPazzleFactory sf = new SudukuPazzleFactory();
		int[][] a = sf.makePazzle();

		for (int[] is : a) {
			for (int i : is) {
				System.out.print(i + " ");
			}
			System.out.println();
		}

	}

}
