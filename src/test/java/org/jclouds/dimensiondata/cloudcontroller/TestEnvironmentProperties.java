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
package org.jclouds.dimensiondata.cloudcontroller;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Provides access to test environment propery values.
 * <p/>
 * The environment properties are loaded from the file specified by the {@link #TEST_DD_CC_ENV_PROPERTY} system
 * property. Loading of the file takes place during the first call to {@link #envProperty(String)}.
 */
public class TestEnvironmentProperties {

   private static final String TEST_DD_CC_ENV_PROPERTY = "test.dimensiondata-cloudcontroller.environment";
   private static final String NON_EXISTENT_PROPERTY_MESSAGE = "Property file %s does not contain property %s.";
   private static final String TEST_DD_CC_ENV_PROPERTY_NOT_SET_MESSAGE = "The %s system property has not been set.";

   private static Properties envProperties;
   private static String fileThatWasLoaded;

   /**
    * Gets the value for the requested property, as defined in the environment properties file being used.
    *
    * @param propertyName the property for which the value is required.
    * @return the property value.
    * @throws RuntimeException if the requested property name is not defined in the environment properties file.
    */
   public static String envProperty(final String propertyName) {
      loadProperties();
      if (envProperties.containsKey(propertyName)) {
         return envProperties.getProperty(propertyName);
      } else {
         throw new RuntimeException(String.format(NON_EXISTENT_PROPERTY_MESSAGE, fileThatWasLoaded, propertyName));
      }
   }

   private static synchronized void loadProperties() {
      if (envProperties == null) {
         String envPropertyPath = System.getProperty(TEST_DD_CC_ENV_PROPERTY);
         if (envPropertyPath != null) {
            try {
               envProperties = new Properties();
               envProperties.load(new FileInputStream(envPropertyPath));
               fileThatWasLoaded = envPropertyPath;
            } catch (Exception e) {
               throw new RuntimeException("Failed to load environment properties from file: " + envPropertyPath, e);
            }
         } else {
            throw new RuntimeException(String.format(TEST_DD_CC_ENV_PROPERTY_NOT_SET_MESSAGE, TEST_DD_CC_ENV_PROPERTY));
         }
      }
   }

}
