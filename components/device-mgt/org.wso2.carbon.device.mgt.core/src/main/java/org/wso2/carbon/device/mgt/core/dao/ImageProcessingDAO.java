package org.wso2.carbon.device.mgt.core.dao;

import java.io.File;

/**
 * This class represents key operations related to maintaining image storing and retrieval
 */
public interface ImageProcessingDAO {

    boolean setImages(String id, File image) throws ImageProcessingDAOException;

        File getImages(String id) throws ImageProcessingDAOException;
}
