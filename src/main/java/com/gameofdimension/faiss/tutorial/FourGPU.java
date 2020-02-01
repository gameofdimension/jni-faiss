package com.gameofdimension.faiss.tutorial;

import static com.gameofdimension.faiss.swig.MetricType.METRIC_L2;
import static com.gameofdimension.faiss.utils.IndexHelper.makeRandomFloatArray;
import static com.gameofdimension.faiss.utils.IndexHelper.show;

import com.gameofdimension.faiss.swig.GpuIndexFlatL2;
import com.gameofdimension.faiss.swig.GpuIndexIVFFlat;
import com.gameofdimension.faiss.swig.StandardGpuResources;
import com.gameofdimension.faiss.swig.floatArray;
import com.gameofdimension.faiss.swig.longArray;
import com.gameofdimension.faiss.utils.JniFaissInitializer;
import com.google.common.base.Preconditions;

import java.util.Random;
import java.util.logging.Logger;

/**
 * @author yzq, yzq@leyantech.com
 * @date 2020-02-01.
 */
public class FourGPU {

  private static Logger LOG = Logger.getLogger(FourGPU.class.getName());
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

  public FourGPU() {
    Preconditions.checkArgument(JniFaissInitializer.initialized());
    random = new Random(42);
    res = new StandardGpuResources();
    index = new GpuIndexFlatL2(res, d);
    ivfIndex = new GpuIndexIVFFlat(res, d, nlist, METRIC_L2);
    xb = makeRandomFloatArray(nb, d, random);
    xq = makeRandomFloatArray(nq, d, random);
    ivfIndex.train(nb, xb.cast());
    ivfIndex.add(nb, xb.cast());
    index.add(nb, xb.cast());
    LOG.info(String.format("is_trained = %s, ntotal = %d",
        index.getIs_trained(), index.getNtotal()));
  }

  public void searchFlat() {
    int rn = 4;
    floatArray distances = new floatArray(nq * rn);
    longArray indices = new longArray(nq * rn);
    index.search(nq, xq.cast(), rn, distances.cast(), indices.cast());

    LOG.info(show(distances, 5, rn));
    LOG.info(show(indices, 5, rn));
  }

  public void searchIvf() {
    Preconditions.checkArgument(ivfIndex.getIs_trained());
    int rn = 4;
    floatArray distances = new floatArray(nq * rn);
    longArray indices = new longArray(nq * rn);
    ivfIndex.search(nq, xq.cast(), rn, distances.cast(), indices.cast());

    LOG.info(show(distances, 5, rn));
    LOG.info(show(indices, 5, rn));
  }

  public static void main(String[] args) {
    FourGPU fourGPU = new FourGPU();

    LOG.info("****************************************************");
    fourGPU.searchFlat();
    LOG.info("****************************************************");
    fourGPU.searchIvf();
  }
}
