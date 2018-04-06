package org.wso2.carbon.device.mgt.core.dao;

public class DeviceHierarchyDAOException extends Exception {
    private String message;

    /**
     * Constructs a new exception with the specified detail message and nested exception.
     *
     * @param message         error message
     * @param nestedException exception
     */
    public DeviceHierarchyDAOException(String message, Exception nestedException) {
        super(message, nestedException);
        setErrorMessage(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detailed message.
     * @param cause   the cause of this exception
     */
    public DeviceHierarchyDAOException(String message, Throwable cause) {
        super(message, cause);
        setErrorMessage(message);
    }

    /**
     * Constructs a new exception with the specified detail message
     *
     * @param message the detail message.
     */
    public DeviceHierarchyDAOException(String message) {
        super(message);
        setErrorMessage(message);
    }

    /**
     * Constructs a new exception with the specified and cause.
     *
     * @param cause the cause of this exception.
     */
    public DeviceHierarchyDAOException(Throwable cause) {
        super(cause);
    }

    public String getMessage() {
        return message;
    }

    public void setErrorMessage(String errorMessage) {
        this.message = errorMessage;
    }
}
