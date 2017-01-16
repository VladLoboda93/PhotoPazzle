package controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controllers.actions.Action;

//@WebServlet("*.do")
@MultipartConfig
public class PhotomosaicController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	/*protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		writer.write("done");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		Part filePart = request.getPart("img_to_process");
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
		filePart.write("D:\\"+fileName);
		 
		response.sendRedirect("create_mosaic.do");		 	 
		 
	}*/


	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Action currentAction = AbstractActionFactory.getInstance().getAction(request);
		currentAction.execute(request, response);

		
	}
	
	

}
