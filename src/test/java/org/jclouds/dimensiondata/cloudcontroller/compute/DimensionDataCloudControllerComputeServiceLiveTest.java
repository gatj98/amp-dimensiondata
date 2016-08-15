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
package org.jclouds.dimensiondata.cloudcontroller.compute;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.TemplateBuilder;
import org.jclouds.compute.internal.BaseComputeServiceLiveTest;
import org.jclouds.sshj.config.SshjSshClientModule;
import org.testng.annotations.Test;

import static org.jclouds.dimensiondata.cloudcontroller.TestEnvironmentProperties.envProperty;

/**
 * Live tests for the {@link org.jclouds.compute.ComputeService} integration.
 */
@Test(groups = "live", singleThreaded = true, testName = "DimensionDataCloudControllerComputeServiceLiveTest")
public class DimensionDataCloudControllerComputeServiceLiveTest extends BaseComputeServiceLiveTest {

   public DimensionDataCloudControllerComputeServiceLiveTest() {
      provider = "dimensiondata-cloudcontroller";
   }

   @Override
   protected Module getSshModule() {
      return new SshjSshClientModule();
   }

   @Override
   public void testOptionToNotBlock() throws Exception {
      // DimensionData ComputeService implementation has to block until the node
      // is provisioned, to be able to return it.
   }

   @Override
   protected void checkTagsInNodeEquals(NodeMetadata node, ImmutableSet<String> tags) {
      // TODO implement (DimensionData does support tags)
   }

   @Override
   protected void checkUserMetadataContains(NodeMetadata node, ImmutableMap<String, String> userMetadata) {
      // TODO implement using tags
   }

   @Override
   protected Template buildTemplate(TemplateBuilder templateBuilder) {
      return super.buildTemplate(templateBuilder.locationId(envProperty("datacenter")));
   }
}
