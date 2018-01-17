package org.wso2.carbon.device.mgt.core.dao.impl;

import org.apache.commons.ssl.Base64;
import org.wso2.carbon.device.mgt.core.dao.DeviceManagementDAOFactory;
import org.wso2.carbon.device.mgt.core.dao.ImageProcessingDAO;
import org.wso2.carbon.device.mgt.core.dao.ImageProcessingDAOException;
import org.wso2.carbon.device.mgt.core.dao.util.DeviceManagementDAOUtil;

import java.io.*;
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
        FileInputStream imageInputStream;
        try {
            conn = this.getConnection();
            String sql = "INSERT INTO IMAGES(DEVICE_ID, TIME_STAMP, IMAGE ) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, deviceId);
            stmt.setString(2, timeStampString);
            try {
                imageInputStream = new FileInputStream(image);
                stmt.setBinaryStream(3, imageInputStream);
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
    public String getImages(String deviceId) throws ImageProcessingDAOException {
        Connection conn;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        File imageResult = null;
        InputStream imageStream;
        FileOutputStream imageOutputStream;
        String img64str = "";
        Blob blob;
        try {
            conn = this.getConnection();
            String sql = "SELECT * FROM IMAGES WHERE DEVICE_ID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, deviceId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                blob = rs.getBlob("IMAGE");
                imageStream = blob.getBinaryStream(3, blob.length());
                String str = imageStream.toString();
                byte[] bdata=str.getBytes();
                byte[] img64 = Base64.encodeBase64(bdata);
                img64str = new String(img64);

            }
        } catch (SQLException e) {
            throw new ImageProcessingDAOException("Error occurred while retrieving image", e);
        } finally {
            DeviceManagementDAOUtil.cleanupResources(stmt, rs);
            return img64str;
        }
    }

    private Connection getConnection() throws SQLException {
        return DeviceManagementDAOFactory.getConnection();
    }
}
