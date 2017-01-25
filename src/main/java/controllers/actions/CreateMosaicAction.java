package controllers.actions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import controllers.Executor;
import net.lingala.zip4j.exception.ZipException;
import util.DirectoryHandler;

public class CreateMosaicAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String imageToProcess;
		List<String> tiles;
		
		String rootDirectory = createRequestUniqueDir(request);
		
		imageToProcess = reciveImgToProcess(request, rootDirectory);
		tiles = reciveTiles(request, rootDirectory);

        Executor executor = new Executor(imageToProcess,tiles,rootDirectory);
				
		
		//TODO вызвать код из пакета services
		//TODO что возвращать?
		return;	
		
	}
	
	private String createRequestUniqueDir(HttpServletRequest request) {
		
		String rootDirectory = request.getServletContext().getRealPath("/") 
				 			 + request.getServletContext().getInitParameter("images_directory");
		String uniqueDirectory = Thread.currentThread().getName().toString() + " "
				 			   + (new Date()).toString() + "\\";
		uniqueDirectory = uniqueDirectory.replace(':', '-');
		rootDirectory += uniqueDirectory;

		File f = new File(rootDirectory);
		f.mkdirs();

		//TODO временно для тестов
		System.out.println(rootDirectory);
		
		return rootDirectory;
		
	}
	
	private String reciveImgToProcess(HttpServletRequest request,  String targetDirectory) throws IOException, ServletException {
		
		String imageToProcess = null;
		
		String imgSource = request.getParameter("img_source");
		
		if (imgSource.equals("from_pc")) {
			imageToProcess = uploadFile(request, "img_to_process", targetDirectory);
		} else if (imgSource.equals("by_url")) {
			imageToProcess = downloadImageByUrl(request.getParameter("img_to_process"));
		}
		
		if (imageToProcess != null)
			return imageToProcess;
		else
			throw new RuntimeException();
	}

	private List<String> reciveTiles(HttpServletRequest request, String targetDirectory) throws IOException, ServletException {
		
		List<String> tiles = null;
		
		String tilesSource = request.getParameter("tiles_source");
		
		if (tilesSource.equals("template")) {
			//TODO плитка из шаблона
		} else if (tilesSource.equals("upload")) {
			tiles = recieveFromZip(request, targetDirectory);
			
		}
		
		if (tiles != null)
			return tiles;
		else
			throw new RuntimeException();
		
	}
	
	private List<String> recieveFromZip(HttpServletRequest request,  String targetDirectory) throws IOException, ServletException {
		
		String tilesArchive = uploadFile(request, "tiles_zip", targetDirectory);
		
		String tilesDir;
		List<String> tiles = null;
		
		try {
			// TODO некрасиво
			String t = tilesArchive.substring(0, tilesArchive.length()- 4);
			DirectoryHandler.unzipFile(tilesArchive, t);
			tilesDir = t;
			
			// связанный список выбран, т.к. будет много удалений из середины (в моем видении)
			// если я правильно понял, что хочет Влад, то возможно лучше переделать в ArrayList
			tiles = new LinkedList<>();
			
			tiles = DirectoryHandler.getFilesSpis(tilesDir);
			
			//TODO временно для тестов
			System.out.println(tiles);
			
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tiles;
	}
	
	private String downloadImageByUrl(String url){
		BufferedImage img;
        	String fileName = "fromlink.png";
        	try {
          		try {
           	     		img = ImageIO.read(new URL(url));
          		}
			catch (MalformedURLException m) {
                		return null;
          		}
          		File file = new File( targetDirectory + fileName);
            		if (!file.exists()) {
                		file.createNewFile();
            		}
            		ImageIO.write(img, "png", file);
        	}
        	catch(IOException e){
            		//TODO ситуация с одинаковыми именами файлов
            		return null;
        	}
       		return targetDirectory + fileName;
	}
	
	private String uploadFile(HttpServletRequest request, String parameterName, String targetDirectory) throws IOException, ServletException {
		
	/*	String rootDirectory = request.getServletContext().getRealPath("/") + request.getServletContext().getInitParameter("images_directory");
		//TODO временно для тестов
		System.out.println(rootDirectory);*/
		Part filePart = request.getPart(parameterName);
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
		String fullName = targetDirectory + fileName;
		filePart.write(fullName);
		
		return fullName;
	}

}
