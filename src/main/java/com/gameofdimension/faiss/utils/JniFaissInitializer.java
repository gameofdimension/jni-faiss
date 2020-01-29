package com.gameofdimension.faiss.utils;

import com.google.common.base.Preconditions;

import java.io.IOException;

/**
 * @author yzq, yzq@leyantech.com
 * @date 2020-01-28.
 */
public class JniFaissInitializer {

  private static volatile boolean initialized = false;

  static {
    try {
      NativeUtils.loadLibraryFromJar("/_swigfaiss.so");
      initialized = true;
    } catch (IOException e) {
      Preconditions.checkArgument(false);
    }
  }

  public static boolean initialized() {
    return initialized;
  }
}
