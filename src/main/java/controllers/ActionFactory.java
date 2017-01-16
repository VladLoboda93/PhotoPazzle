package controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import controllers.actions.Action;
import controllers.actions.CreateMosaicAction;
import controllers.actions.MistakeAction;

public class ActionFactory {

	private Map<String, Action> actions = new HashMap<>();

	public ActionFactory() {

		actions.put("GET/mistake.do", new MistakeAction());
		actions.put("POST/create_mosaic.do", new CreateMosaicAction());
		
	}
	
	public synchronized Action getAction(HttpServletRequest request) {
		
		String path = request.getServletPath();// + request.getPathInfo();
		String actionKey = request.getMethod() + path;
		
		Action action = actions.get(actionKey);
		
		if (action == null) {
			action = actions.get("GET/mistake.do");
		}
		
		return action;
		
	}
	
	
	
}
