package at.universalnet.signature;

public class TemplateException extends Exception {

	/**
	 * TemplateException is thrown, when was not possible to apply the template
	 */
	private static final long serialVersionUID = 1L;

	public TemplateException() {
		super();
	}

	public TemplateException(String message) {
		super(message);
	}

	public TemplateException(String message, Throwable cause) {
		super(message, cause);
	}

}
