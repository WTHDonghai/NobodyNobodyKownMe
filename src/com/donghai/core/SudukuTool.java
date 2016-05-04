package com.donghai.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 检查数独
 * 
 * @author donghai
 */
public class SudukuTool {
	private static SudukuPazzleFactory factory;

	private SudukuTool() {
	}

	/**
	 * 生成终盘
	 */
	public static void loadSuduPazzle() {
		SudukuPazzleFactory pazzleFactory = new SudukuPazzleFactory();

		for (int i = 0; i < 5; i++) {

			int[][] pazzle = pazzleFactory.makePazzle();

			writePazzle(i, pazzle);
		}

		/**
		 * 采用"挖洞"法，产生不同难度的数独难题文件。 文件中 0 表示 有数据且不可编辑，1 表示不显示数据，且可编辑
		 */
		HoleDigUtils dogger = new HoleDigUtils();
		for (DifficultyLevel level : DifficultyLevel.values()) {
			dogger.digHolesByGameDifficulty(100, level);
		}
	}

	/**
	 * 写入文件
	 * 
	 * @param num
	 * @param pazzle
	 */
	public static void writePazzle(int num, int[][] pazzle) {
		BufferedWriter bfw = null;
		try {
			bfw = new BufferedWriter(new FileWriter(new File("boards/" + num + ".su")));

			for (int i = 0; i < pazzle.length; i++) {

				for (int j = 0; j < pazzle.length; j++) {

					bfw.write(pazzle[i][j] + "");
				}
				bfw.newLine();
			}

			bfw.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (bfw != null)
				try {
					bfw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}

	/**
	 * 扫描终盘/或谜题
	 * 
	 * @param count
	 * @return
	 */
	public static int[][] scanSuduku(int count, String filePath) {
		int[][] suduAry = new int[9][9];
		// String filePath = "boards/"+count+".su";

		File file = null;
		BufferedReader bfr = null;
		String s = null;
		try {

			file = new File(filePath);
			bfr = new BufferedReader(new FileReader(file));

			int row = 0;
			while ((s = bfr.readLine()) != null) {
				int col = 0;
				for (char c : s.toCharArray()) {
					if (c >= '0' && c <= '9') {
						suduAry[row][col] = Integer.parseInt(c + "");
						col++;
					}
				}
				row++;
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();

		} finally {
			if (bfr != null)
				try {
					bfr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		return suduAry;
	}

	/** Check whether grid[i][j] is valid in the grid */
	public static boolean isValid(int i, int j, int[][] grid) {
		
		// j != column i != row 意思是除了自己以外
		// Check whether grid[i][j] is valid at the i's row
		for (int column = 0; column < 9; column++)
			if (column != j && grid[i][column] == grid[i][j])
				return false;

		// Check whether grid[i][j] is valid at the j's column
		for (int row = 0; row < 9; row++)
			if (row != i && grid[row][j] == grid[i][j])
				return false;

		// Check whether grid[i][j] is valid in the 3 by 3 box
		for (int row = (i / 3) * 3; row < (i / 3) * 3 + 3; row++)
			for (int col = (j / 3) * 3; col < (j / 3) * 3 + 3; col++)
				if (row != i && col != j && grid[row][col] == grid[i][j])
					return false;

		return true; // The current value at grid[i][j] is valid
	}

	/** Obtain a list of free cells from the puzzle */
	public static int[][] getFreeCellList(int[][] grid) {
		// Determine the number of free cells
		int numberOfFreeCells = 0;
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (grid[i][j] == 0)
					numberOfFreeCells++;

		// 每一行中的那一列是空的
		// Store free cell positions into freeCellList
		int[][] freeCellList = new int[numberOfFreeCells][2];
		int count = 0;
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++)
				if (grid[i][j] == 0) {
					freeCellList[count][0] = i;
					freeCellList[count++][1] = j;
				}

		// printGrid(freeCellList);
		return freeCellList;
	}

	/** Search for a solution */
	public static boolean search(int[][] grid) {

		int[][] freeCellList = getFreeCellList(grid); // Free cells
		int k = 0; // Start from the first free cell
		boolean found = false; // Solution found?

		while (!found) {
			int i = freeCellList[k][0];
			int j = freeCellList[k][1];
			if (grid[i][j] == 0)
				grid[i][j] = 1; // Start with 1

			if (isValid(i, j, grid)) {

				if (k + 1 == freeCellList.length) { // No more free cells
					found = true; // A solution is found
				} else { // Move to the next free cell
					k++;
				}
			} else if (grid[i][j] < 9) {
				grid[i][j] = grid[i][j] + 1; // Check the next possible value
			} else { // grid[i][j] is 9, backtrack
				while (grid[i][j] == 9) {
					grid[i][j] = 0; // Reset to free cell
					if (k == 0) {
						return false; // No possible value
					}
					k--; // Backtrack
					i = freeCellList[k][0];
					j = freeCellList[k][1];
				}
				grid[i][j] = grid[i][j] + 1; // Check the next possible value
			}
		}

		return true; // A solution is found
	}

	/**
	 * 检查解
	 * 
	 * @param freeCell
	 * @param pazzle
	 * @return
	 */
	public static boolean cheackSolve(int[][] pazzle) {

	    for (int j2 = 0; j2 < pazzle.length; j2++) {

			for (int k = 0; k < pazzle.length; k++) {

				if (pazzle[j2][k]==0||!isValid(j2, k, pazzle)) 
					return false;
			}
		}
		return true;
	}// checkWin
	
	public static void printMatrix(int[][] matrix) {
		
		for(int[] m:matrix)
		{
			for(int n:m)
			{
				System.out.print(n+" ");
			}
			System.out.println();
		}
	}

}
