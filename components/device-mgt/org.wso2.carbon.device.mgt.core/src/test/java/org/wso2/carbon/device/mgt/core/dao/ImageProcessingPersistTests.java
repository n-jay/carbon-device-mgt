package org.wso2.carbon.device.mgt.core.dao;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.device.mgt.core.common.BaseDeviceManagementTest;
import org.wso2.carbon.device.mgt.core.dao.impl.ImageProcessingDAOImpl;

import java.io.File;

public class ImageProcessingPersistTests extends BaseDeviceManagementTest{

    ImageProcessingDAOImpl imageProcessingDAOimpl;
    File expectedFile;

    @BeforeClass
    @Override
    public void init() throws Exception {
        imageProcessingDAOimpl = new ImageProcessingDAOImpl();
        expectedFile = new File("");
    }

    @Test
    public void setImagesTest() {

    }

    @Test (dependsOnMethods = {"setImagesTest"})
    public void getImagesTest() {

    }
}
