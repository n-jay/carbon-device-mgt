/*
 *   Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.device.mgt.extensions.device.type.template;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.device.mgt.common.DeviceManagementException;
import org.wso2.carbon.device.mgt.common.DeviceManager;
import org.wso2.carbon.device.mgt.common.InitialOperationConfig;
import org.wso2.carbon.device.mgt.common.MonitoringOperation;
import org.wso2.carbon.device.mgt.common.OperationMonitoringTaskConfig;
import org.wso2.carbon.device.mgt.common.DeviceStatusTaskPluginConfig;
import org.wso2.carbon.device.mgt.common.ProvisioningConfig;
import org.wso2.carbon.device.mgt.common.app.mgt.ApplicationManager;
import org.wso2.carbon.device.mgt.common.configuration.mgt.ConfigurationEntry;
import org.wso2.carbon.device.mgt.common.configuration.mgt.PlatformConfiguration;
import org.wso2.carbon.device.mgt.common.general.GeneralConfig;
import org.wso2.carbon.device.mgt.common.policy.mgt.PolicyMonitoringManager;
import org.wso2.carbon.device.mgt.common.pull.notification.PullNotificationSubscriber;
import org.wso2.carbon.device.mgt.common.push.notification.PushNotificationConfig;
import org.wso2.carbon.device.mgt.common.spi.DeviceManagementService;
import org.wso2.carbon.device.mgt.extensions.device.type.template.config.ConfigProperties;
import org.wso2.carbon.device.mgt.extensions.device.type.template.config.DeviceStatusTaskConfiguration;
import org.wso2.carbon.device.mgt.extensions.device.type.template.config.DeviceTypeConfiguration;
import org.wso2.carbon.device.mgt.extensions.device.type.template.config.Feature;
import org.wso2.carbon.device.mgt.extensions.device.type.template.config.PolicyMonitoring;
import org.wso2.carbon.device.mgt.extensions.device.type.template.config.Property;
import org.wso2.carbon.device.mgt.extensions.device.type.template.config.PullNotificationSubscriberConfig;
import org.wso2.carbon.device.mgt.extensions.device.type.template.config.PushNotificationProvider;
import org.wso2.carbon.device.mgt.extensions.device.type.template.config.TaskConfiguration;
import org.wso2.carbon.device.mgt.extensions.device.type.template.policy.mgt.DefaultPolicyMonitoringManager;
import org.wso2.carbon.device.mgt.extensions.device.type.template.pull.notification.PullNotificationSubscriberLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the template for device type manager service. This will create and instance of device management service
 * through the configuration file.
 */
public class DeviceTypeManagerService implements DeviceManagementService {

    private static final Log log = LogFactory.getLog(DeviceTypeManagerService.class);

    private DeviceManager deviceManager;
    private PushNotificationConfig pushNotificationConfig;
    private ProvisioningConfig provisioningConfig;
    private String type;
    private OperationMonitoringTaskConfig operationMonitoringConfigs;
    private List<MonitoringOperation> monitoringOperations;
    private PolicyMonitoringManager policyMonitoringManager;
    private InitialOperationConfig initialOperationConfig;
    private PullNotificationSubscriber pullNotificationSubscriber;
    private DeviceStatusTaskPluginConfig deviceStatusTaskPluginConfig;
    private GeneralConfig generalConfig;

