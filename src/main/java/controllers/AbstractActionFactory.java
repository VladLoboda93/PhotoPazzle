package controllers;

public abstract class AbstractActionFactory {
	
	private final static ActionFactory instance = new ActionFactory();

	public static ActionFactory getInstance() {
		return instance;
	}
}
