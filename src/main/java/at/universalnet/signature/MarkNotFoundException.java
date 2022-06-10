package at.universalnet.signature;

public class MarkNotFoundException extends Exception {

	/**
	 * TemplateException is thrown, when was not possible to apply the template
	 */
	private static final long serialVersionUID = 1L;

	public MarkNotFoundException() {
		super();
	}

	public MarkNotFoundException(String message) {
		super(message);
	}

	public MarkNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
