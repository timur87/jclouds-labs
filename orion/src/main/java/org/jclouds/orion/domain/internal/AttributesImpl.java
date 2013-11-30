package org.jclouds.orion.domain.internal;

import org.jclouds.orion.domain.Attributes;

import com.google.gson.annotations.SerializedName;

/**
 * @see Attributes
 * @author Timur
 * 
 */
public class AttributesImpl implements Attributes {
   @SerializedName("ReadOnly")
   Boolean readOnly = false;
   @SerializedName("Exectuable")
   Boolean executable = false;
   @SerializedName("Hidden")
   Boolean hidden = false;
   @SerializedName("Archive")
   Boolean archive = false;
   @SerializedName("SymbolicLink")
   Boolean symbolicLink = false;

   /**
    * @return the readOnly
    */
   @Override
   public Boolean getReadOnly() {
      return this.readOnly;
   }

   /**
    * @param readOnly
    *           the readOnly to set
    */
   @Override
   public void setReadOnly(Boolean readOnly) {
      this.readOnly = readOnly;
   }

   /**
    * @return the executable
    */
   @Override
   public Boolean getExecutable() {
      return this.executable;
   }

   /**
    * @param executable
    *           the executable to set
    */
   @Override
   public void setExecutable(Boolean executable) {
      this.executable = executable;
   }

   /**
    * @return the hidden
    */
   @Override
   public Boolean getHidden() {
      return this.hidden;
   }

   /**
    * @param hidden
    *           the hidden to set
    */
   @Override
   public void setHidden(Boolean hidden) {
      this.hidden = hidden;
   }

   /**
    * @return the archive
    */
   @Override
   public Boolean getArchive() {
      return this.archive;
   }

   /**
    * @param archive
    *           the archive to set
    */
   @Override
   public void setArchive(Boolean archive) {
      this.archive = archive;
   }

   /**
    * @return the symbolicLink
    */
   @Override
   public Boolean getSymbolicLink() {
      return this.symbolicLink;
   }

   /**
    * @param symbolicLink
    *           the symbolicLink to set
    */
   @Override
   public void setSymbolicLink(Boolean symbolicLink) {
      this.symbolicLink = symbolicLink;
   }

}
