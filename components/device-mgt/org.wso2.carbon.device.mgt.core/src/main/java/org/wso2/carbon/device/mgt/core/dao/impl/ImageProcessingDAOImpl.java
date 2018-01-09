package org.wso2.carbon.device.mgt.core.dao.impl;

import org.wso2.carbon.device.mgt.core.dao.DeviceManagementDAOFactory;
import org.wso2.carbon.device.mgt.core.dao.ImageProcessingDAO;
import org.wso2.carbon.device.mgt.core.dao.ImageProcessingDAOException;
import org.wso2.carbon.device.mgt.core.dao.util.DeviceManagementDAOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageProcessingDAOImpl implements ImageProcessingDAO{
    @Override
        public boolean setImages(String deviceId, File image) throws ImageProcessingDAOException {
        Connection conn;
        PreparedStatement stmt = null;
        boolean isSuccess = false;
        Date todaysDate = new Date();
        DateFormat timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String timeStampString = timeStamp.format(todaysDate);
        try {
            conn = this.getConnection();
            String sql = "INSERT INTO IMAGES(DEVICE_ID, TIME_STAMP, IMAGE ) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, deviceId);
            stmt.setTimestamp(2, Timestamp.valueOf(image.getName()));
            image = new File(timeStampString);
            try {
                InputStream inputImage = new FileInputStream(image);
                stmt.setBlob(3, inputImage);
            } catch (FileNotFoundException e) {
                throw new ImageProcessingDAOException("Error occurred while finding image", e);
            }
            stmt.executeUpdate();
            isSuccess = true;
        } catch (SQLException e) {
            throw new ImageProcessingDAOException("Error occurred while saving image", e);
        } finally {
            DeviceManagementDAOUtil.cleanupResources(stmt, null);
            return isSuccess;
        }
    }

    @Override
    public File getImages(String id) throws ImageProcessingDAOException {
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        File imageResult = null;
        try {
            conn = this.getConnection();
            String sql = "SELECT IMAGE FROM IMAGES WHERE DEVICE_ID = ?";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                imageResult = (File) rs.getBlob("IMAGE");
            }
        } catch (SQLException e) {
            throw new ImageProcessingDAOException("Error occurred while saving image", e);
        } finally {
            DeviceManagementDAOUtil.cleanupResources(stmt, rs);
            return imageResult;
        }
    }

    private Connection getConnection() throws SQLException {
        return DeviceManagementDAOFactory.getConnection();
    }
}
