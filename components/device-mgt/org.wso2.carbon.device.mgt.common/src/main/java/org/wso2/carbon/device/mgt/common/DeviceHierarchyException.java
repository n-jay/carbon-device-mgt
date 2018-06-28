package org.wso2.carbon.device.mgt.common;

public class DeviceHierarchyException extends Exception {

    private static final long serialVersionUID = -3151279311929070297L;

    public DeviceHierarchyException(String msg, Exception nestedEx) {
        super(msg, nestedEx);
    }

    public DeviceHierarchyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeviceHierarchyException(String msg) {
        super(msg);
    }

    public DeviceHierarchyException() {
        super();
    }

    public DeviceHierarchyException(Throwable cause) {
        super(cause);
    }
}
