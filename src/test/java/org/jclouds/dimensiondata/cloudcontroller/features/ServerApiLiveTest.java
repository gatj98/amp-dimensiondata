/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.dimensiondata.cloudcontroller.features;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.jclouds.dimensiondata.cloudcontroller.domain.Disk;
import org.jclouds.dimensiondata.cloudcontroller.domain.NIC;
import org.jclouds.dimensiondata.cloudcontroller.domain.NetworkInfo;
import org.jclouds.dimensiondata.cloudcontroller.domain.Response;
import org.jclouds.dimensiondata.cloudcontroller.domain.Server;
import org.jclouds.dimensiondata.cloudcontroller.internal.BaseDimensionDataCloudControllerApiLiveTest;
import org.jclouds.dimensiondata.cloudcontroller.utils.DimensionDataCloudControllerUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.util.List;

import static org.jclouds.dimensiondata.cloudcontroller.TestEnvironmentProperties.envProperty;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Test(groups = "live", testName = "ServerApiLiveTest", singleThreaded = true)
public class ServerApiLiveTest extends BaseDimensionDataCloudControllerApiLiveTest {

    private String serverId;

    @Test
    public void testListServers() {
        List<Server> servers = api().listServers().concat().toList();
        assertNotNull(servers);
        for (Server s : servers) {
            assertNotNull(s);
        }
    }

    @Test
    public void testDeployAndStartServer() {
        Boolean started = Boolean.TRUE;
        NetworkInfo networkInfo = NetworkInfo.create(
              envProperty("networkId"),
                NIC.builder()
                        .vlanId(envProperty("vlanId"))
                        .build(),
                Lists.<NIC> newArrayList()
        );
        List<Disk> disks = ImmutableList.of(
                Disk.builder()
                        .scsiId(0)
                        .speed("STANDARD")
                        .build()
        );
        Response response = api().deployServer(
              ServerApiLiveTest.class.getSimpleName(),
              envProperty("existingImageId"),
              started,
              networkInfo,
              disks,
              "P$$ssWwrrdGoDd!");
        assertNotNull(response);
        serverId = DimensionDataCloudControllerUtils.tryFindPropertyValue(response, "serverId");
        assertNotNull(serverId);
        DimensionDataCloudControllerUtils.waitForServerStatus(api(), serverId, true, true, 30 * 60 * 1000, "Error");
    }

    @Test(dependsOnMethods = "testDeployAndStartServer")
    public void testPowerOffServer() {
        Response response = api().powerOffServer(serverId);
        DimensionDataCloudControllerUtils.waitForServerStatus(api(), serverId, false, true, 30 * 60 * 1000, "Error");
        assertFalse(api().getServer(serverId).started());
        assertTrue(response.error().isEmpty());
    }

    @AfterTest
    public void testDeleteServer() {
        if (serverId != null) {
            Response response = api().deleteServer(serverId);
            assertTrue(response.error().isEmpty());
        }
    }

    private ServerApi api() {
        return api.getServerApi();
    }

}
