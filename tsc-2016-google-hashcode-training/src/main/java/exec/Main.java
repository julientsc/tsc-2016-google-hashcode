package exec;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import command.CmdEraseCell;
import command.CmdPaintLine;
import command.CmdPaintSquare;
import command.ICommand;
import model.Picture2D;

public class Main {
	static Random r = new Random();

	public static ArrayList<CmdPaintLine> CreateRowCommands(Picture2D picture2d) throws Exception {
		ArrayList<CmdPaintLine> cmdPaintRows = new ArrayList<>();
		for (int row = 0; row < picture2d.getNbRows(); row++) {
			int start = 0;
			boolean started = false;
			for (int col = 0; col < picture2d.getNbCols(); col++) {
				if (picture2d.getMatrix()[row][col] == 1 && !started) {
					start = col;
					started = true;
				} else if (picture2d.getMatrix()[row][col] == 0 && started) {
					cmdPaintRows.add(new CmdPaintLine(row, row, start, col - 1));
					started = false;
				}
			}
			if (started) {
				cmdPaintRows.add(new CmdPaintLine(row, row, start, picture2d.getNbCols() - 1));
			}
		}
		return cmdPaintRows;
	}

	public static ArrayList<CmdPaintLine> CreateColCommands(Picture2D picture2d) throws Exception {
		ArrayList<CmdPaintLine> cmdPaintCols = new ArrayList<>();
		for (int col = 0; col < picture2d.getNbCols(); col++) {
			int start = 0;
			boolean started = false;
			for (int row = 0; row < picture2d.getNbRows(); row++) {
				if (picture2d.getMatrix()[row][col] == 1 && !started) {
					start = row;
					started = true;
				} else if (picture2d.getMatrix()[row][col] == 0 && started) {
					cmdPaintCols.add(new CmdPaintLine(start, row - 1, col, col));
					started = false;
				}
			}
			if (started) {
				cmdPaintCols.add(new CmdPaintLine(start, picture2d.getNbRows() - 1, col, col));
			}
		}
		return cmdPaintCols;
	}

	public static ArrayList<CmdPaintSquare> CreateSquareCommands(Picture2D picture2d) throws Exception {
		ArrayList<CmdPaintSquare> cmdPaintSquares = new ArrayList<>();
		for (int col = 0; col < picture2d.getNbCols(); col++) {
			for (int row = 0; row < picture2d.getNbRows(); row++) {
				CmdPaintSquare cmdPaintSquare = searchOptimizedSquare(picture2d, col, row);
				if (cmdPaintSquare != null) {
					cmdPaintSquares.add(cmdPaintSquare);
				}
			}
		}
		return cmdPaintSquares;
	}

	private static CmdPaintSquare searchOptimizedSquare(Picture2D picture2d, int col, int row) {

		int space = 1;
		int previousSet = 0;
		ArrayList<CmdEraseCell> previousNone = null;
		do {
			

			if((col + space < picture2d.getNbCols() && col-space >= 0) && (row + space < picture2d.getNbRows() && row-space >= 0)) {
				ArrayList<CmdEraseCell> none = getEraseCellCommands(picture2d, col, row, space);
				int set = (2 * space + 1) * (2 * space + 1) - none.size();
				if (none.size()*space*1.5 > space) {
					if (previousSet > 0) {
						return new CmdPaintSquare(row, col, space - 1, previousNone);
					}
					return null;
				}

				previousNone = none;
				previousSet = set;
			}else {
				if (previousSet > 0) {
					return new CmdPaintSquare(row, col, space - 1, previousNone);
				}
				return null;
			}

			space++;

		} while (true);

	}
	
//	public static ArrayList<ICommand> detectSpecialCrossPattern(Picture2D picture2d) throws Exception {
//		ArrayList<ICommand> iCommands = new ArrayList<>();
//		for(int row = 1 ; row < picture2d.getNbRows() - 2 ; row++)
//			for(int col = 1 ; col < picture2d.getNbCols() - 2 ; col++)
//				if(
//						picture2d.getMatrix()[row][col] == 0 && 
//						picture2d.getMatrix()[row+1][col] == 1 && 
//						picture2d.getMatrix()[row][col+1] == 1 && 
//						picture2d.getMatrix()[row-1][col] == 1 && 
//						picture2d.getMatrix()[row][col-1] == 1 ) {
//					
//				
//					int startX = 0, stopX = 0, startY = 0, stopY = 0;
//					for(int i = row + 1 ; i < picture2d.getNbRows() ; i++) {
//						if(picture2d.getMatrix()[i][col] == 0) {
//							stopY = i;
//							break;
//						}
//					}
//					for(int i = row - 1 ; i >= 0 ; i--) {
//						if(picture2d.getMatrix()[i][col] == 0) {
//							startY = i;
//							break;
//						}
//					}
//					for(int i = col + 1 ; i < picture2d.getNbCols() ; i++) {
//						if(picture2d.getMatrix()[row][i] == 0) {
//							stopX = i;
//							break;
//						}
//					}
//					for(int i = col - 1 ; i >= 0 ; i--) {
//						if(picture2d.getMatrix()[row][i] == 0) {
//							startX = i;
//							break;
//						}
//					}
//
//					CmdPaintLine cmdRow = new CmdPaintLine(row, row, startX, stopX);
//					iCommands.add(cmdRow);
//					CmdPaintLine cmdCol = new CmdPaintLine(startY, stopY, col, col);
//					iCommands.add(cmdCol);
//					CmdEraseCell cmdEraseCell = new CmdEraseCell(row, col);
//					iCommands.add(cmdEraseCell);
//					
//				}
//		return iCommands;
//	}

