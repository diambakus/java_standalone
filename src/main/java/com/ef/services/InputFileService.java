package com.ef.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

abstract class InputFileService {

	protected File inputFile = null;
	protected BufferedReader bufferedReader = null;
	
	public BufferedReader getBufferReader(String fileName) {
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			bufferedReader = new BufferedReader(new InputStreamReader(fstream));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bufferedReader;
	}
	
	public void closeBufferReader() {
		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
