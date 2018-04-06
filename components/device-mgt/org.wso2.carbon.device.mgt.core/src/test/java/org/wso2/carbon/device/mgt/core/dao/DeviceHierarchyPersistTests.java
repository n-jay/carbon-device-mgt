package org.wso2.carbon.device.mgt.core.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.device.mgt.common.Device;
import org.wso2.carbon.device.mgt.common.DeviceHierarchyDataContainer;
import org.wso2.carbon.device.mgt.common.TransactionManagementException;
import org.wso2.carbon.device.mgt.core.TestUtils;
import org.wso2.carbon.device.mgt.core.common.BaseDeviceManagementTest;
import org.wso2.carbon.device.mgt.core.common.TestDataHolder;
import org.wso2.carbon.device.mgt.core.dao.impl.DeviceHierarchyDAOImpl;
import org.wso2.carbon.device.mgt.core.dto.DeviceType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DeviceHierarchyPersistTests extends BaseDeviceManagementTest {

    private static final Log log = LogFactory.getLog(DeviceHierarchyPersistTests.class);

    DeviceHierarchyDAOImpl deviceHierarchyDAOimpl;
    List<DeviceHierarchyDataContainer> expectedArray;
    DeviceDAO deviceDAO;
    DeviceTypeDAO deviceTypeDAO;
    Device device = TestDataHolder.generateDummyDeviceData(TestDataHolder.TEST_DEVICE_TYPE);
    int tenantId = TestDataHolder.SUPER_TENANT_ID;

    @BeforeClass
    @Override
    public void init() throws Exception {
        initDataSource();
        preliminaryDevicePreparation();
        deviceHierarchyDAOimpl = new DeviceHierarchyDAOImpl();
        expectedArray = new ArrayList<>();
    }

    private void preliminaryDevicePreparation() {
        deviceDAO = DeviceManagementDAOFactory.getDeviceDAO();
        deviceTypeDAO = DeviceManagementDAOFactory.getDeviceTypeDAO();
        DeviceType deviceType = TestDataHolder.generateDeviceTypeData(TestDataHolder.TEST_DEVICE_TYPE);
        try {
            DeviceManagementDAOFactory.beginTransaction();
            deviceTypeDAO.addDeviceType(deviceType, TestDataHolder.SUPER_TENANT_ID, true);
        } catch (DeviceManagementDAOException e) {
            DeviceManagementDAOFactory.rollbackTransaction();
            String msg = "Error occurred while adding device type '" + deviceType.getName() + "'";
            log.error(msg, e);
        } catch (TransactionManagementException e) {
            String msg = "Error occurred while initiating transaction to persist device type '" +
                    deviceType.getName() + "'";
            log.error(msg, e);
        } finally {
            DeviceManagementDAOFactory.closeConnection();
        }

        Integer targetTypeId = null;
        try {
            targetTypeId = this.getDeviceTypeId(TestDataHolder.TEST_DEVICE_TYPE);
        } catch (DeviceManagementDAOException e) {
            String msg = "Error occurred while retrieving target device type id";
            log.error(msg, e);
        }
        Assert.assertNotNull(targetTypeId, "Device Type Id is null");
        deviceType.setId(targetTypeId);
        TestDataHolder.initialTestDeviceType = deviceType;
        try {
            DeviceManagementDAOFactory.beginTransaction();
            int deviceId = deviceDAO.addDevice(TestDataHolder.initialTestDeviceType.getId(), device, tenantId);
            device.setId(deviceId);
            deviceDAO.addEnrollment(device, tenantId);
            DeviceManagementDAOFactory.commitTransaction();
            TestDataHolder.initialTestDevice = device;
        } catch (DeviceManagementDAOException e) {
            DeviceManagementDAOFactory.rollbackTransaction();
            String msg = "Error occurred while adding '" + device.getType() + "' device with the identifier '" +
                    device.getDeviceIdentifier() + "'";
            log.error(msg, e);
        } catch (TransactionManagementException e) {
            String msg = "Error occurred while initiating transaction";
            log.error(msg, e);
        } finally {
            DeviceManagementDAOFactory.closeConnection();
        }
        int targetId = -1;
        try {
            targetId = this.getDeviceId(TestDataHolder.initialTestDevice.getDeviceIdentifier(),
                    TestDataHolder.SUPER_TENANT_ID);
        } catch (DeviceManagementDAOException e) {
            String msg = "Error occurred while retrieving device id";
            log.error(msg, e);
            Assert.fail(msg, e);
        }
        Assert.assertNotNull(targetId, "Device Id persisted in device management metadata repository upon '" +
                device.getType() + "' carrying the identifier '" + device.getDeviceIdentifier() + "', is null");
    }

    private int getDeviceTypeId(String deviceTypeName) throws DeviceManagementDAOException {
        int id = -1;
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "SELECT ID, NAME FROM DM_DEVICE_TYPE WHERE NAME = ?";

        try {
            Assert.assertNotNull(getDataSource(), "Data Source is not initialized properly");
            conn = getDataSource().getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, deviceTypeName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("ID");
            }
            return id;
        } catch (SQLException e) {
            String msg = "Error in fetching device type by name IOS";
            throw new DeviceManagementDAOException(msg, e);
        } finally {
            TestUtils.cleanupResources(conn, stmt, null);
        }
    }

    private int getDeviceId(String deviceIdentification, int tenantId) throws DeviceManagementDAOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int id = -1;
        try {
            Assert.assertNotNull(getDataSource(), "Data Source is not initialized properly");
            conn = getDataSource().getConnection();
            String sql = "SELECT ID FROM DM_DEVICE WHERE DEVICE_IDENTIFICATION = ? AND TENANT_ID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, deviceIdentification);
            stmt.setInt(2, tenantId);

            rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("ID");
            }
            return id;
        } catch (SQLException e) {
            String msg = "Error in fetching device by device identification id";
            throw new DeviceManagementDAOException(msg, e);
        } finally {
            TestUtils.cleanupResources(conn, stmt, rs);
        }
    }

    @Test
    public void addDeviceToHierarchyTest() {
        int id;
        try {
            DeviceManagementDAOFactory.beginTransaction();
            id = deviceHierarchyDAOimpl.addDeviceToHierarchy(
                    device.getDeviceIdentifier(), "g0", 0, tenantId);
            DeviceManagementDAOFactory.commitTransaction();
            expectedArray.add(new DeviceHierarchyDataContainer(
                    1, device.getDeviceIdentifier(), "g0", 0, tenantId));
            Assert.assertEquals(id, 1);
        } catch (TransactionManagementException e) {
            DeviceManagementDAOFactory.rollbackTransaction();
            String msg = "Error occurred while initiating transaction";
            log.error(msg, e);
            Assert.fail(msg, e);
        } catch (DeviceHierarchyDAOException e) {
            DeviceManagementDAOFactory.rollbackTransaction();
            String msg = "Unable to perform action with hierarchy";
            log.error(msg, e);
            Assert.fail(msg, e);
        } finally {
            DeviceManagementDAOFactory.closeConnection();
        }
    }

    @Test
    public void getDevicesInHierarchyTest() {
        int id;
        List<DeviceHierarchyDataContainer> resultArray;
        try {
            DeviceManagementDAOFactory.beginTransaction();
            id = deviceHierarchyDAOimpl.addDeviceToHierarchy(
                    device.getDeviceIdentifier(), "g0", 0, tenantId);
            DeviceManagementDAOFactory.commitTransaction();
            if (id > 0) {
                expectedArray.add(new DeviceHierarchyDataContainer(
                        1, device.getDeviceIdentifier(), "g0", 0, tenantId));
                resultArray = deviceHierarchyDAOimpl.getDevicesInHierarchy();
                arraylistAssertion(resultArray, expectedArray);
            } else {
                DeviceManagementDAOFactory.rollbackTransaction();
                String msg = "Error occurred while adding device " + device.getDeviceIdentifier() + " to array";
                log.error(msg);
                Assert.fail(msg);
            }
        } catch (TransactionManagementException e) {
            DeviceManagementDAOFactory.rollbackTransaction();
            String msg = "Error occurred while initiating transaction";
            log.error(msg, e);
            Assert.fail(msg, e);
        } catch (DeviceHierarchyDAOException e) {
            DeviceManagementDAOFactory.rollbackTransaction();
            String msg = "Unable to perform action with hierarchy";
            log.error(msg, e);
            Assert.fail(msg, e);
        } finally {
            DeviceManagementDAOFactory.closeConnection();
        }
    }

    @Test
    public void getParentOfDeviceInHierarchyTest() {
        int id;
        DeviceHierarchyDataContainer result;
        List<DeviceHierarchyDataContainer> resultArray = new ArrayList<>();
        List<DeviceHierarchyDataContainer> expectedArray = new ArrayList<>();
        try {
            DeviceManagementDAOFactory.beginTransaction();
            id = deviceHierarchyDAOimpl.addDeviceToHierarchy(
                    device.getDeviceIdentifier(), "g0", 0, tenantId);
            DeviceManagementDAOFactory.commitTransaction();
            if (id > 0) {
                DeviceHierarchyDataContainer expected = new DeviceHierarchyDataContainer(id, device
                        .getDeviceIdentifier()
                        , "g0", 0, tenantId);
                expectedArray.add(expected);
                result = deviceHierarchyDAOimpl.getParentOfDeviceInHierarchy(id);
                DeviceManagementDAOFactory.commitTransaction();
                resultArray.add(result);
                arraylistAssertion(resultArray, expectedArray);
            } else {
                DeviceManagementDAOFactory.rollbackTransaction();
                String msg = "Error occurred while adding device " + device.getDeviceIdentifier() + " to array";
                log.error(msg);
                Assert.fail(msg);
            }
        } catch (TransactionManagementException e) {
            DeviceManagementDAOFactory.rollbackTransaction();
            String msg = "Error occurred while initiating transaction";
            log.error(msg, e);
            Assert.fail(msg, e);
        } catch (DeviceHierarchyDAOException e) {
            DeviceManagementDAOFactory.rollbackTransaction();
            String msg = "Unable to perform action with hierarchy";
            log.error(msg, e);
            Assert.fail(msg, e);
        } finally {
            DeviceManagementDAOFactory.closeConnection();
        }
    }

    @Test
    public void getChildrenOfParentInHierarchyTest() {
        int id;
        List<DeviceHierarchyDataContainer> resultArray = new ArrayList<>();
        List<DeviceHierarchyDataContainer> expectedArray = new ArrayList<>();
        try {
            DeviceManagementDAOFactory.beginTransaction();
            id = deviceHierarchyDAOimpl.addDeviceToHierarchy(
                    device.getDeviceIdentifier(), "g0", 0, tenantId);
            DeviceManagementDAOFactory.commitTransaction();
            if (id > 0) {
                DeviceHierarchyDataContainer expected = new DeviceHierarchyDataContainer(id, device
                        .getDeviceIdentifier()
                        , "g0", 0, tenantId);
                expectedArray.add(expected);
                resultArray = deviceHierarchyDAOimpl.getChildrenOfParentInHierarchy("g0");
                DeviceManagementDAOFactory.commitTransaction();
                arraylistAssertion(resultArray, expectedArray);
            } else {
                DeviceManagementDAOFactory.rollbackTransaction();
                String msg = "Error occurred while adding device " + device.getDeviceIdentifier() + " to array";
                log.error(msg);
                Assert.fail(msg);
            }
        } catch (TransactionManagementException e) {
            DeviceManagementDAOFactory.rollbackTransaction();
            String msg = "Error occurred while initiating transaction";
            log.error(msg, e);
            Assert.fail(msg, e);
        } catch (DeviceHierarchyDAOException e) {
            DeviceManagementDAOFactory.rollbackTransaction();
            String msg = "Unable to perform action with hierarchy";
            log.error(msg, e);
            Assert.fail(msg, e);
        } finally {
            DeviceManagementDAOFactory.closeConnection();
        }
    }

    @Test
    public void setParentOfDeviceInHierarchyTest() {
        int id;
        boolean isUpdateSuccess;
        try {
            DeviceManagementDAOFactory.beginTransaction();
            id = deviceHierarchyDAOimpl.addDeviceToHierarchy(
                    device.getDeviceIdentifier(), "g0", 0, tenantId);
            DeviceManagementDAOFactory.commitTransaction();
            if (id > 0) {
                isUpdateSuccess = deviceHierarchyDAOimpl.setParentOfDeviceInHierarchy(id, "gX");
                Assert.assertEquals(isUpdateSuccess, true);
            } else {
                DeviceManagementDAOFactory.rollbackTransaction();
                String msg = "Error occurred while adding device " + device.getDeviceIdentifier() + " to array";
                log.error(msg);
                Assert.fail(msg);
            }
        } catch (TransactionManagementException e) {
            DeviceManagementDAOFactory.rollbackTransaction();
            String msg = "Error occurred while initiating transaction";
            log.error(msg, e);
            Assert.fail(msg, e);
        } catch (DeviceHierarchyDAOException e) {
            DeviceManagementDAOFactory.rollbackTransaction();
            String msg = "Unable to perform action with hierarchy";
            log.error(msg, e);
            Assert.fail(msg, e);
        } finally {
            DeviceManagementDAOFactory.closeConnection();
        }
    }

    /**
     * This method is used to compare to ArrayList objects
     *
     * @param resultArray   array that is retrieved from DAO method
     * @param expectedArray array that is expected from the DAO method retrieval
     */
    private void arraylistAssertion(List<DeviceHierarchyDataContainer> resultArray,
                                    List<DeviceHierarchyDataContainer> expectedArray) {
        for (int counter = 0; counter <= (expectedArray.size() - 1); counter++) {
            Assert.assertEquals(resultArray.get(counter).getDeviceId(), expectedArray.get(counter).getDeviceId());
            Assert.assertEquals(resultArray.get(counter).getParentId(), expectedArray.get(counter).
                    getParentId());
            Assert.assertEquals(resultArray.get(counter).getIsParent(), expectedArray.get(counter).getIsParent());
            Assert.assertEquals(resultArray.get(counter).getTenantId(), expectedArray.get(counter).getTenantId());
        }
    }


}
