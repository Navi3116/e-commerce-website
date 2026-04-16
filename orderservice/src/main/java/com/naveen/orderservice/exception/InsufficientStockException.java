package com.naveen.orderservice.exception;

public class InsufficientStockException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 187537240959878592L;

	/**
     * Constructs a new InsufficientStockException with the specified detail message.
     * * @param message The detail message (e.g., "Insufficient stock for Product ID: 101").
     */
    public InsufficientStockException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new InsufficientStockException with the specified detail message and cause.
     * * @param message The detail message.
     * @param cause The cause (which is typically the HttpClientErrorException from RestTemplate).
     */
    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
    }

}
