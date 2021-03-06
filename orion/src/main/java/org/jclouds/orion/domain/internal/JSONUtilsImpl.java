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
package org.jclouds.orion.domain.internal;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jclouds.io.MutableContentMetadata;
import org.jclouds.io.payloads.BaseMutableContentMetadata;
import org.jclouds.orion.config.constans.OrionConstantValues;
import org.jclouds.orion.domain.Attributes;
import org.jclouds.orion.domain.JSONUtils;
import org.jclouds.orion.domain.MutableBlobProperties;
import org.jclouds.orion.domain.OrionChildMetadata;
import org.jclouds.orion.domain.OrionError;
import org.jclouds.orion.domain.OrionSpecificFileMetadata;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @see JSONUtils
 *
 */

public class JSONUtilsImpl implements JSONUtils {

   private final Gson converterGson;

   JSONUtilsImpl() {
      this.converterGson = new GsonBuilder().registerTypeAdapter(OrionError.class, new OrionErrorDeserializer())
            .registerTypeAdapter(MutableBlobProperties.class, new MutableBlobPropertiesDeserializer())
            .registerTypeAdapter(MutableContentMetadata.class, new MutableContentMetadataDeserializer())
            .registerTypeAdapter(OrionSpecificFileMetadata.class, new OrionSpecificFileMetadataDeserializer())
            .registerTypeAdapter(Attributes.class, new AttributesDeserializer())
            .registerTypeAdapter(OrionChildMetadata.class, new OrionChildMetadataDeserializer())
            .registerTypeAdapter(Date.class, new DateLongSerializer()).enableComplexMapKeySerialization().create();
   }

   @Override
   public <T> String getObjectAsString(T obj, Class<T> c) {
      return this.converterGson.toJson(obj, c);
   }

   @Override
   public <T> T getStringAsObject(String str, Class<T> c) {
      return this.converterGson.fromJson(str, c);
   }

   @Override
   public List<OrionChildMetadata> fetchContainerObjects(String string) {
      // get json string as object
      final JsonParser parser = new JsonParser();
      final JsonElement parentNode = parser.parse(string);

      final List<OrionChildMetadata> arrayList = Lists.newArrayList();
      // check if it has the children and the type of children are array then
      // fetch each child and add it to the list
      if (parentNode.getAsJsonObject().has(OrionConstantValues.LIST_CHILDREN)
            && parentNode.getAsJsonObject().get(OrionConstantValues.LIST_CHILDREN).isJsonArray()) {
         for (final JsonElement childElement : parentNode.getAsJsonObject().get(OrionConstantValues.LIST_CHILDREN)
               .getAsJsonArray()) {
            final OrionChildMetadata childData = this.converterGson.fromJson(childElement, OrionChildMetadata.class);
            // do not include metadata in the list
            if (childData.getName().equals(OrionConstantValues.ORION_METADATA_FILE_NAME)) {
               continue;
            }
            arrayList.add(childData);
         }
      }
      return arrayList;
   }

   @Override
   public List<OrionChildMetadata> fetchFileObjects(String string) {
      // get json string as object
      final JsonParser parser = new JsonParser();
      final JsonElement parentNode = parser.parse(string);
      final List<OrionChildMetadata> arrayList = Lists.newArrayList();
      this.fetchFileObjectsRecursively(parentNode, arrayList);
      return arrayList;
   }

   /**
    * Fetch file objects and return them as a list of {@link OrionChildMetadata}
    *
    * @param parentNode
    *           parent node of the tree
    * @param arrayList
    *           final list
    */
   private void fetchFileObjectsRecursively(JsonElement parentNode, List<OrionChildMetadata> arrayList) {
      // check if it has the children and the type of children are array then
      // fetch each child and add it to the list
      if (parentNode.getAsJsonObject().has(OrionConstantValues.LIST_CHILDREN)
            && parentNode.getAsJsonObject().get(OrionConstantValues.LIST_CHILDREN).isJsonArray()) {
         for (final JsonElement childElement : parentNode.getAsJsonObject().get(OrionConstantValues.LIST_CHILDREN)
               .getAsJsonArray()) {
            final OrionChildMetadata childData = this.converterGson.fromJson(childElement, OrionChildMetadata.class);
            // do not include metadata in the list
            if (childData.getName().equals(OrionConstantValues.ORION_METADATA_FILE_NAME)) {
               continue;
            }
            arrayList.add(childData);
            this.fetchFileObjectsRecursively(childElement, arrayList);
         }
      }

   }

