/*
 *   Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.wso2.carbon.device.mgt.core.service;

import org.wso2.carbon.device.mgt.common.Device;
import org.wso2.carbon.device.mgt.common.DeviceHierarchyException;

import java.util.List;

/**
 * Interface that implements device hierarchy OSGi services
 */
public interface DeviceHierarchyProviderService {

    /**
     * This method adds a newly enrolled device to the hierarchy
     *
     * @param device metadata about device being enrolled
     * @return returns true when device is added successfully
     * @throws DeviceHierarchyException
     */
    boolean addDeviceToHierarchy(Device device) throws DeviceHierarchyException;

    /**
     * This method allows retrieval of all the devices in the hierarchy
     *
     * @return list of hierarchy
     * @throws DeviceHierarchyException
     */
    List<Device> getDevicesInHierarchy() throws DeviceHierarchyException;

    /**
     * This method allows retrieval of parentid of a device in the hierarchy
     *
     * @param deviceId unique identifier of device
     * @return details of parent device
     * @throws DeviceHierarchyException
     */
    Device getParentOfDeviceInHierarchy(int deviceId) throws DeviceHierarchyException;

    /**
     * Retrieves list of children connected to a parent in the hierarchy
     *
     * @param deviceId unique identifier of device
     * @return list of child devices
     * @throws DeviceHierarchyException
     */
    List<Device> getChildrenOfDeviceInHierarchy(int deviceId) throws DeviceHierarchyException;

    /**
     * This method allows updating a devices' parent in the hierarchy
     *
     * @param deviceId unique identifier of device
     * @param parentId unique identifier of parent device
     * @return true if update is successful
     * @throws DeviceHierarchyException
     */
    boolean setParentOfDeviceHierarchy(int deviceId, int parentId) throws DeviceHierarchyException;

}
