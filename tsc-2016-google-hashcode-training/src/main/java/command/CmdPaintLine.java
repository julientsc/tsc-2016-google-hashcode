package command;

import model.Picture2D;

public class CmdPaintLine implements ICommand {

	private int row1;
	private int row2;
	private int col1;
	private int col2;
	
	public CmdPaintLine(int row1, int row2, int col1, int col2) throws Exception {
		this.row1 = row1;
		this.row2 = row2;
		this.col1 = col1;
		this.col2 = col2;
		
		if(col1 != col2 && row1 != row2)
			throw new Exception("Bad command");
	}

	@Override
	public int execute(Picture2D picture2d, StringBuilder sb) {
//		System.out.println(toString());
		
		for(int row = row1 ; row <= row2 ; row++)
			for(int col = col1 ; col <= col2 ; col++) 
				picture2d.getMatrix()[row][col] = 1;
		
		sb.append(toString() + "\n");
		return 1;
	}

	@Override
	public int getImpactedCell(Picture2D picture2d, Picture2D picture2d2) {
		int tot = 0;
		if(col1 == col2) {
			for(int row = row1 ; row <= row2 ; row++) {
				if(picture2d.getMatrix()[row][col1] != picture2d2.getMatrix()[row][col1])
					tot++;
			}
			return tot;
		}
		else {
			for(int col = col1 ; col <= col2 ; col++) {
				if(picture2d.getMatrix()[row1][col] != picture2d2.getMatrix()[row1][col])
					tot++;
			}
			return tot;
		}
	}
	
	@Override
	public String toString() {
		return "PAINT_LINE " + row1 + " " + col1 + " " + row2 + " " + col2;
	}

	@Override
	public void executeNegative(Picture2D picture2d) {
		for(int row = row1 ; row <= row2 ; row++)
			for(int col = col1 ; col <= col2 ; col++) 
				picture2d.getMatrix()[row][col] = 0;
	}

	@Override
	public int getImpactedCell2(Picture2D negativePicture) {
		int tot = 0;
		for(int row = row1 ; row <= row2 ; row++) {
			for(int col = col1 ; col <= col2 ; col++) {
				if(negativePicture.getMatrix()[row][col] == 1)
					tot++;
			}	
		}
		return tot;
	}
	
}
