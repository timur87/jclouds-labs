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
package org.jclouds.azure.management.xml;

import org.jclouds.azure.management.domain.Location;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.util.SaxUtils;
import org.xml.sax.SAXException;

/**
 * @see <a href="http://msdn.microsoft.com/en-us/library/gg441293" >api</a>
 */
public class LocationHandler extends ParseSax.HandlerForGeneratedRequestWithResult<Location> {

   private StringBuilder currentText = new StringBuilder();
   private Location.Builder builder = Location.builder();

   /**
    * {@inheritDoc}
    */
   @Override
   public Location getResult() {
      try {
         return builder.build();
      } finally {
         builder = Location.builder();
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void endElement(String uri, String name, String qName) throws SAXException {
      if (qName.equals("Name")) {
         builder.name(SaxUtils.currentOrNull(currentText));
      } else if (qName.equals("DisplayName")) {
         builder.displayName(SaxUtils.currentOrNull(currentText));
      } else if (qName.equals("AvailableService")) {
         builder.addAvailableService(SaxUtils.currentOrNull(currentText));
      }
      currentText.setLength(0);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void characters(char ch[], int start, int length) {
      currentText.append(ch, start, length);
   }

}
