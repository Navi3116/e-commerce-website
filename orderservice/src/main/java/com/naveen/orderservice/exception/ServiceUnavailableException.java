package com.naveen.orderservice.exception;

public class ServiceUnavailableException extends RuntimeException{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceUnavailableException(String message) {
	        super(message);
	    }
	    
	    /**
	     * Constructs a new InsufficientStockException with the specified detail message and cause.
	     * * @param message The detail message.
	     * @param cause The cause (which is typically the HttpClientErrorException from RestTemplate).
	     */
	    public ServiceUnavailableException(String message, Throwable cause) {
	        super(message, cause);
	    }
}