	private static ArrayList<CmdEraseCell> getEraseCellCommands(Picture2D picture2d, int col, int row, int space) {
		ArrayList<CmdEraseCell> cmdEraseCells = new ArrayList<>();

		for (int r = row - space; r <= row + space; r++)
			for (int c = col - space; c <= col + space; c++)
				if (picture2d.getMatrix()[r][c] == 0)
					cmdEraseCells.add(new CmdEraseCell(r, c));
		return cmdEraseCells;
	}
	
	public static void splitPicture(Picture2D picture2d) {
		ArrayList<Integer> emptyRows = searchEmptyRows(picture2d);
		ArrayList<Integer> emptyCols = searchEmptyCols(picture2d);

		System.out.println(emptyRows.size());
		System.out.println(emptyCols.size());
	}
	
	private static ArrayList<Integer> searchEmptyCols(Picture2D picture2d) {
		ArrayList<Integer> cols = new ArrayList<>();
		for(int col = 0 ; col < picture2d.getNbCols() ; col++) {
			boolean mustAdd = true;
			for(int row = 0 ; row < picture2d.getNbRows() ; row++) {
				if(picture2d.getMatrix()[row][col] == 1) {
					mustAdd = false;
					break;
				}
			}
			if(mustAdd)
				cols.add(col);
		}
		return cols;
	}

	private static ArrayList<Integer> searchEmptyRows(Picture2D picture2d) {
		ArrayList<Integer> rows = new ArrayList<>();
		for(int row = 0 ; row < picture2d.getNbRows() ; row++) {
			boolean mustAdd = true;
			for(int col = 0 ; col < picture2d.getNbCols() ; col++) {
				if(picture2d.getMatrix()[row][col] == 1) {
					mustAdd = false;
					break;
				}
			}
			if(mustAdd)
				rows.add(row);
		}
		return rows;
	}

	public static int exec(Picture2D inputPicture, String outputFile) throws Exception {
		
		StringBuilder sb = new StringBuilder();
		int commandCount = 0;
		
		// load from file
		Picture2D originalePicture = inputPicture.copy();
		Picture2D negativePicture = inputPicture.copy();
		
		// create an empty image
		Picture2D drawingPicture = new Picture2D(negativePicture.getNbRows(), negativePicture.getNbCols());
		
//		for (ICommand cmd : detectSpecialCrossPattern(inputPicture)) {
//			commandCount += cmd.execute(drawingPicture, sb);
//			cmd.executeNegative(negativePicture);
//		}

		// search global commands
		ArrayList<ICommand> originalCommands = new ArrayList<>();
		originalCommands.addAll(CreateRowCommands(originalePicture));
		originalCommands.addAll(CreateColCommands(originalePicture));
		originalCommands.addAll(CreateSquareCommands(originalePicture));

		
		try {
			
			do {

				ArrayList<ICommand> avaiableCommands = new ArrayList<>();
				avaiableCommands.addAll(originalCommands);

//				// create command from the negative picture
				avaiableCommands.addAll(CreateRowCommands(negativePicture));
				avaiableCommands.addAll(CreateColCommands(negativePicture));
				avaiableCommands.addAll(CreateSquareCommands(negativePicture));

				// desc sort of glbCommand
				Collections.sort(avaiableCommands, new Comparator<ICommand>() {
					@Override
					public int compare(ICommand o1, ICommand o2) {
//						return o2.getImpactedCell(drawingPicture, originalePicture) - o1.getImpactedCell(drawingPicture, originalePicture);
						try {
						return o2.getImpactedCell2(negativePicture) - o1.getImpactedCell2(negativePicture);
						} catch(Exception e) {

							return o2.getImpactedCell2(negativePicture) - o1.getImpactedCell2(negativePicture);
						}
					}
				});

				int impact = avaiableCommands.get(0).getImpactedCell2(negativePicture);
				if (impact == 0)
					break;
				
				int i = 0, currentImpact;
				do {
					currentImpact = avaiableCommands.get(i++).getImpactedCell2(negativePicture);
				} while (i < avaiableCommands.size() && impact == currentImpact);
				i--;
				
				ICommand bestCommand = avaiableCommands.get(0);
//				ICommand bestCommand = avaiableCommands.get(0);
				
				
				commandCount += bestCommand.execute(drawingPicture, sb);
				bestCommand.executeNegative(negativePicture);

				if(commandCount % 100 == 0)
					System.out.println(commandCount);

			} while (true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(sb.length() >0 ) {
		PrintWriter pw = new PrintWriter(new FileWriter(outputFile + "_" + commandCount + ".out"));
			System.out.println(inputPicture.getName() + " (" + commandCount + ") -> " + outputFile);
			pw.write(commandCount + "\n");
			pw.write(sb.toString().substring(0, sb.length() - 1));
			pw.close();
		} else {
			System.err.println("Nothing to write");
		}
		return commandCount;
	}

	public static void main(String[] args) throws Exception {
//		splitPicture(new Picture2D("logo.in"));
		exec(new Picture2D("logo.in"), "learn_and_teach");
//		int min1 = 1000;
//		int min2 = 1000;
//		for(int i = 0 ; i < 10000 ; i++) {
//			int r1 = exec("right_angle.in", "right_angle");
//			int r2 = exec(new Picture2D("logo.in"), "logo");
//			if(r1 < min1)
//				min1 = r1;
////			if(r2 < min2)
////				min2 = r2;
//			System.out.println(min1 + " / " + min2);
//		}
//		
//		exec("right_angle.in", "right_angle.out");
	}
}
