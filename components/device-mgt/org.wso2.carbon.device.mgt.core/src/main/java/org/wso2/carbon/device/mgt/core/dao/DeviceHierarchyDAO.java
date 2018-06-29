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
package org.wso2.carbon.device.mgt.core.dao;

import org.wso2.carbon.device.mgt.common.Device;
import org.wso2.carbon.device.mgt.common.DeviceHierarchyDataContainer;

import java.util.List;

public interface DeviceHierarchyDAO {

    /**
     * Add metadata of a new device to organizational hierarchy.
     *
     * @param id       unique identifier given to device for internal access
     * @param deviceId unique identifier as provided by
     * @param parentId parent that device is connected
     * @param isParent states whether device is an agent
     * @param tenantId id of the tenant that device was enrolled with
     * @return true if enrolled successfully
     * @throws DeviceHierarchyDAOException
     */
    int addDeviceToHierarchy(Device device, int tenantId) throws DeviceHierarchyDAOException;

    /**
     * Retrieve metadata of all enrolled devices in hierarchy.
     *
     * @return Array with all devices
     * @throws DeviceHierarchyDAOException
     */
    List<DeviceHierarchyDataContainer> getDevicesInHierarchy() throws DeviceHierarchyDAOException;

    /**
     * Retrieve details about the parent the device is connected to in the hierarchy.
     *
     * @param id unique identifier given to device for internal access
     * @return Details of parent device
     * @throws DeviceHierarchyDAOException
     */
    DeviceHierarchyDataContainer getParentOfDeviceInHierarchy(int id) throws
            DeviceHierarchyDAOException;

    /**
     * Retrieve list of child devices of a parent in the hierarchy.
     *
     * @param id unique identifier given to device for internal access
     * @return Array with child devices
     * @throws DeviceHierarchyDAOException
     */
    List<DeviceHierarchyDataContainer> getChildrenOfParentInHierarchy(String parentId) throws
            DeviceHierarchyDAOException;

    /**
     * Updates the parent the device is connected to in the hierarchy.
     *
     * @param id       unique identifier given to device for internal access
     * @param ParentId unique identifier of new parent
     * @return
     * @throws DeviceHierarchyDAOException
     */
    boolean setParentOfDeviceInHierarchy(int id, String parentId) throws
            DeviceHierarchyDAOException;

}
