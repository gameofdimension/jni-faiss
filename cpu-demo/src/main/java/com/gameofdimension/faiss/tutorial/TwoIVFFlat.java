package com.gameofdimension.faiss.tutorial;

import static com.gameofdimension.faiss.utils.IndexHelper.makeRandomFloatArray;
import static com.gameofdimension.faiss.utils.IndexHelper.show;

import com.gameofdimension.faiss.swig.IndexFlatL2;
import com.gameofdimension.faiss.swig.IndexIVFFlat;
import com.gameofdimension.faiss.swig.MetricType;
import com.gameofdimension.faiss.swig.floatArray;
import com.gameofdimension.faiss.swig.longArray;
import com.gameofdimension.faiss.utils.JniFaissInitializer;
import com.google.common.base.Preconditions;

import java.util.Random;
import java.util.logging.Logger;

/**
 * @author yzq, yzq@leyantech.com
 * @date 2020-01-29.
 */
public class TwoIVFFlat {

  private static Logger LOG = Logger.getLogger(TwoIVFFlat.class.getName());
  private static int d = 64;                            // dimension
  private static int nb = 100000;                       // database size
  private static int nq = 10000;                        // nb of queries
  private static int nlist = 100;

  private floatArray xb;
  private floatArray xq;

  private Random random;
  private IndexFlatL2 quantizer;
  private IndexIVFFlat index;

  public TwoIVFFlat() {

    Preconditions.checkArgument(JniFaissInitializer.initialized());
    random = new Random(42);

    xb = makeRandomFloatArray(nb, d, random);
    xq = makeRandomFloatArray(nq, d, random);

    quantizer = new IndexFlatL2(d);
    index = new IndexIVFFlat(quantizer, d, nlist, MetricType.METRIC_L2);
    Preconditions.checkArgument(!index.getIs_trained());
    index.train(nb, xb.cast());
    Preconditions.checkArgument(index.getIs_trained());
    index.add(nb, xb.cast());
  }


  public void search() {
    int rn = 4;
    floatArray distances = new floatArray(nq * rn);
    longArray indices = new longArray(nq * rn);
    index.search(nq, xq.cast(), rn, distances.cast(), indices.cast());

    LOG.info(show(distances, 5, rn));
    LOG.info(show(indices, 5, rn));
  }

  public void searchAgain() {
    int rn = 4;
    floatArray distances = new floatArray(nq * rn);
    longArray indices = new longArray(nq * rn);
    index.setNprobe(10);
    index.search(nq, xq.cast(), rn, distances.cast(), indices.cast());

    LOG.info(show(distances, 5, rn));
    LOG.info(show(indices, 5, rn));
  }

  public static void main(String[] args) {
    TwoIVFFlat twoIVFFlat = new TwoIVFFlat();

    LOG.info("****************************************************");
    twoIVFFlat.search();
    LOG.info("****************************************************");
    twoIVFFlat.searchAgain();
  }
}
