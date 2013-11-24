package org.jclouds.orion.domain;

import java.util.List;

import org.jclouds.orion.domain.internal.JSONUtilsImpl;

import com.google.inject.ImplementedBy;

/**
 * JSON processing utilities for Orion domain objects
 * @author timur
 *
 */
@ImplementedBy(JSONUtilsImpl.class)
public interface JSONUtils {
   /**
    * Return passed object in json string
    * @param obj
    * @return
    */
   <T> String getObjectAsString(T obj);
   /**
    * Create the object out of json string
    * @param str
    * @param t
    * @return
    */
   <T> T getStringAsObject(String str, Class<T> t);
   /**
    * Fetch the list of container from a response json string and return them as OrionChildMetada
    * @param string
    * Response message from Orion to file list query
    * @return
    */
   List<OrionChildMetadata> fetchContainerObjects(String string);
   /**
    * Return list of files from a response string
    * @param string
    * Response message from Orion to file list query
    * @return
    */
   List<OrionChildMetadata> fetchFileObjects(String string);

}
