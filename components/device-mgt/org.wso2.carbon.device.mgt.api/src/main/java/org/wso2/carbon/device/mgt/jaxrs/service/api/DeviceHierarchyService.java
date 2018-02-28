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
package org.wso2.carbon.device.mgt.jaxrs.service.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;
import io.swagger.annotations.Info;
import io.swagger.annotations.ResponseHeader;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.wso2.carbon.apimgt.annotations.api.Scope;
import org.wso2.carbon.apimgt.annotations.api.Scopes;
import org.wso2.carbon.device.mgt.common.operation.mgt.Operation;
import org.wso2.carbon.device.mgt.jaxrs.beans.ErrorResponse;
import org.wso2.carbon.device.mgt.jaxrs.util.Constants;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@SwaggerDefinition(
        info = @Info(
                version = "1.0.0",
                title = "",
                extensions = {
                        @Extension(properties = {
                                @ExtensionProperty(name = "name", value = "DeviceHierarchy"),
                                @ExtensionProperty(name = "context", value = "/api/device-mgt/v1.0/device-hierarchy"),
                        })
                }
        ),
        tags = {
                @Tag(name = "device_hierarchy, device_management", description = "Device hierarchy related " +
                        "REST-APIs. Can be used to manipulate hierarchy related data")
        }
)
@Scopes(
        scopes = {
                @Scope(
                        name = "Get Device hierarchy arrangement",
                        description = "Get device hierarchy arrangement",
                        key = "perm:device-organization:get-hierarchy",
                        permissions = {"/device-mgt/device-organization/get_hierarchy"}
                ),
                @Scope(
                        name = "Getting Device hierarchy visualization data",
                        description = "Getting Device hierarchy visualization data",
                        key = "perm:device-organization:graph",
                        permissions = {"/device-mgt/device-organization/graph"}
                ),
                @Scope(
                        name = "Getting parent of device in hierarchy",
                        description = "Getting parent of device in hierarchy",
                        key = "perm:device-organization:parent",
                        permissions = {"/device-mgt/device-organization/parent"}
                ),
                @Scope(
                        name = "Getting children of device in hierarchy",
                        description = "Getting children of device in hierarchy",
                        key = "perm:device-organization:children",
                        permissions = {"/device-mgt/device-organization/children"}
                ),
                @Scope(
                        name = "Updating parent of device in hierarchy",
                        description = "Updating parent of device in hierarchy",
                        key = "perm:device-organization:update",
                        permissions = {"/device-mgt/device-organization/update"}
                )
        }
)
@Path("/device-hierarchy")
@Api(value = "Device Hierarchy", description = "This API carries all device hierarchy related " +
        "operations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface DeviceHierarchyService {

    @GET
    @Path("/")
    @ApiOperation(
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "GET",
            value = "Get device arrangement hierarchy",
            notes = "Get the hierarchical arrangement of devices",
            tags = "Device Hierarchy",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = Constants.SCOPE, value = "perm:device-organization:get-hierarchy")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "OK. \n Successfully retrieved hierarchy.",
                            response = Operation.class,
                            responseHeaders = {
                                    @ResponseHeader(
                                            name = "Content-Type",
                                            description = "The content type of the body"),
                                    @ResponseHeader(
                                            name = "ETag",
                                            description = "Entity Tag of the response resource.\n" +
                                                    "Used by caches, or in conditional requests."),
                                    @ResponseHeader(
                                            name = "Last-Modified",
                                            description = "Date and time the resource has been modified the last time.\n" +
                                                    "Used by caches, or in conditional requests."),
                            }),
                    @ApiResponse(
                            code = 304,
                            message = "Not Modified. Empty body because the client already has the latest " +
                                    "version of the requested resource."),
                    @ApiResponse(
                            code = 400,
                            message = "Bad Request. \n Invalid request or validation error.",
                            response = ErrorResponse.class),
                    @ApiResponse(
                            code = 404,
                            message = "Not Found. \n No device is found under the provided id.",
                            response = ErrorResponse.class),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error. \n " +
                                    "Server error occurred while retrieving information requested device.",
                            response = ErrorResponse.class)
            }
    )
    Response getHierarchy();

    @GET
    @Path("/graph")
    @ApiOperation(
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "GET",
            value = "Get device arrangement hierarchy as graph",
            notes = "Get device arrangement hierarchy as graph",
            tags = "Device Hierarchy",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = Constants.SCOPE, value = "perm:device-organization:graph")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "OK. \n Successfully retrieved hierarchy graph visualization.",
                            response = Operation.class,
                            responseHeaders = {
                                    @ResponseHeader(
                                            name = "Content-Type",
                                            description = "The content type of the body"),
                                    @ResponseHeader(
                                            name = "ETag",
                                            description = "Entity Tag of the response resource.\n" +
                                                    "Used by caches, or in conditional requests."),
                                    @ResponseHeader(
                                            name = "Last-Modified",
                                            description = "Date and time the resource has been modified the last time.\n" +
                                                    "Used by caches, or in conditional requests."),
                            }),
                    @ApiResponse(
                            code = 304,
                            message = "Not Modified. Empty body because the client already has the latest " +
                                    "version of the requested resource."),
                    @ApiResponse(
                            code = 400,
                            message = "Bad Request. \n Invalid request or validation error.",
                            response = ErrorResponse.class),
                    @ApiResponse(
                            code = 404,
                            message = "Not Found. \n No device is found under the provided id.",
                            response = ErrorResponse.class),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error. \n " +
                                    "Server error occurred while retrieving information requested device.",
                            response = ErrorResponse.class)
            }
    )
    Response getHierarchyGraph();

    @GET
    @Path("/device/{deviceId}/parent")
    @ApiOperation(
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "GET",
            value = "Get parent of a device in hierarchy",
            notes = "Get parent of a device in hierarchy",
            tags = "Device Hierarchy",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = Constants.SCOPE, value = "perm:device-organization:parent")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "OK. \n Successfully retrieved parent of device in hierarchy.",
                            response = Operation.class,
                            responseHeaders = {
                                    @ResponseHeader(
                                            name = "Content-Type",
                                            description = "The content type of the body"),
                                    @ResponseHeader(
                                            name = "ETag",
                                            description = "Entity Tag of the response resource.\n" +
                                                    "Used by caches, or in conditional requests."),
                                    @ResponseHeader(
                                            name = "Last-Modified",
                                            description = "Date and time the resource has been modified the last time.\n" +
                                                    "Used by caches, or in conditional requests."),
                            }),
                    @ApiResponse(
                            code = 304,
                            message = "Not Modified. Empty body because the client already has the latest " +
                                    "version of the requested resource."),
                    @ApiResponse(
                            code = 400,
                            message = "Bad Request. \n Invalid request or validation error.",
                            response = ErrorResponse.class),
                    @ApiResponse(
                            code = 404,
                            message = "Not Found. \n No device is found under the provided id.",
                            response = ErrorResponse.class),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error. \n " +
                                    "Server error occurred while retrieving information requested device.",
                            response = ErrorResponse.class)
            }
    )
    Response getDeviceParentInHierarchy(@ApiParam(name = "deviceId", value = "Unique device identifier",
            required = true) @PathParam("deviceId") String deviceId);

    @GET
    @Path("/device/{deviceId}/children")
    @ApiOperation(
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "GET",
            value = "Get device children in the hierarchy",
            notes = "Get device children in the hierarchy",
            tags = "Device Hierarchy",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = Constants.SCOPE, value = "perm:device-organization:children")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "OK. \n Successfully retrieved children of device in hierarchy.",
                            response = Operation.class,
                            responseHeaders = {
                                    @ResponseHeader(
                                            name = "Content-Type",
                                            description = "The content type of the body"),
                                    @ResponseHeader(
                                            name = "ETag",
                                            description = "Entity Tag of the response resource.\n" +
                                                    "Used by caches, or in conditional requests."),
                                    @ResponseHeader(
                                            name = "Last-Modified",
                                            description = "Date and time the resource has been modified the last time.\n" +
                                                    "Used by caches, or in conditional requests."),
                            }),
                    @ApiResponse(
                            code = 304,
                            message = "Not Modified. Empty body because the client already has the latest " +
                                    "version of the requested resource."),
                    @ApiResponse(
                            code = 400,
                            message = "Bad Request. \n Invalid request or validation error.",
                            response = ErrorResponse.class),
                    @ApiResponse(
                            code = 404,
                            message = "Not Found. \n No device is found under the provided id.",
                            response = ErrorResponse.class),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error. \n " +
                                    "Server error occurred while retrieving information requested device.",
                            response = ErrorResponse.class)
            }
    )
    Response getDeviceChildrenInHierarchy(@ApiParam(name = "deviceId", value = "Unique device identifier",
            required = true) @PathParam("deviceId") String deviceId);

    @PUT
    @Path("/device/{deviceId}/parent")
    @ApiOperation(
            produces = MediaType.APPLICATION_JSON,
            httpMethod = "PUT",
            value = "Update device children in the hierarchy",
            notes = "Update device children in the hierarchy",
            tags = "Device Hierarchy",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = Constants.SCOPE, value = "perm:device-organization:update")
                    })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "OK. \n Successfully updated parent of device",
                            response = Operation.class,
                            responseHeaders = {
                                    @ResponseHeader(
                                            name = "Content-Type",
                                            description = "The content type of the body"),
                                    @ResponseHeader(
                                            name = "ETag",
                                            description = "Entity Tag of the response resource.\n" +
                                                    "Used by caches, or in conditional requests."),
                                    @ResponseHeader(
                                            name = "Last-Modified",
                                            description = "Date and time the resource has been modified the last time.\n" +
                                                    "Used by caches, or in conditional requests."),
                            }),
                    @ApiResponse(
                            code = 304,
                            message = "Not Modified. Empty body because the client already has the latest " +
                                    "version of the requested resource."),
                    @ApiResponse(
                            code = 400,
                            message = "Bad Request. \n Invalid request or validation error.",
                            response = ErrorResponse.class),
                    @ApiResponse(
                            code = 404,
                            message = "Not Found. \n No device is found under the provided id.",
                            response = ErrorResponse.class),
                    @ApiResponse(
                            code = 500,
                            message = "Internal Server Error. \n " +
                                    "Server error occurred while retrieving information requested device.",
                            response = ErrorResponse.class)
            }
    )
    Response setDeviceParentInHierarchy(@ApiParam(name = "deviceId", value = "Unique device identifier",
            required = true) @PathParam("deviceId") String deviceId);

}
