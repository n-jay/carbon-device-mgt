package org.wso2.carbon.device.mgt.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "DeviceHierarchyDataContainer", description = "This class carries all metadata related to a device" +
        "in the device hierarchy")
public class DeviceHierarchyDataContainer implements Serializable {

    @ApiModelProperty(name = "id", value = "ID of the device as assigned by the server.",
            required = true)
    private String id;

    @ApiModelProperty(name = "deviceid", value = "Device id as provided by agent.",
            required = true)
    private String deviceId;

    @ApiModelProperty(name = "parentid", value = "Id of parent device is connected to.",
            required = true)
    private String parentId;

    @ApiModelProperty(name = "isParent", value = "Flag to check if device is a gateway or not.",
            required = true)
    private int isParent;

    @ApiModelProperty(name = "tenantId", value = "Tenant used for enrolling device.",
            required = true)
    private String tenantId;

    public DeviceHierarchyDataContainer() {
    }

    public DeviceHierarchyDataContainer(String id, String deviceId, String parentId, int isParent, String tenantId) {
        this.id = id;
        this.deviceId = deviceId;
        this.parentId = parentId;
        this.isParent = isParent;
        this.tenantId = tenantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getIsParent() {
        return isParent;
    }

    public void setIsParent(int isParent) {
        this.isParent = isParent;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
