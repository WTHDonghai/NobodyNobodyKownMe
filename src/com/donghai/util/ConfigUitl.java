package com.donghai.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import com.donghai.core.DifficultyLevel;

/**
 * 程序配置信息相关
 * 
 * @author donghai
 *
 */
public class ConfigUitl {
	private static final String CONFIG_PATH = "config/config.ini";
	private static final Object leve = null;
	private static Properties pro;

	static {
		pro = new Properties();
	}

	/**
	 * 写入当前难度的关卡数
	 */
	public static void writerTheLeve(DifficultyLevel leve, int count) {
		File configFile = new File(CONFIG_PATH);

		pro.setProperty(leve.toString(), count + "");
		try {
			pro.store(new FileOutputStream(configFile), "Sudoku");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读入当前难度的关卡数
	 */
	public static int readTheLeve(DifficultyLevel level) {
		String value = pro.getProperty(leve.toString());
		return Integer.valueOf(value);
	}

	/**
	 * 保存进度
	 * 
	 */
	public static void writeRecord(DifficultyLevel level, int count,int[][] matrix) {
		
		BufferedWriter bfw = null;
		
		
		try {
			bfw = new BufferedWriter(new FileWriter(new File("record/" + level + "_" + count + ".txt")));

			for (int i = 0; i < matrix.length; i++) {
				
				for (int j = 0; j < matrix.length; j++) {
					
					bfw.write(matrix[i][j]+"");
				}
				bfw.newLine();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		} finally {

			if (bfw != null) {
				try {
					bfw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 读取纪录数据
	 */
	public static int[][] readRecord(File file) {
		
		int[][] recordPazzle = new int[9][9];

		BufferedReader bfr = null;
		String ary = null;
		int row = 0;
		try {
			
			bfr = new BufferedReader(new FileReader(file));

			while ((ary = bfr.readLine()) != null) {

				for (int i = 0; i < ary.length(); i++) {
					
					int n = Integer.valueOf(ary.substring(i, i+1));
					recordPazzle[row][i] = n; 
				}
				
				row++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		finally {

			if (bfr != null) {
				try {
					bfr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return recordPazzle;
	}

}
