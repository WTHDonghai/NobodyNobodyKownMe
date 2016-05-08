package com.donghai.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;
import com.donghai.ui.SudukuWindow;


public class HoleDigUtils {

	private static final Logger logger = Logger
			.getLogger("my.sudoku.utils.HoleDigUtils");

	private Random random = new Random();

	public void digHolesByGameDifficulty(int numOfFiles, DifficultyLevel level) {
		
		
		for (int num = 1; num <= numOfFiles; num++) {
			int[][] array = new int[9][9];
			
			for (int i = 0; i < array.length; i++) {
				for (int j = 0; j < array.length; j++) {
					
					array[i][j] = 1;
				}
			}
			int randomInt = 0;
			for (int i = 0; i < 9; i++) {

				randomInt = getRandomNumberByLevel(level);

				int[] randomPositions = populateRandomArray(randomInt);

				for (int j = 0; j < randomPositions.length; j++) {
					int col = (i % 3) * 3 + (randomPositions[j] - 1) % 3;
					int row = (i / 3) * 3 + ((randomPositions[j] - 1) / 3);

					array[row][col] = 0;
				}
				/**
				 * 将array写入文件
				 */
				BufferedWriter bw = null;
				try {
					bw = new BufferedWriter(new FileWriter(new File(
							SudukuWindow.SUDOKU_FOLDER_NAME, buildFileName(
									level, num))));
					
				} catch (IOException e) {
					
					logger.severe(e.getMessage());
				}
				StringBuilder sb = new StringBuilder();
				for (int k = 0; k < 9; k++) {
					sb.setLength(0);
					for (int j = 0; j < 9; j++) {
						sb.append(array[k][j]);
						sb.append(",");
					}
					try {
						bw.write(sb.substring(0, sb.length() - 1).toString());
						bw.newLine();
					} catch (IOException e) {
						logger.severe(e.getMessage());
					}

				}
				if (bw != null) {
					try {
						bw.close();
					} catch (IOException e) {
						logger.severe(e.getMessage());
					} finally {
						bw = null;
					}
				}

			}
		}
	}

	private String buildFileName(DifficultyLevel level, int fileNumberl) {
		StringBuilder sb = new StringBuilder();

		sb.append(fileNumberl);
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
		default:
			break;

		}

		return sb.toString();
	}

	/**
	 * 根据不同的游戏难度，获取随机数
	 */
	public int getRandomNumberByLevel(DifficultyLevel level) {
		int randomValue = 5;

		switch (level) {
		case EASY:
			/**
			 * 产生随机数[4,5]
			 */
			randomValue = random.nextInt(2) + 4;
			break;
		case MEDIUM:
			/**
			 * 产生随机数[5,7]
			 */
			randomValue = random.nextInt(3) + 5;
			break;
		case DIFFICULT:
			/**
			 * 产生随机数[5,8]
			 */
			randomValue = random.nextInt(4) + 5;
			break;
		case EVIL:
			/**
			 * 产生随机数[6,9]
			 */
			randomValue = random.nextInt(4) + 6;
			break;
		default:
			break;

		}
		return randomValue;
	}

	private int[] populateRandomArray(int numOfRandoms) {
		
		int[] array = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int randomInt = 0;
		for (int i = 0; i < 20; i++) {
			randomInt = random.nextInt(8) + 1;
			int temp = array[0];
			array[0] = array[randomInt];
			array[randomInt] = temp;
		}

		int[] result = new int[numOfRandoms];

		System.arraycopy(array, 0, result, 0, numOfRandoms);

		return result;
	}
	
	public static void main(String[] args) throws IOException {
		HoleDigUtils digger = new HoleDigUtils();
		/**
		 * 采用"挖洞"法，产生不同难度的数独难题文件。
		 *  文件中 0 表示 有数据且不可编辑，1 表示不显示数据，且可编辑
		 */
		for (DifficultyLevel level : DifficultyLevel.values()) {
			digger.digHolesByGameDifficulty(
					100, level);
		}
	}

}