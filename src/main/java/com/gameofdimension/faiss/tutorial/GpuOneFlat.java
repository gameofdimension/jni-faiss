package com.gameofdimension.faiss.tutorial;

import static com.gameofdimension.faiss.utils.IndexHelper.makeRandomFloatArray;
import static com.gameofdimension.faiss.utils.IndexHelper.show;

import com.gameofdimension.faiss.swig.GpuIndexFlatL2;
import com.gameofdimension.faiss.swig.StandardGpuResources;
import com.gameofdimension.faiss.swig.floatArray;
import com.gameofdimension.faiss.swig.longArray;
import com.gameofdimension.faiss.utils.JniFaissInitializer;
import com.google.common.base.Preconditions;

import java.util.Random;
import java.util.logging.Logger;

/**
 * @author yzq, yzq@leyantech.com
 * @date 2020-01-28.
 */
public class GpuOneFlat {

  private static Logger LOG = Logger.getLogger(GpuOneFlat.class.getName());
  private static int d = 64;                            // dimension
  private static int nb = 100000;                       // database size
  private static int nq = 10000;                        // nb of queries

  private floatArray xb;
  private floatArray xq;

  private Random random;
  private StandardGpuResources res;
  private GpuIndexFlatL2 index;

  public GpuOneFlat() {
    Preconditions.checkArgument(JniFaissInitializer.initialized());
    random = new Random(42);
    res = new StandardGpuResources();
    index = new GpuIndexFlatL2(res, d);
    LOG.info(String.format("is_trained = %s, ntotal = %d",
        index.getIs_trained(), index.getNtotal()));
    xb = makeRandomFloatArray(nb, d, random);
    xq = makeRandomFloatArray(nq, d, random);
    index.add(nb, xb.cast());
  }

  public void sanityCheck() {
    int rn = 4;
    int qn = 5;
    floatArray distances = new floatArray(qn * rn);
    longArray indices = new longArray(qn * rn);
    index.search(qn, xb.cast(), rn, distances.cast(), indices.cast());

    LOG.info(show(distances, qn, rn));
    LOG.info(show(indices, qn, rn));
  }

  public void search() {
    int rn = 4;
    floatArray distances = new floatArray(nq * rn);
    longArray indices = new longArray(nq * rn);
    index.search(nq, xq.cast(), rn, distances.cast(), indices.cast());

    LOG.info(show(distances, 5, rn));
    LOG.info(show(indices, 5, rn));
  }

  public static void main(String[] args) {
    GpuOneFlat oneFlat = new GpuOneFlat();

    LOG.info("****************************************************");
    oneFlat.sanityCheck();
    LOG.info("****************************************************");
    oneFlat.search();
  }
}
