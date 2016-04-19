package command;

import model.Picture2D;

public class CmdEraseCell implements ICommand {

	private int row;
	private int col;
	
	
	
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

	public CmdEraseCell( int row, int col) {
		this.row = row;
		this.col = col;
	}

	@Override
	public int execute(Picture2D picture2d, StringBuilder sb) {
//		System.out.println(toString());
		picture2d.getMatrix()[row][col] = 0;
		sb.append(toString() + "\n");
		return 1;
	}

	@Override
	public int getImpactedCell(Picture2D picture2d, Picture2D picture2d2) {
		return 1;
	}
	
	@Override
	public String toString() {
		return "ERASE_CELL " + row + " " + col;
	}

	@Override
	public void executeNegative(Picture2D picture2d) {
		// do nothing
	}

	@Override
	public int getImpactedCell2(Picture2D negativePicture) {
		return 0;
	}
	
}
