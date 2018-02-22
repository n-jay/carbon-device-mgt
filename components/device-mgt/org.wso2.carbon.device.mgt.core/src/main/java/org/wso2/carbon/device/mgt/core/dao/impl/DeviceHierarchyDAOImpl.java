/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.device.mgt.core.dao.impl;

import org.wso2.carbon.device.mgt.core.dao.DeviceHierarchyDAO;
import org.wso2.carbon.device.mgt.core.dao.DeviceHierarchyDAOException;
import org.wso2.carbon.device.mgt.core.dao.DeviceManagementDAOFactory;
import org.wso2.carbon.device.mgt.core.dao.util.DeviceManagementDAOUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeviceHierarchyDAOImpl implements DeviceHierarchyDAO {
    @Override
    public boolean addDeviceToHierarchy(String deviceId, String deviceParent, int isParent, int tenantId)
            throws DeviceHierarchyDAOException {
        Connection conn;
        PreparedStatement stmt = null;
        boolean isSuccess = false;
        try {
            conn = this.getConnection();
            String sql = "INSERT INTO DM_DEVICE_HIERARCHY(DEVICE_ID, DEVICE_PARENT, IS_PARENT, TENANT_ID) " +
                    "VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, deviceId);
            stmt.setString(2, deviceParent);
            stmt.setInt(3, isParent);
            stmt.setInt(4, tenantId);
            stmt.executeUpdate();
            isSuccess = true;
        } catch (SQLException e) {
            throw new DeviceHierarchyDAOException("Error occurred while adding device '" + deviceId +
                    "'", e);
        } finally {
            DeviceManagementDAOUtil.cleanupResources(stmt, null);
            return isSuccess;
        }
    }

    @Override
    public boolean removeDeviceFromHierarchy(String deviceId) throws DeviceHierarchyDAOException {
        return false;
    }

    @Override
    public boolean updateDeviceHierarchyParent(String deviceId, String newParentId)
            throws DeviceHierarchyDAOException {
        return false;
    }

    @Override
    public boolean updateDeviceHierarchyParencyState(String deviceId, int newParencyState)
            throws DeviceHierarchyDAOException {
        return false;
    }

    @Override
    public boolean updateDeviceHierarchyTenantId(String deviceId, int newTenantId) throws DeviceHierarchyDAOException {
        return false;
    }

    private Connection getConnection() throws SQLException {
        return DeviceManagementDAOFactory.getConnection();
    }
}
