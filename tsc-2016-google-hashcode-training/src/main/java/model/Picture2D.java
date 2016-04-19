package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Picture2D {
	
	private char FULL = '#';
	
	private int nbRows;
	private int nbCols;

	public int getNbRows() {
		return nbRows;
	}

	public int getNbCols() {
		return nbCols;
	}

	private int[][] matrix = null; // rows, cols

	public Picture2D(int nbRows, int nbCols) {
		matrix = new int[nbRows][nbCols];
		this.nbRows = nbRows;
		this.nbCols = nbCols;
	}
	private String filePath;
		
	public Picture2D(String filePath) throws IOException {
		this.filePath = filePath;
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line;
		int row = 0;
		boolean isFirst = true;
		while ((line = br.readLine()) != null) {
			if (isFirst) {
				String[] values = line.split(" ");
				nbRows = Integer.parseInt(values[0]);
				nbCols = Integer.parseInt(values[1]);
				matrix = new int[nbRows][nbCols];
				isFirst = false;
			} else {
				for (int i = 0; i < line.length(); i++) {
					char c = line.charAt(i);
					if(c == FULL) {
						matrix[row][i] = 1;
					} else {
						matrix[row][i] = 0;
					}
				}
				row++;
			}

		}
		br.close();
	}

	public Picture2D diff(Picture2D a, Picture2D b) {
		Picture2D picture2d = new Picture2D(b.nbRows, a.nbCols);
		for(int row = 0 ; row < b.nbRows ; row++)
			for(int col = 0 ; col < b.nbCols ; col++) {
				if(a.getMatrix()[row][col] != b.getMatrix()[row][col] )
					picture2d.getMatrix()[row][col] = 1;
				else
					picture2d.getMatrix()[row][col] = 0;
			}
		
		return picture2d;
	}
	public String toString() {
		String s = "";
		for(int row = 0 ; row < nbRows ; row++) {
			for(int col = 0 ; col < nbCols ; col++) {
				s += matrix[row][col];
				
			}
			s += "\n";
		}
		
		return s;
		
	}
	
	public static boolean isSame(Picture2D a, Picture2D b) {
		for(int row = 0 ; row < b.nbRows ; row++) {
			for(int col = 0 ; col < b.nbCols ; col++) {
				if(a.getMatrix()[row][col] != b.getMatrix()[row][col])
					return false;
				
			}
		}
		
		return true;
	}
	
	public int[][] getMatrix() {
		return matrix;
	}

	public Picture2D copy() {
		Picture2D picture2d = new Picture2D(nbRows, nbCols);
		for (int row = 0 ; row < matrix.length ; row++) {
			for (int col = 0 ; col < matrix[row].length ; col++) {
				picture2d.getMatrix()[row][col] = matrix[row][col];
			}
		}
		return picture2d;
	}

	public String getName() {
		return filePath;
	}


}
