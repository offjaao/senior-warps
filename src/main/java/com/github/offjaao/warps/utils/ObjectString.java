package com.github.offjaao.warps.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class ObjectString {
   private String code;
   private Object object;

   public ObjectString(String code) {
      try {
         ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(code));
         BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
         Object object = dataInput.readObject();
         dataInput.close();
         this.code = code;
         this.object = object;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public ObjectString(Object object) {
      try {
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
         dataOutput.writeObject(object);
         dataOutput.close();
         this.object = object;
         this.code = Base64Coder.encodeLines(outputStream.toByteArray());
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public Object getObject() {
      return this.object;
   }

   public String getCode() {
      return this.code;
   }
}