CREATE TABLE IF NOT EXISTS DM_DEVICE_TYPE (
     ID INT AUTO_INCREMENT NOT NULL,
     NAME VARCHAR(300) NULL DEFAULT NULL,
     DEVICE_TYPE_META VARCHAR(20000) NULL DEFAULT NULL,
     LAST_UPDATED_TIMESTAMP TIMESTAMP NOT NULL,
     PROVIDER_TENANT_ID INTEGER DEFAULT 0,
     SHARED_WITH_ALL_TENANTS BOOLEAN NOT NULL DEFAULT FALSE,
     PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS DM_GROUP (
  ID                  INTEGER AUTO_INCREMENT NOT NULL,
  GROUP_NAME          VARCHAR(100) DEFAULT NULL,
  DESCRIPTION         TEXT         DEFAULT NULL,
  OWNER               VARCHAR(45)  DEFAULT NULL,
  TENANT_ID           INTEGER      DEFAULT 0,
  PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS DM_ROLE_GROUP_MAP (
  ID        INTEGER AUTO_INCREMENT NOT NULL,
  GROUP_ID  INTEGER     DEFAULT NULL,
  ROLE      VARCHAR(45) DEFAULT NULL,
  TENANT_ID INTEGER     DEFAULT 0,
  PRIMARY KEY (ID),
  CONSTRAINT fk_DM_ROLE_GROUP_MAP_DM_GROUP2 FOREIGN KEY (GROUP_ID)
  REFERENCES DM_GROUP (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS DM_DEVICE (
     ID                    INTEGER auto_increment NOT NULL,
     DESCRIPTION           TEXT DEFAULT NULL,
     NAME                  VARCHAR(100) DEFAULT NULL,
     DEVICE_TYPE_ID        INT(11) DEFAULT NULL,
     DEVICE_IDENTIFICATION VARCHAR(300) DEFAULT NULL,
     LAST_UPDATED_TIMESTAMP TIMESTAMP NOT NULL,
     TENANT_ID INTEGER DEFAULT 0,
     PRIMARY KEY (ID),
     CONSTRAINT fk_DM_DEVICE_DM_DEVICE_TYPE2 FOREIGN KEY (DEVICE_TYPE_ID)
     REFERENCES DM_DEVICE_TYPE (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
     CONSTRAINT uk_DM_DEVICE UNIQUE (NAME, DEVICE_TYPE_ID, DEVICE_IDENTIFICATION, TENANT_ID)
);

CREATE TABLE IF NOT EXISTS DM_DEVICE_PROPERTIES (
     DEVICE_TYPE_NAME VARCHAR(300) NOT NULL,
     DEVICE_IDENTIFICATION VARCHAR(300) NOT NULL,
     PROPERTY_NAME VARCHAR(100) DEFAULT 0,
     PROPERTY_VALUE VARCHAR(100) DEFAULT NULL,
     TENANT_ID VARCHAR(100),
     PRIMARY KEY (DEVICE_TYPE_NAME, DEVICE_IDENTIFICATION, PROPERTY_NAME, TENANT_ID)
);

CREATE TABLE IF NOT EXISTS DM_DEVICE_GROUP_MAP (
  ID        INTEGER AUTO_INCREMENT NOT NULL,
  DEVICE_ID INTEGER DEFAULT NULL,
  GROUP_ID  INTEGER DEFAULT NULL,
  TENANT_ID INTEGER DEFAULT 0,
  PRIMARY KEY (ID),
  CONSTRAINT fk_DM_DEVICE_GROUP_MAP_DM_DEVICE2 FOREIGN KEY (DEVICE_ID)
  REFERENCES DM_DEVICE (ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_DM_DEVICE_GROUP_MAP_DM_GROUP2 FOREIGN KEY (GROUP_ID)
  REFERENCES DM_GROUP (ID) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS DM_OPERATION (
    ID INTEGER AUTO_INCREMENT NOT NULL,
    TYPE VARCHAR(50) NOT NULL,
    CREATED_TIMESTAMP TIMESTAMP NOT NULL,
    RECEIVED_TIMESTAMP TIMESTAMP NULL,
    OPERATION_CODE VARCHAR(1000) NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS DM_CONFIG_OPERATION (
    OPERATION_ID INTEGER NOT NULL,
    OPERATION_CONFIG  BLOB DEFAULT NULL,
    ENABLED BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (OPERATION_ID),
    CONSTRAINT fk_dm_operation_config FOREIGN KEY (OPERATION_ID) REFERENCES
    DM_OPERATION (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS DM_COMMAND_OPERATION (
    OPERATION_ID INTEGER NOT NULL,
    ENABLED BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (OPERATION_ID),
    CONSTRAINT fk_dm_operation_command FOREIGN KEY (OPERATION_ID) REFERENCES
    DM_OPERATION (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS DM_POLICY_OPERATION (
    OPERATION_ID INTEGER NOT NULL,
    ENABLED INTEGER NOT NULL DEFAULT 0,
    OPERATION_DETAILS BLOB DEFAULT NULL,
    PRIMARY KEY (OPERATION_ID),
    CONSTRAINT fk_dm_operation_policy FOREIGN KEY (OPERATION_ID) REFERENCES
    DM_OPERATION (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS DM_PROFILE_OPERATION (
    OPERATION_ID INTEGER NOT NULL,
    ENABLED INTEGER NOT NULL DEFAULT 0,
    OPERATION_DETAILS BLOB DEFAULT NULL,
    PRIMARY KEY (OPERATION_ID),
    CONSTRAINT fk_dm_operation_profile FOREIGN KEY (OPERATION_ID) REFERENCES
    DM_OPERATION (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS DM_ENROLMENT (
    ID INTEGER AUTO_INCREMENT NOT NULL,
    DEVICE_ID INTEGER NOT NULL,
    OWNER VARCHAR(50) NOT NULL,
    OWNERSHIP VARCHAR(45) DEFAULT NULL,
    STATUS VARCHAR(50) NULL,
    DATE_OF_ENROLMENT TIMESTAMP DEFAULT NULL,
    DATE_OF_LAST_UPDATE TIMESTAMP DEFAULT NULL,
    TENANT_ID INT NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT fk_dm_device_enrolment FOREIGN KEY (DEVICE_ID) REFERENCES
    DM_DEVICE (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT uk_dm_device_enrolment UNIQUE (DEVICE_ID, OWNER, OWNERSHIP, TENANT_ID)
);

CREATE TABLE IF NOT EXISTS DM_ENROLMENT_OP_MAPPING (
    ID INTEGER AUTO_INCREMENT NOT NULL,
    ENROLMENT_ID INTEGER NOT NULL,
    OPERATION_ID INTEGER NOT NULL,
    STATUS VARCHAR(50) NULL,
    PUSH_NOTIFICATION_STATUS VARCHAR(50) NULL,
    CREATED_TIMESTAMP INT NOT NULL,
    UPDATED_TIMESTAMP INT NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT fk_dm_device_operation_mapping_device FOREIGN KEY (ENROLMENT_ID) REFERENCES
    DM_ENROLMENT (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_dm_device_operation_mapping_operation FOREIGN KEY (OPERATION_ID) REFERENCES
    DM_OPERATION (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS DM_DEVICE_OPERATION_RESPONSE (
    ID INTEGER AUTO_INCREMENT NOT NULL,
    ENROLMENT_ID INTEGER NOT NULL,
    OPERATION_ID INTEGER NOT NULL,
    EN_OP_MAP_ID  INTEGER NOT NULL,
    OPERATION_RESPONSE LONGBLOB DEFAULT NULL,
    RECEIVED_TIMESTAMP TIMESTAMP NULL,
    PRIMARY KEY (ID),
    CONSTRAINT fk_dm_device_operation_response_enrollment FOREIGN KEY (ENROLMENT_ID) REFERENCES
    DM_ENROLMENT (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_dm_device_operation_response_operation FOREIGN KEY (OPERATION_ID) REFERENCES
    DM_OPERATION (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT  fk_dm_en_op_map_response FOREIGN KEY (EN_OP_MAP_ID) REFERENCES
    DM_ENROLMENT_OP_MAPPING (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS DEVICE_ORGANIZATION_MAP (
    DEVICE_ID VARCHAR(32) NOT NULL PRIMARY KEY ,
    DEVICE_NAME VARCHAR(20) NOT NULL,
    DEVICE_PARENT VARCHAR(256) NOT NULL,
    MINUTES_SINCE_LAST_PING INTEGER NOT NULL,
    STATE INTEGER NOT NULL DEFAULT 0,
    IS_GATEWAY INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS IMAGES (
  ID VARCHAR(32) DEFAULT NULL,
  SIZE INT(11) DEFAULT NULL,
  IMAGE LONGBLOB
);

-- POLICY RELATED TABLES --

CREATE  TABLE IF NOT EXISTS DM_PROFILE (
  ID INT NOT NULL AUTO_INCREMENT ,
  PROFILE_NAME VARCHAR(45) NOT NULL ,
  TENANT_ID INT NOT NULL ,
  DEVICE_TYPE VARCHAR(300) NOT NULL ,
  CREATED_TIME DATETIME NOT NULL ,
  UPDATED_TIME DATETIME NOT NULL ,
  PRIMARY KEY (ID)
);

CREATE  TABLE IF NOT EXISTS DM_POLICY (
  ID INT(11) NOT NULL AUTO_INCREMENT ,
  NAME VARCHAR(45) DEFAULT NULL ,
  DESCRIPTION VARCHAR(1000) NULL,
  TENANT_ID INT(11) NOT NULL ,
  PROFILE_ID INT(11) NOT NULL ,
  OWNERSHIP_TYPE VARCHAR(45) NULL,
  COMPLIANCE VARCHAR(100) NULL,
  PRIORITY INT NOT NULL,
  ACTIVE INT(2) NOT NULL,
  UPDATED INT(1) NULL,
  PRIMARY KEY (ID) ,
  CONSTRAINT FK_DM_PROFILE_DM_POLICY
    FOREIGN KEY (PROFILE_ID )
    REFERENCES DM_PROFILE (ID )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE  TABLE IF NOT EXISTS DM_DEVICE_POLICY (
  ID INT(11) NOT NULL AUTO_INCREMENT ,
  DEVICE_ID INT(11) NOT NULL ,
  ENROLMENT_ID INT(11) NOT NULL,
  DEVICE BLOB NOT NULL,
  POLICY_ID INT(11) NOT NULL ,
  PRIMARY KEY (ID) ,
  CONSTRAINT FK_POLICY_DEVICE_POLICY
    FOREIGN KEY (POLICY_ID )
    REFERENCES DM_POLICY (ID )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT FK_DEVICE_DEVICE_POLICY
    FOREIGN KEY (DEVICE_ID )
    REFERENCES DM_DEVICE (ID )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE  TABLE IF NOT EXISTS DM_DEVICE_TYPE_POLICY (
  ID INT(11) NOT NULL ,
  DEVICE_TYPE VARCHAR(300) NOT NULL ,
  POLICY_ID INT(11) NOT NULL ,
  PRIMARY KEY (ID) ,
  CONSTRAINT FK_DEVICE_TYPE_POLICY
    FOREIGN KEY (POLICY_ID )
    REFERENCES DM_POLICY (ID )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE  TABLE IF NOT EXISTS DM_PROFILE_FEATURES (
  ID INT(11) NOT NULL AUTO_INCREMENT,
  PROFILE_ID INT(11) NOT NULL,
  FEATURE_CODE VARCHAR(100) NOT NULL,
  DEVICE_TYPE VARCHAR(300) NOT NULL,
  TENANT_ID INT(11) NOT NULL ,
  CONTENT BLOB NULL DEFAULT NULL,
  PRIMARY KEY (ID),
  CONSTRAINT FK_DM_PROFILE_DM_POLICY_FEATURES
    FOREIGN KEY (PROFILE_ID)
    REFERENCES DM_PROFILE (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE  TABLE IF NOT EXISTS DM_ROLE_POLICY (
  ID INT(11) NOT NULL AUTO_INCREMENT ,
  ROLE_NAME VARCHAR(45) NOT NULL ,
  POLICY_ID INT(11) NOT NULL ,
  PRIMARY KEY (ID) ,
  CONSTRAINT FK_ROLE_POLICY_POLICY
    FOREIGN KEY (POLICY_ID )
    REFERENCES DM_POLICY (ID )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE  TABLE IF NOT EXISTS DM_USER_POLICY (
  ID INT NOT NULL AUTO_INCREMENT ,
  POLICY_ID INT NOT NULL ,
  USERNAME VARCHAR(45) NOT NULL ,
  PRIMARY KEY (ID) ,
  CONSTRAINT DM_POLICY_USER_POLICY
    FOREIGN KEY (POLICY_ID )
    REFERENCES DM_POLICY (ID )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE  TABLE IF NOT EXISTS DM_DEVICE_POLICY_APPLIED (
  ID INT NOT NULL AUTO_INCREMENT ,
  DEVICE_ID INT NOT NULL ,
  ENROLMENT_ID INT(11) NOT NULL,
  POLICY_ID INT NOT NULL ,
  POLICY_CONTENT BLOB NULL ,
  TENANT_ID INT NOT NULL,
  APPLIED TINYINT(1) NULL ,
  CREATED_TIME TIMESTAMP NULL ,
  UPDATED_TIME TIMESTAMP NULL ,
  APPLIED_TIME TIMESTAMP NULL ,
  PRIMARY KEY (ID) ,
  CONSTRAINT FK_DM_POLICY_DEVCIE_APPLIED
    FOREIGN KEY (DEVICE_ID )
    REFERENCES DM_DEVICE (ID )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS DM_CRITERIA (
  ID INT NOT NULL AUTO_INCREMENT,
  TENANT_ID INT NOT NULL,
  NAME VARCHAR(50) NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS DM_POLICY_CRITERIA (
  ID INT NOT NULL AUTO_INCREMENT,
  CRITERIA_ID INT NOT NULL,
  POLICY_ID INT NOT NULL,
  PRIMARY KEY (ID),
  CONSTRAINT FK_CRITERIA_POLICY_CRITERIA
    FOREIGN KEY (CRITERIA_ID)
    REFERENCES DM_CRITERIA (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT FK_POLICY_POLICY_CRITERIA
    FOREIGN KEY (POLICY_ID)
    REFERENCES DM_POLICY (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS DM_POLICY_CRITERIA_PROPERTIES (
  ID INT NOT NULL AUTO_INCREMENT,
  POLICY_CRITERION_ID INT NOT NULL,
  PROP_KEY VARCHAR(45) NULL,
  PROP_VALUE VARCHAR(100) NULL,
  CONTENT BLOB NULL COMMENT 'This is used to ',
  PRIMARY KEY (ID),
  CONSTRAINT FK_POLICY_CRITERIA_PROPERTIES
    FOREIGN KEY (POLICY_CRITERION_ID)
    REFERENCES DM_POLICY_CRITERIA (ID)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS DM_POLICY_COMPLIANCE_STATUS (
  ID INT NOT NULL AUTO_INCREMENT,
  DEVICE_ID INT NOT NULL,
  ENROLMENT_ID INT(11) NOT NULL,
  POLICY_ID INT NOT NULL,
  TENANT_ID INT NOT NULL,
  STATUS INT NULL,
  LAST_SUCCESS_TIME TIMESTAMP NULL,
  LAST_REQUESTED_TIME TIMESTAMP NULL,
  LAST_FAILED_TIME TIMESTAMP NULL,
  ATTEMPTS INT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS DM_POLICY_CHANGE_MGT (
  ID INT NOT NULL AUTO_INCREMENT,
  POLICY_ID INT NOT NULL,
  DEVICE_TYPE VARCHAR(300) NOT NULL ,
  TENANT_ID INT(11) NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS DM_POLICY_COMPLIANCE_FEATURES (
  ID INT NOT NULL AUTO_INCREMENT,
  COMPLIANCE_STATUS_ID INT NOT NULL,
  TENANT_ID INT NOT NULL,
  FEATURE_CODE VARCHAR(100) NOT NULL,
  STATUS INT NULL,
  PRIMARY KEY (ID),
  CONSTRAINT FK_COMPLIANCE_FEATURES_STATUS
    FOREIGN KEY (COMPLIANCE_STATUS_ID)
    REFERENCES DM_POLICY_COMPLIANCE_STATUS (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS DM_APPLICATION (
    ID INTEGER AUTO_INCREMENT NOT NULL,
    NAME VARCHAR(150) NOT NULL,
    APP_IDENTIFIER VARCHAR(150) NOT NULL,
    PLATFORM VARCHAR(50) DEFAULT NULL,
    CATEGORY VARCHAR(50) NULL,
    VERSION VARCHAR(50) NULL,
    TYPE VARCHAR(50) NULL,
    LOCATION_URL VARCHAR(100) DEFAULT NULL,
    IMAGE_URL VARCHAR(100) DEFAULT NULL,
    APP_PROPERTIES BLOB NULL,
    MEMORY_USAGE INTEGER(10) NULL,
    IS_ACTIVE BOOLEAN NOT NULL DEFAULT FALSE,
    TENANT_ID INTEGER NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS DM_DEVICE_APPLICATION_MAPPING (
    ID INTEGER AUTO_INCREMENT NOT NULL,
    DEVICE_ID INTEGER NOT NULL,
    APPLICATION_ID INTEGER NOT NULL,
    TENANT_ID INTEGER NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT fk_dm_device FOREIGN KEY (DEVICE_ID) REFERENCES
    DM_DEVICE (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_dm_application FOREIGN KEY (APPLICATION_ID) REFERENCES
    DM_APPLICATION (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- POLICY RELATED TABLES  FINISHED --

-- NOTIFICATION TABLE --
CREATE TABLE IF NOT EXISTS DM_NOTIFICATION (
    NOTIFICATION_ID INTEGER AUTO_INCREMENT NOT NULL,
    DEVICE_ID INTEGER NOT NULL,
    OPERATION_ID INTEGER NOT NULL,
    TENANT_ID INTEGER NOT NULL,
    STATUS VARCHAR(10) NULL,
    DESCRIPTION VARCHAR(1000) NULL,
    PRIMARY KEY (NOTIFICATION_ID),
    CONSTRAINT fk_dm_device_notification FOREIGN KEY (DEVICE_ID) REFERENCES
    DM_DEVICE (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_dm_operation_notification FOREIGN KEY (OPERATION_ID) REFERENCES
    DM_OPERATION (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
);
-- NOTIFICATION TABLE END --

CREATE TABLE IF NOT EXISTS DM_DEVICE_INFO (
  ID INTEGER AUTO_INCREMENT NOT NULL,
  DEVICE_ID INT NULL,
  KEY_FIELD VARCHAR(45) NULL,
  VALUE_FIELD VARCHAR(100) NULL,
  PRIMARY KEY (ID),
  CONSTRAINT DM_DEVICE_INFO_DEVICE
    FOREIGN KEY (DEVICE_ID)
    REFERENCES DM_DEVICE (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS DM_DEVICE_LOCATION (
  ID INTEGER AUTO_INCREMENT NOT NULL,
  DEVICE_ID INT NULL,
  LATITUDE DOUBLE NULL,
  LONGITUDE DOUBLE NULL,
  STREET1 VARCHAR(255) NULL,
  STREET2 VARCHAR(45) NULL,
  CITY VARCHAR(45) NULL,
  ZIP VARCHAR(10) NULL,
  STATE VARCHAR(45) NULL,
  COUNTRY VARCHAR(45) NULL,
  GEO_HASH VARCHAR(45) NULL,
  UPDATE_TIMESTAMP BIGINT(15) NOT NULL,
  PRIMARY KEY (ID),
  CONSTRAINT DM_DEVICE_LOCATION_DEVICE
    FOREIGN KEY (DEVICE_ID)
    REFERENCES DM_DEVICE (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);
CREATE INDEX DM_DEVICE_LOCATION_GEO_hashx ON DM_DEVICE_LOCATION(GEO_HASH ASC);

CREATE TABLE IF NOT EXISTS DM_DEVICE_DETAIL (
  ID INT NOT NULL AUTO_INCREMENT,
  DEVICE_ID INT NOT NULL,
  DEVICE_MODEL VARCHAR(45) NULL,
  VENDOR VARCHAR(45) NULL,
  OS_VERSION VARCHAR(45) NULL,
  OS_BUILD_DATE VARCHAR(100) NULL,
  BATTERY_LEVEL DECIMAL(4) NULL,
  INTERNAL_TOTAL_MEMORY DECIMAL(30,3) NULL,
  INTERNAL_AVAILABLE_MEMORY DECIMAL(30,3) NULL,
  EXTERNAL_TOTAL_MEMORY DECIMAL(30,3) NULL,
  EXTERNAL_AVAILABLE_MEMORY DECIMAL(30,3) NULL,
  CONNECTION_TYPE VARCHAR(50) NULL,
  SSID VARCHAR(45) NULL,
  CPU_USAGE DECIMAL(5) NULL,
  TOTAL_RAM_MEMORY DECIMAL(30,3) NULL,
  AVAILABLE_RAM_MEMORY DECIMAL(30,3) NULL,
  PLUGGED_IN INT(1) NULL,
  UPDATE_TIMESTAMP BIGINT(15) NOT NULL,
  PRIMARY KEY (ID),
  CONSTRAINT FK_DM_DEVICE_DETAILS_DEVICE
    FOREIGN KEY (DEVICE_ID)
    REFERENCES DM_DEVICE (ID)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

-- POLICY AND DEVICE GROUP MAPPING --
CREATE TABLE IF NOT EXISTS DM_DEVICE_GROUP_POLICY (
  ID INT NOT NULL AUTO_INCREMENT,
  DEVICE_GROUP_ID INT NOT NULL,
  POLICY_ID INT NOT NULL,
  TENANT_ID INT NOT NULL,
  PRIMARY KEY (ID),
  CONSTRAINT FK_DM_DEVICE_GROUP_POLICY
    FOREIGN KEY (DEVICE_GROUP_ID)
    REFERENCES DM_GROUP (ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE ,
  CONSTRAINT FK_DM_DEVICE_GROUP_DM_POLICY
    FOREIGN KEY (POLICY_ID)
    REFERENCES DM_POLICY (ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
-- END OF POLICY AND DEVICE GROUP MAPPING --

-- DASHBOARD RELATED VIEWS --
CREATE VIEW POLICY_COMPLIANCE_INFO AS
SELECT
DEVICE_INFO.DEVICE_ID,
DEVICE_INFO.DEVICE_IDENTIFICATION,
DEVICE_INFO.PLATFORM,
DEVICE_INFO.OWNERSHIP,
DEVICE_INFO.CONNECTIVITY_STATUS,
IFNULL(DEVICE_WITH_POLICY_INFO.POLICY_ID, -1) AS POLICY_ID,
IFNULL(DEVICE_WITH_POLICY_INFO.IS_COMPLIANT, -1) AS IS_COMPLIANT,
DEVICE_INFO.TENANT_ID
FROM
(SELECT
DM_DEVICE.ID AS DEVICE_ID,
DM_DEVICE.DEVICE_IDENTIFICATION,
DM_DEVICE_TYPE.NAME AS PLATFORM,
DM_ENROLMENT.OWNERSHIP,
DM_ENROLMENT.STATUS AS CONNECTIVITY_STATUS,
DM_DEVICE.TENANT_ID
FROM DM_DEVICE, DM_DEVICE_TYPE, DM_ENROLMENT
WHERE DM_DEVICE.DEVICE_TYPE_ID = DM_DEVICE_TYPE.ID AND DM_DEVICE.ID = DM_ENROLMENT.DEVICE_ID) DEVICE_INFO
LEFT JOIN
(SELECT
DEVICE_ID,
POLICY_ID,
STATUS AS IS_COMPLIANT
FROM DM_POLICY_COMPLIANCE_STATUS) DEVICE_WITH_POLICY_INFO
ON DEVICE_INFO.DEVICE_ID = DEVICE_WITH_POLICY_INFO.DEVICE_ID
ORDER BY DEVICE_INFO.DEVICE_ID;

CREATE VIEW FEATURE_NON_COMPLIANCE_INFO AS
SELECT
DM_DEVICE.ID AS DEVICE_ID,
DM_DEVICE.DEVICE_IDENTIFICATION,
DM_DEVICE_DETAIL.DEVICE_MODEL,
DM_DEVICE_DETAIL.VENDOR,
DM_DEVICE_DETAIL.OS_VERSION,
DM_ENROLMENT.OWNERSHIP,
DM_ENROLMENT.OWNER,
DM_ENROLMENT.STATUS AS CONNECTIVITY_STATUS,
DM_POLICY_COMPLIANCE_STATUS.POLICY_ID,
DM_DEVICE_TYPE.NAME AS PLATFORM,
DM_POLICY_COMPLIANCE_FEATURES.FEATURE_CODE,
DM_POLICY_COMPLIANCE_FEATURES.STATUS AS IS_COMPLAINT,
DM_DEVICE.TENANT_ID
FROM
DM_POLICY_COMPLIANCE_FEATURES, DM_POLICY_COMPLIANCE_STATUS, DM_ENROLMENT, DM_DEVICE, DM_DEVICE_TYPE, DM_DEVICE_DETAIL
WHERE
DM_POLICY_COMPLIANCE_FEATURES.COMPLIANCE_STATUS_ID = DM_POLICY_COMPLIANCE_STATUS.ID AND
DM_POLICY_COMPLIANCE_STATUS.ENROLMENT_ID = DM_ENROLMENT.ID AND
DM_POLICY_COMPLIANCE_STATUS.DEVICE_ID = DM_DEVICE.ID AND
DM_DEVICE.DEVICE_TYPE_ID = DM_DEVICE_TYPE.ID AND
DM_DEVICE.ID = DM_DEVICE_DETAIL.DEVICE_ID
ORDER BY TENANT_ID, DEVICE_ID;

-- END OF DASHBOARD RELATED VIEWS --
