package org.wso2.carbon.device.mgt.core.dao.impl;

import org.wso2.carbon.device.mgt.core.dao.DeviceOrganizationDAOException;
import org.wso2.carbon.device.mgt.core.dao.ImageProcessingDAO;

import java.io.File;

public class ImageProcessingDAOImpl implements ImageProcessingDAO{
    @Override
    public boolean setImages(File image) throws DeviceOrganizationDAOException {
        return false;
    }

    @Override
    public File getImages(String id) throws DeviceOrganizationDAOException {
        return null;
    }
}
