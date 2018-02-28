/*
 *   Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.device.mgt.jaxrs.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.device.mgt.jaxrs.service.api.DeviceHierarchyService;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public class DeviceHierarchyServiceImpl implements DeviceHierarchyService {
    private static final Log log = LogFactory.getLog(DeviceHierarchyServiceImpl.class);

    @GET
    @Path("/")
    @Override
    public Response getHierarchy() {
        return null;
    }

    @GET
    @Path("/graph")
    @Override
    public Response getHierarchyGraph() {
        return null;
    }

    @GET
    @Path("/device/{deviceId}/parent")
    @Override
    public Response getDeviceParentInHierarchy(String deviceId) {
        if (deviceId.isEmpty()) {
            String errorMessage = "The parameter of the device organization ID is empty.";
            return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
        }

        return null;
    }

    @GET
    @Path("/device/{deviceId}/children")
    @Override
    public Response getDeviceChildrenInHierarchy(String deviceId) {
        if (deviceId.isEmpty()) {
            String errorMessage = "The parameter of the device organization ID is empty.";
            return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
        }
        return null;
    }

    @PUT
    @Path("/device/{deviceId}/parent")
    @Override
    public Response setDeviceParentInHierarchy(String deviceId) {
        if (deviceId.isEmpty()) {
            String errorMessage = "The parameter of the device organization ID is empty.";
            return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
        }

        return null;
    }
}