   class OrionErrorDeserializer implements JsonDeserializer<OrionError>, InstanceCreator<OrionError> {
      @Override
      public OrionError deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
            throws JsonParseException {
         return JSONUtilsImpl.this.converterGson.fromJson(arg0, OrionErrorImpl.class);
      }

      @Override
      public OrionError createInstance(Type arg0) {
         return new OrionErrorImpl();
      }
   }

   class MutableBlobPropertiesDeserializer implements JsonDeserializer<MutableBlobProperties>,
   InstanceCreator<MutableBlobProperties>, JsonSerializer<MutableBlobProperties> {
      @Override
      public MutableBlobProperties deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
            throws JsonParseException {
         return JSONUtilsImpl.this.converterGson.fromJson(arg0, MutableBlobPropertiesImpl.class);
      }

      @Override
      public MutableBlobProperties createInstance(Type arg0) {
         return new MutableBlobPropertiesImpl();
      }

      @Override
      public JsonElement serialize(MutableBlobProperties arg0, Type arg1, JsonSerializationContext arg2) {
         return arg2.serialize(arg0, MutableBlobPropertiesImpl.class);
      }

   }

   class MutableContentMetadataDeserializer implements JsonDeserializer<MutableContentMetadata>,
   JsonSerializer<MutableContentMetadata>, InstanceCreator<MutableContentMetadata> {
      @Override
      public MutableContentMetadata deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
            throws JsonParseException {
         return JSONUtilsImpl.this.converterGson.fromJson(arg0, BaseMutableContentMetadata.class);
      }

      @Override
      public MutableContentMetadata createInstance(Type arg0) {
         return new BaseMutableContentMetadata();
      }

      @Override
      public JsonElement serialize(MutableContentMetadata arg0, Type arg1, JsonSerializationContext arg2) {
         return arg2.serialize(arg0);

      }

   }

   class OrionSpecificFileMetadataDeserializer implements JsonDeserializer<OrionSpecificFileMetadata>,
   JsonSerializer<OrionSpecificFileMetadata>, InstanceCreator<OrionSpecificFileMetadata> {
      @Override
      public OrionSpecificFileMetadata deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
            throws JsonParseException {
         return JSONUtilsImpl.this.converterGson.fromJson(arg0, OrionSpecificFileMetadataImpl.class);
      }

      @Override
      public JsonElement serialize(OrionSpecificFileMetadata arg0, Type arg1, JsonSerializationContext arg2) {
         return JSONUtilsImpl.this.converterGson.toJsonTree(arg0, OrionSpecificFileMetadataImpl.class);
      }

      @Override
      public OrionSpecificFileMetadata createInstance(Type arg0) {
         final OrionSpecificFileMetadata orionSpecificFileMetadata = new OrionSpecificFileMetadataImpl();
         orionSpecificFileMetadata.setAttributes(new AttributesImpl());
         return orionSpecificFileMetadata;
      }

   }

   class AttributesDeserializer implements JsonDeserializer<Attributes>, InstanceCreator<Attributes>,
   JsonSerializer<Attributes> {
      @Override
      public Attributes deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
            throws JsonParseException {
         return JSONUtilsImpl.this.converterGson.fromJson(arg0, AttributesImpl.class);
      }

      @Override
      public Attributes createInstance(Type arg0) {
         return new AttributesImpl();
      }

      @Override
      public JsonElement serialize(Attributes arg0, Type arg1, JsonSerializationContext arg2) {

         return arg2.serialize(arg0, AttributesImpl.class);
      }

   }

   class OrionChildMetadataDeserializer implements JsonDeserializer<OrionChildMetadata>,
   InstanceCreator<OrionChildMetadata> {
      @Override
      public OrionChildMetadata deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2)
            throws JsonParseException {
         return JSONUtilsImpl.this.converterGson.fromJson(arg0, OrionChildMetadataImpl.class);
      }

      @Override
      public OrionChildMetadata createInstance(Type arg0) {
         return new OrionChildMetadataImpl();
      }

   }

   /*
    * Create a Date object serializer since .setDateFormat(int) in Gson builder
    * does not change the result
    */
   class DateLongSerializer implements JsonDeserializer<Date>, JsonSerializer<Date> {

      @Override
      public JsonElement serialize(Date arg0, Type arg1, JsonSerializationContext arg2) {
         return new JsonPrimitive(arg0.getTime());
      }

      @Override
      public Date deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
         final Calendar temp = Calendar.getInstance();
         temp.setTimeInMillis(Long.parseLong(arg0.getAsString()));
         return temp.getTime();
      }
   }
}
