package com.gameofdimension.faiss.tutorial;

import static com.gameofdimension.faiss.utils.IndexHelper.makeRandomFloatArray;
import static com.gameofdimension.faiss.utils.IndexHelper.show;

import com.gameofdimension.faiss.swig.IndexFlatL2;
import com.gameofdimension.faiss.swig.IndexIVFPQ;
import com.gameofdimension.faiss.swig.floatArray;
import com.gameofdimension.faiss.swig.longArray;
import com.gameofdimension.faiss.utils.JniFaissInitializer;
import com.google.common.base.Preconditions;

import java.util.Random;
import java.util.logging.Logger;

/**
 * @author yzq, yzq@leyantech.com
 * @date 2020-01-31.
 */
public class ThreeIVFPQ {

  private static Logger LOG = Logger.getLogger(ThreeIVFPQ.class.getName());
  private static int d = 64;                            // dimension
  private static int nb = 100000;                       // database size
  private static int nq = 10000;                        // nb of queries
  private static int nlist = 100;
  private static int m = 8;

  private floatArray xb;
  private floatArray xq;

  private Random random;
  private IndexFlatL2 quantizer;
  private IndexIVFPQ index;

  public ThreeIVFPQ() {

    Preconditions.checkArgument(JniFaissInitializer.initialized());
    random = new Random(42);

    xb = makeRandomFloatArray(nb, d, random);
    xq = makeRandomFloatArray(nq, d, random);

    quantizer = new IndexFlatL2(d);
    index = new IndexIVFPQ(quantizer, d, nlist, m, 8);
    Preconditions.checkArgument(!index.getIs_trained());
    index.train(nb, xb.cast());
    Preconditions.checkArgument(index.getIs_trained());
    index.add(nb, xb.cast());
  }


  public void sanityCheck() {

    int rn = 4;
    int qn = 5;
    floatArray distances = new floatArray(qn * rn);
    longArray indices = new longArray(qn * rn);
    index.setNprobe(10);
    index.search(qn, xb.cast(), rn, distances.cast(), indices.cast());

    LOG.info(show(distances, qn, rn));
    LOG.info(show(indices, qn, rn));
  }


  public void search() {

    int rn = 4;
    floatArray distances = new floatArray(nq * rn);
    longArray indices = new longArray(nq * rn);
    index.setNprobe(10);
    index.search(nq, xq.cast(), rn, distances.cast(), indices.cast());

    LOG.info(show(distances, 5, rn));
    LOG.info(show(indices, 5, rn));
  }

  public static void main(String[] args) {
    ThreeIVFPQ threeIVFPQ = new ThreeIVFPQ();

    LOG.info("****************************************************");
    threeIVFPQ.sanityCheck();
    LOG.info("****************************************************");
    threeIVFPQ.search();
  }
}
