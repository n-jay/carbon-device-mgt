/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.device.mgt.jaxrs.common;

import java.util.Arrays;
import java.util.List;

public class ActivityIdList {

    private String ids;
    private List<String> idList;

    public ActivityIdList(String ids) {
        this.ids = ids;
        if (ids != null) {
            String[] splits = ids.split(",");
            if (splits.length > 0 && splits.length < 11 && splits[0] != null && !splits[0].isEmpty()) {
                idList = Arrays.asList(splits);
            }
        }
    }

    public List<String> getIdList() {
        return idList;
    }

    public String getIds() {
        return ids;
    }
}