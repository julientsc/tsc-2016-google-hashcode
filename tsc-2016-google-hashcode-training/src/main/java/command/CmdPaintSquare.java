package command;

import java.util.ArrayList;

import model.Picture2D;

public class CmdPaintSquare implements ICommand {

	private int row;
	private int col;
	private int space;
	private ArrayList<CmdEraseCell> previousNone;
	
	public CmdPaintSquare( int row, int col, int space, ArrayList<CmdEraseCell> previousNone) {
		super();
		this.row = row;
		this.col = col;
		this.space = space;
		this.previousNone = previousNone;
	}

	@Override
	public int execute(Picture2D picture2d, StringBuilder sb) {
//		System.out.println(toString());
		for(int row = this.row-space ; row <= this.row + space ; row++) 
			for(int col = this.col-space ; col <= this.col + space ; col++) {
				boolean mustDraw = true;
				for(CmdEraseCell cell : previousNone) {
					if(cell.getRow() == row && cell.getCol() == col) {
						mustDraw = false;
						break;
					}
				}
				if(mustDraw) {
					picture2d.getMatrix()[row][col] = 1;
				}
			}
		int tot = 1;
		sb.append(toString() + "\n");
		for (CmdEraseCell cmdEraseCell : previousNone) {
			tot += cmdEraseCell.execute(picture2d, sb);
		}
		return tot;
	}

	@Override
	public int getImpactedCell(Picture2D picture2d, Picture2D picture2d2) {
		int tot = 0;
		for(int row = this.row-space ; row <= this.row + space ; row++) 
			for(int col = this.col-space ; col <= this.col + space ; col++) { 
				if(picture2d.getMatrix()[row][col] != picture2d2.getMatrix()[row][col])
					tot++;
			}
		
		return tot - previousNone.size();
	}
	
	@Override
	public String toString() {
		return "PAINT_SQUARE " + row + " " + col + " " + space;
	}

	@Override
	public void executeNegative(Picture2D picture2d) {
		for(int row = this.row-space ; row <= this.row + space ; row++) 
			for(int col = this.col-space ; col <= this.col + space ; col++) {
				boolean mustDraw = true;
				for(CmdEraseCell cell : previousNone) {
					if(cell.getRow() == row && cell.getCol() == col) {
						mustDraw = false;
						break;
					}
				}
				if(mustDraw) {
					picture2d.getMatrix()[row][col] = 0;
				}
				
			}
	}

	@Override
	public int getImpactedCell2(Picture2D negativePicture) {
		int tot = 0;
		for(int row = this.row-space ; row <= this.row + space ; row++) 
			for(int col = this.col-space ; col <= this.col + space ; col++) { 
				if(negativePicture.getMatrix()[row][col] == 1)
					tot++;
			}
		
		return tot - previousNone.size();
	}

	public int getSpace() {
		return this.space;
	}
	
}
