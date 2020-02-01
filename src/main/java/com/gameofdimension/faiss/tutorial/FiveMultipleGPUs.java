package com.gameofdimension.faiss.tutorial;

import com.gameofdimension.faiss.swig.GpuIndexFlatL2;
import com.gameofdimension.faiss.swig.GpuIndexIVFFlat;
import com.gameofdimension.faiss.swig.StandardGpuResources;
import com.gameofdimension.faiss.swig.floatArray;

import java.util.Random;
import java.util.logging.Logger;

/**
 * @author yzq, yzq@leyantech.com
 * @date 2020-02-01.
 */
public class FiveMultipleGPUs {

  private static Logger LOG = Logger.getLogger(FiveMultipleGPUs.class.getName());
  private static int d = 64;                            // dimension
  private static int nb = 100000;                       // database size
  private static int nq = 10000;                        // nb of queries
  private static int nlist = 100;

  private floatArray xb;
  private floatArray xq;

  private Random random;
  private StandardGpuResources res;
  private GpuIndexFlatL2 index;
  private GpuIndexIVFFlat ivfIndex;

  public FiveMultipleGPUs() {
  }
}
