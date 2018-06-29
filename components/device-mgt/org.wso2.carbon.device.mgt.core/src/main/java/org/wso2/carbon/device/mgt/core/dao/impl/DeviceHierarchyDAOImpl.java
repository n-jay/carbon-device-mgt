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
package org.wso2.carbon.device.mgt.core.dao.impl;

import org.wso2.carbon.device.mgt.common.Device;
import org.wso2.carbon.device.mgt.common.DeviceHierarchyDataContainer;
import org.wso2.carbon.device.mgt.core.dao.DeviceHierarchyDAO;
import org.wso2.carbon.device.mgt.core.dao.DeviceHierarchyDAOException;
import org.wso2.carbon.device.mgt.core.dao.DeviceManagementDAOFactory;
import org.wso2.carbon.device.mgt.core.dao.util.DeviceManagementDAOUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeviceHierarchyDAOImpl implements DeviceHierarchyDAO {
    @Override
    public int addDeviceToHierarchy(Device device, int tenantId) throws DeviceHierarchyDAOException {
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0;
        try {
            conn = this.getConnection();
            String sql = "INSERT INTO DM_DEVICE_HIERARCHY(DEVICE_ID, DEVICE_PARENT, IS_PARENT, TENANT_ID) VALUES " +
                    "(?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, device.getDeviceIdentifier());
            stmt.setString(2, device.getParentId());
            stmt.setInt(3, device.getIsParent());
            stmt.setInt(4, tenantId);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DeviceHierarchyDAOException("Error occurred while adding device '" + device.getDeviceIdentifier() +
                    "' to hierarchy", e);
        } finally {
            DeviceManagementDAOUtil.cleanupResources(stmt, null);
            return id;
        }
    }

    @Override
    public List<DeviceHierarchyDataContainer> getDevicesInHierarchy() throws DeviceHierarchyDAOException {
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<DeviceHierarchyDataContainer> deviceHierarchyMetadataArray = new ArrayList<>();
        DeviceHierarchyDataContainer deviceHierarchyMetadataHolder;
        try {
            conn = this.getConnection();
            String sql = "SELECT * FROM DM_DEVICE_HIERARCHY";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                deviceHierarchyMetadataHolder = this.loadHierarchy(rs);
                deviceHierarchyMetadataArray.add(deviceHierarchyMetadataHolder);
            }
        } catch (SQLException e) {
            throw new DeviceHierarchyDAOException("Error occurred while obtaining device list in hierarchy", e);
        } finally {
            DeviceManagementDAOUtil.cleanupResources(stmt, rs);
            return deviceHierarchyMetadataArray;
        }
    }

    @Override
    public DeviceHierarchyDataContainer getParentOfDeviceInHierarchy(int id) throws DeviceHierarchyDAOException {
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        DeviceHierarchyDataContainer deviceHierarchyMetadataHolder = new DeviceHierarchyDataContainer();
        try {
            conn = this.getConnection();
            String sql = "SELECT * FROM DM_DEVICE_HIERARCHY WHERE ID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                deviceHierarchyMetadataHolder.setId(rs.getInt("ID"));
                deviceHierarchyMetadataHolder.setDeviceId(rs.getString("DEVICE_ID"));
                deviceHierarchyMetadataHolder.setParentId(rs.getString("DEVICE_PARENT"));
                deviceHierarchyMetadataHolder.setIsParent(rs.getInt("IS_PARENT"));
                deviceHierarchyMetadataHolder.setTenantId(rs.getInt("TENANT_ID"));
            }
        } catch (SQLException e) {
            throw new DeviceHierarchyDAOException("Error occurred while getting parent of device list in hierarchy", e);
        } finally {
            DeviceManagementDAOUtil.cleanupResources(stmt, rs);
            return deviceHierarchyMetadataHolder;
        }
    }

    @Override
    public List<DeviceHierarchyDataContainer> getChildrenOfParentInHierarchy(String parentId)
            throws DeviceHierarchyDAOException {
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<DeviceHierarchyDataContainer> children = new ArrayList<>();
        DeviceHierarchyDataContainer deviceHierarchyMetadataHolder;
        try {
            conn = this.getConnection();
            String sql = "SELECT * FROM DM_DEVICE_HIERARCHY WHERE DEVICE_PARENT = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, parentId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                deviceHierarchyMetadataHolder = this.loadHierarchy(rs);
                children.add(deviceHierarchyMetadataHolder);
            }
        } catch (SQLException e) {
            throw new DeviceHierarchyDAOException("Error occurred while getting children of parent device list in " +
                    "hierarchy", e);
        } finally {
            DeviceManagementDAOUtil.cleanupResources(stmt, rs);
            return children;
        }
    }

    @Override
    public boolean setParentOfDeviceInHierarchy(int id, String parentId)
            throws DeviceHierarchyDAOException {
        boolean isUpdateSuccess = false;
        Connection conn;
        PreparedStatement stmt = null;
        int rows;

        try {
            conn = this.getConnection();
            String sql = "UPDATE DM_DEVICE_HIERARCHY SET DEVICE_PARENT = ? WHERE ID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, parentId);
            stmt.setInt(2, id);
            rows = stmt.executeUpdate();
            if (rows > 0) {
                isUpdateSuccess = true;
            }
        } catch (SQLException e) {
            throw new DeviceHierarchyDAOException("Error occurred while updating parent of device in " +
                    "hierarchy", e);
        } finally {
            DeviceManagementDAOUtil.cleanupResources(stmt, null);
            return isUpdateSuccess;
        }

    }

    private Connection getConnection() throws SQLException {
        return DeviceManagementDAOFactory.getConnection();
    }

    private DeviceHierarchyDataContainer loadHierarchy(ResultSet rs) throws SQLException {
        DeviceHierarchyDataContainer tempDeviceHierarchyDataContainer = new DeviceHierarchyDataContainer();
        tempDeviceHierarchyDataContainer.setId(rs.getInt("ID"));
        tempDeviceHierarchyDataContainer.setDeviceId(rs.getString("DEVICE_ID"));
        tempDeviceHierarchyDataContainer.setParentId(rs.getString("DEVICE_PARENT"));
        tempDeviceHierarchyDataContainer.setIsParent(rs.getInt("IS_PARENT"));
        tempDeviceHierarchyDataContainer.setTenantId(rs.getInt("TENANT_ID"));
        return tempDeviceHierarchyDataContainer;
    }
}
