package org.wso2.carbon.device.mgt.core.dao;

import java.io.File;
import java.io.FileOutputStream;

/**
 * This class represents key operations related to maintaining image storing and retrieval
 */
public interface ImageProcessingDAO {

    boolean setImages(String id, File image) throws ImageProcessingDAOException;

    String getImages(String deviceId) throws ImageProcessingDAOException;
}
