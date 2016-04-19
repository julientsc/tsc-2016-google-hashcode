package command;

import model.Picture2D;

public interface ICommand {

	public int execute(Picture2D picture2d, StringBuilder sb);
	public void executeNegative(Picture2D picture2d);
	
	public int getImpactedCell(Picture2D picture2d, Picture2D picture2d2);
	public int getImpactedCell2(Picture2D negativePicture);
	
}
