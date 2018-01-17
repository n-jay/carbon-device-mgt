package org.wso2.carbon.device.mgt.core.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.device.mgt.common.TransactionManagementException;
import org.wso2.carbon.device.mgt.core.common.BaseDeviceManagementTest;
import org.wso2.carbon.device.mgt.core.dao.impl.ImageProcessingDAOImpl;

import java.io.File;

public class ImageProcessingPersistTests extends BaseDeviceManagementTest{

    private static final Log log = LogFactory.getLog(ImageProcessingPersistTests.class);

    ImageProcessingDAOImpl imageProcessingDAOimpl;
    File expectedFile;
    String fileName = "images/testImage.png";
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    @BeforeClass
    @Override
    public void init() throws Exception {
        imageProcessingDAOimpl = new ImageProcessingDAOImpl();
        expectedFile = new File(classLoader.getResource(fileName).getFile());
    }

    @Test
    public void setImagesTest() {
        boolean isAddSuccess;
        try {
            DeviceManagementDAOFactory.beginTransaction();
            isAddSuccess = imageProcessingDAOimpl.setImages("dev1", new File(classLoader
                    .getResource(fileName).getFile()));
            DeviceManagementDAOFactory.commitTransaction();
            Assert.assertTrue(isAddSuccess);
            DeviceManagementDAOFactory.rollbackTransaction();
        } catch (TransactionManagementException e) {
            DeviceManagementDAOFactory.rollbackTransaction();
            String msg = "Error occurred while initiating transaction";
            log.error(msg, e);
            Assert.fail(msg, e);
        } catch (ImageProcessingDAOException e) {
            DeviceManagementDAOFactory.rollbackTransaction();
            String msg = "Unable to save image";
            log.error(msg, e);
            Assert.fail(msg, e);
        } finally {
            DeviceManagementDAOFactory.closeConnection();
        }
    }

    @Test (dependsOnMethods = {"setImagesTest"})
    public void getImagesTest() {
        String output;
        try {
            DeviceManagementDAOFactory.beginTransaction();
            output = imageProcessingDAOimpl.getImages("dev1");
        } catch (TransactionManagementException e) {
            e.printStackTrace();
        } catch (ImageProcessingDAOException e) {
            e.printStackTrace();
        } finally {
            DeviceManagementDAOFactory.closeConnection();
        }
    }
}
