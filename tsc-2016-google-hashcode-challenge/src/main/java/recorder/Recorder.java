package recorder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Recorder {

	public static String RECORDING_PATH = "./results/";
	public static String RECORDING_NAME = "results.txt";
	public static String RECORDING_PRGM = "src.zip";

	public static String SRC_PATH = "src";
	
	public Recorder(String identifier, String content, String value) throws IOException {
		String folder = RECORDING_PATH + identifier +"/" + value + "/" + new SimpleDateFormat("HH_mm_ss").format(new Date());
		File f = new File(folder);
		if(!f.isDirectory())
			f.mkdirs();
		
		PrintWriter pw = new PrintWriter(f.getAbsolutePath() + "/" + RECORDING_NAME);
		pw.write(value + "\n");
		pw.write(content);
		pw.close();
		
		ZipHelper zippy = new ZipHelper();
		zippy.zipDir(SRC_PATH,f.getAbsolutePath() + "/" + RECORDING_PRGM);

		
	}
	
	class ZipHelper  
	{
	    public void zipDir(String dirName, String nameZipFile) throws IOException {
	        ZipOutputStream zip = null;
	        FileOutputStream fW = null;
	        fW = new FileOutputStream(nameZipFile);
	        zip = new ZipOutputStream(fW);
	        addFolderToZip("", dirName, zip);
	        zip.close();
	        fW.close();
	    }

	    private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws IOException {
	        File folder = new File(srcFolder);
	        if (folder.list().length == 0) {
	            addFileToZip(path , srcFolder, zip, true);
	        }
	        else {
	            for (String fileName : folder.list()) {
	                if (path.equals("")) {
	                    addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip, false);
	                } 
	                else {
	                     addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip, false);
	                }
	            }
	        }
	    }

	    private void addFileToZip(String path, String srcFile, ZipOutputStream zip, boolean flag) throws IOException {
	        File folder = new File(srcFile);
	        if (flag) {
	            zip.putNextEntry(new ZipEntry(path + "/" +folder.getName() + "/"));
	        }
	        else {
	            if (folder.isDirectory()) {
	                addFolderToZip(path, srcFile, zip);
	            }
	            else {
	                byte[] buf = new byte[1024];
	                int len;
	                FileInputStream in = new FileInputStream(srcFile);
	                zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
	                while ((len = in.read(buf)) > 0) {
	                    zip.write(buf, 0, len);
	                }
	                in.close();
	            }
	        }
	    }
	}
	
	
}