    public DeviceTypeManagerService(DeviceTypeConfigIdentifier deviceTypeConfigIdentifier,
                                    DeviceTypeConfiguration deviceTypeConfiguration) {
        this.setProvisioningConfig(deviceTypeConfigIdentifier.getTenantDomain(), deviceTypeConfiguration);
        this.deviceManager = new DeviceTypeManager(deviceTypeConfigIdentifier, deviceTypeConfiguration);
        this.setType(deviceTypeConfiguration.getName());
        this.populatePushNotificationConfig(deviceTypeConfiguration.getPushNotificationProvider());
        this.operationMonitoringConfigs = new OperationMonitoringTaskConfig();
        this.setOperationMonitoringConfig(deviceTypeConfiguration);
        this.initialOperationConfig = new InitialOperationConfig();
        this.setInitialOperationConfig(deviceTypeConfiguration);
        this.deviceStatusTaskPluginConfig = new DeviceStatusTaskPluginConfig();
        this.setDeviceStatusTaskPluginConfig(deviceTypeConfiguration.getDeviceStatusTaskConfiguration());
        this.setPolicyMonitoringManager(deviceTypeConfiguration.getPolicyMonitoring());
        this.setPullNotificationSubscriber(deviceTypeConfiguration.getPullNotificationSubscriberConfig());
        this.setGeneralConfig(deviceTypeConfiguration);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public OperationMonitoringTaskConfig getOperationMonitoringConfig() {
        return operationMonitoringConfigs;
    }

    private void setOperationMonitoringConfig(DeviceTypeConfiguration deviceTypeConfiguration) {
        //Read the config file and take the list of operations there in the config
        TaskConfiguration taskConfiguration = deviceTypeConfiguration.getTaskConfiguration();
        if (taskConfiguration != null) {
            operationMonitoringConfigs.setEnabled(taskConfiguration.isEnabled());
            operationMonitoringConfigs.setFrequency(taskConfiguration.getFrequency());
            List<TaskConfiguration.Operation> ops = taskConfiguration.getOperations();
            if (ops != null && !ops.isEmpty()) {
                monitoringOperations = new ArrayList<>();
                for (TaskConfiguration.Operation op : ops) {
                    MonitoringOperation monitoringOperation = new MonitoringOperation();
                    monitoringOperation.setTaskName(op.getOperationName());
                    monitoringOperation.setRecurrentTimes(op.getRecurrency());
                    monitoringOperations.add(monitoringOperation);
                }
            }
            operationMonitoringConfigs.setMonitoringOperation(monitoringOperations);
        }
    }

    @Override
    public void init() throws DeviceManagementException {
    }

    private void populatePushNotificationConfig(PushNotificationProvider pushNotificationProvider) {
        if (pushNotificationProvider != null) {
            if (pushNotificationProvider.isFileBasedProperties()) {
                Map<String, String> staticProps = new HashMap<>();
                ConfigProperties configProperties = pushNotificationProvider.getConfigProperties();
                if (configProperties != null) {
                    List<Property> properties = configProperties.getProperty();
                    if (properties != null && properties.size() > 0) {
                        for (Property property : properties) {
                            staticProps.put(property.getName(), property.getValue());
                        }
                    }
                }
                pushNotificationConfig = new PushNotificationConfig(pushNotificationProvider.getType(),
                        pushNotificationProvider.isScheduled(), staticProps);
            } else {
                try {
                    PlatformConfiguration deviceTypeConfig = deviceManager.getConfiguration();
                    if (deviceTypeConfig != null) {
                        List<ConfigurationEntry> configuration = deviceTypeConfig.getConfiguration();
                        if (configuration.size() > 0) {
                            Map<String, String> properties = this.getConfigProperty(configuration);
                            pushNotificationConfig = new PushNotificationConfig(
                                    pushNotificationProvider.getType(), pushNotificationProvider.isScheduled(),
                                    properties);
                        }
                    }
                } catch (DeviceManagementException e) {
                    log.error("Unable to get the " + type + " platform configuration from registry.");
                }
            }
        }
    }

    @Override
    public DeviceManager getDeviceManager() {
        return deviceManager;
    }

    @Override
    public ApplicationManager getApplicationManager() {
        return null;
    }

    @Override
    public ProvisioningConfig getProvisioningConfig() {
        return provisioningConfig;
    }

    @Override
    public PushNotificationConfig getPushNotificationConfig() {
        return pushNotificationConfig;
    }

    @Override
    public PolicyMonitoringManager getPolicyMonitoringManager() {
        return policyMonitoringManager;
    }

    @Override
    public InitialOperationConfig getInitialOperationConfig() {
        return initialOperationConfig;
    }

    @Override
    public PullNotificationSubscriber getPullNotificationSubscriber() {
        return pullNotificationSubscriber;
    }

    public DeviceStatusTaskPluginConfig getDeviceStatusTaskPluginConfig() {
        return deviceStatusTaskPluginConfig;
    }

    @Override
    public GeneralConfig getGeneralConfig() {
        return generalConfig;
    }

    private void setProvisioningConfig(String tenantDomain, DeviceTypeConfiguration deviceTypeConfiguration) {
        if (deviceTypeConfiguration.getProvisioningConfig() != null) {
            boolean sharedWithAllTenants = deviceTypeConfiguration.getProvisioningConfig().isSharedWithAllTenants();
            provisioningConfig = new ProvisioningConfig(tenantDomain, sharedWithAllTenants);
        } else {
            provisioningConfig = new ProvisioningConfig(tenantDomain, false);
        }
    }

    private void setDeviceStatusTaskPluginConfig(DeviceStatusTaskConfiguration deviceStatusTaskConfiguration) {
        if (deviceStatusTaskConfiguration != null && deviceStatusTaskConfiguration.isEnabled()) {
            deviceStatusTaskPluginConfig.setRequireStatusMonitoring(deviceStatusTaskConfiguration.isEnabled());
            deviceStatusTaskPluginConfig.setIdleTimeToMarkInactive(deviceStatusTaskConfiguration.getIdleTimeToMarkInactive());
            deviceStatusTaskPluginConfig.setIdleTimeToMarkUnreachable(deviceStatusTaskConfiguration.getIdleTimeToMarkUnreachable());
            deviceStatusTaskPluginConfig.setFrequency(deviceStatusTaskConfiguration.getFrequency());
        }
    }

    protected void setInitialOperationConfig(DeviceTypeConfiguration deviceTypeConfiguration) {
        if (deviceTypeConfiguration.getOperations() != null) {
            List<String> ops = deviceTypeConfiguration.getOperations();
            if (!ops.isEmpty() && deviceTypeConfiguration.getFeatures() != null
                    && deviceTypeConfiguration.getFeatures().getFeature() != null) {
                List<String> validOps = new ArrayList<>();
                for (String operation : ops) {
                    boolean isFeatureExist = false;
                    for (Feature feature : deviceTypeConfiguration.getFeatures().getFeature()) {
                        if (feature.getCode().equals(operation)) {
                            isFeatureExist = true;
                            break;
                        }
                    }
                    if (isFeatureExist) {
                        validOps.add(operation);
                    } else {
                        log.warn("Couldn't fine a valid feature for the operation : " + operation);
                    }
                }
                initialOperationConfig.setOperations(validOps);
            }
        }
    }

    private void setType(String type) {
        this.type = type;
    }

    private Map<String, String> getConfigProperty(List<ConfigurationEntry> configs) {
        Map<String, String> propertMap = new HashMap<>();
        for (ConfigurationEntry entry : configs) {
            propertMap.put(entry.getName(), entry.getValue().toString());
        }
        return propertMap;
    }

    private void setPolicyMonitoringManager(PolicyMonitoring policyMonitoring) {
        if (policyMonitoring != null && policyMonitoring.isEnabled()) {
            this.policyMonitoringManager = new DefaultPolicyMonitoringManager();
        }
    }

    private void setPullNotificationSubscriber(PullNotificationSubscriberConfig pullNotificationSubscriberConfig) {
        if (pullNotificationSubscriberConfig != null) {
            String className = pullNotificationSubscriberConfig.getClassName();
            if (className != null && !className.isEmpty()) {
                PullNotificationSubscriberLoader pullNotificationSubscriberLoader = new PullNotificationSubscriberLoader
                        (className, pullNotificationSubscriberConfig.getConfigProperties());
                this.pullNotificationSubscriber = pullNotificationSubscriberLoader.getPullNotificationSubscriber();
            }
        }
    }


    public void setGeneralConfig(DeviceTypeConfiguration deviceTypeConfiguration) {
        this.generalConfig = new GeneralConfig();
        if (deviceTypeConfiguration.getPolicyMonitoring() != null) {
            this.generalConfig.setPolicyMonitoringEnabled(deviceTypeConfiguration.getPolicyMonitoring().isEnabled());
        }
    }
}
