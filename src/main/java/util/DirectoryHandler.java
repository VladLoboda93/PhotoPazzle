package util;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class DirectoryHandler {
	
	public static void unzipFile(String zipSource, String zipDestination) throws ZipException {
			
		ZipFile zipFile = new ZipFile(zipSource);      
		zipFile.extractAll(zipDestination);
		
	}
	
	//TODO переписать в стиле java8. тут возможно будет слабая производительность.
	//     либо переделать возвращаемые данные в формат file
	public static LinkedList<String> getFilesSpis(String rootDir) {
		
		File root = new File(rootDir);
		LinkedList<String> fileNames = new LinkedList<>();
		
		if (root.isDirectory()) {
			File[] files = root.listFiles();
			
			for (File f : files) {
				getOnlyFiles(f, fileNames);
			}
		} else {
			throw new RuntimeException();
		}
		return fileNames;
			
		
		
		
	}
	
	private static void getOnlyFiles(File currentFile, List<String> fNames) {
		
		if (currentFile.isDirectory()) {
			File[] files = currentFile.listFiles();
			
			for (File f : files) {
				getOnlyFiles(f, fNames);
			}
		} else {
			fNames.add(currentFile.getAbsolutePath());
		}
		
	}
	
	
	public static boolean isImageFile(String filename) {
		
		String extension = FilenameUtils.getExtension(filename);
		
		//TODO возможно стоит вынести за метод
		ArrayList<String> correctExtentions = new ArrayList<>();
		correctExtentions.add("png");
		correctExtentions.add("jpg");
		
		if (correctExtentions.contains(extension))
			return true;
		else
			return false;		
	}
	
	public static List<String> selectOnlyImageFiles(List<String> files) {
		
		//TODO переделывать! нельзя указывать тип здесь: он может отличаться от исходного!
		List<String> res = new LinkedList<>();
		
		for (String f : files) {
			if (isImageFile(f))
				res.add(f);
				
		}
		
		return res;
		
	}
	
	//use only for test
	public static void main(String[] args) {
		List<String> res = new LinkedList<>();
		res = getFilesSpis("C:\\Users\\USER\\Desktop\\Altium Designer - Видеоуроки");
		System.out.println(res);
	}

}
