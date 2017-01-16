package controllers.actions;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import net.lingala.zip4j.exception.ZipException;
import util.DirectoryHandler;

public class CreateMosaicAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		//TODO мозайка из шаблона (это относится в моему видению)
		if (true) {
			createFromArchive(request, response);
		}
		
	}
	
	private void createFromTemplate(HttpServletRequest request, HttpServletResponse response) {
		//TODO мозайка из шаблона (это относится в моему видению)
	};
	
	private void createFromArchive(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		
		String imageToProcess = uploadFile(request, "img_to_process");
		String tilesArchive = uploadFile(request, "tiles_zip");
		
		String tilesDir;
		
		try {
			// TODO некрасиво
			String t = tilesArchive.substring(0, tilesArchive.length()- 4);
			DirectoryHandler.unzipFile(tilesArchive, t);
			tilesDir = t;
			
			// связанный список выбран, т.к. будет много удалений из середины (в моем видении)
			// если я правильно понял, что хочет Влад, то возможно лучше переделать в ArrayList
			List<String> tiles = new LinkedList<>();
			
			tiles = DirectoryHandler.getFilesSpis(tilesDir);
			
			//TODO временно для тестов
			System.out.println(tiles);
			
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//TODO вызвать код из пакета services
		//TODO что возвращать?
		return;	
		
	}
	
	private String uploadFile(HttpServletRequest request, String parameterName) throws IOException, ServletException {
		
		String rootDirectory = request.getServletContext().getInitParameter("images_directory");
		Part filePart = request.getPart(parameterName);
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
		String fullName = rootDirectory + fileName;
		filePart.write(fullName);
		
		return fullName;
	}

}
