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

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		Action currentAction = AbstractActionFactory.getInstance().getAction(request);
		currentAction.execute(request, response);	
	}
}
