orion-jclouds
=====


jclouds blobstore implementation for orion based back-ends. This implementation is based on maven archetype of jclouds: http://jclouds.incubator.apache.org/documentation/devguides/creating-providers-with-maven/. 

An Orion server provides a set of APIs [1][2] for storing files retrieving them and updating them. This adapter exploits these APIs and converts Orion to a jClouds complaint Blobstore. 

[1] http://wiki.eclipse.org/Orion/Server_API/File_API
[2] http://wiki.eclipse.org/Orion/Server_API/Transfer_API

basic setup
=====

Please read <http://jclouds.incubator.apache.org/documentation/userguide/blobstore-guide/> for more information

```
    BlobStoreContext context = ContextBuilder.newBuilder("orionblob")
                 .credentials(username, password)
                 .buildView(BlobStoreContext.class);
```
The username and password are the login data of the Orion server.  

Default end-point is <https://orionhub.org/> this can be overridden by setting the end-point during the build of blobstore.
The general blob naming convention is like relative paths which start without a slash.

In general names as.metadata and with slashes are forbidden. 



mapping to orion concepts
===

A blob is either map to a file or to a folder. Containers are map to the top-level folders in the user workspaces and they can be called as projects as well.

some remarks
====



- A file and older with the same name in the same directory are forbidden. In such a case a request removes the existing duplicate and adds the requested item. 

- Username changes would be problematic since workspace name is based on it and Orion does not update workspace name in Version 3. In Version 4 it is not allowed. 



