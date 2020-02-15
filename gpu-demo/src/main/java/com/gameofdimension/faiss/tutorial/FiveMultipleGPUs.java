package com.gameofdimension.faiss.tutorial;

import static com.gameofdimension.faiss.swig.swigfaiss_gpu.getNumDevices;
import static com.gameofdimension.faiss.utils.IndexHelper.makeRandomFloatArray;
import static com.gameofdimension.faiss.utils.IndexHelper.show;

import com.gameofdimension.faiss.swig.GpuResourcesVector;
import com.gameofdimension.faiss.swig.Index;
import com.gameofdimension.faiss.swig.IndexFlatL2;
import com.gameofdimension.faiss.swig.IntVector;
import com.gameofdimension.faiss.swig.StandardGpuResources;
import com.gameofdimension.faiss.swig.floatArray;
import com.gameofdimension.faiss.swig.longArray;
import com.gameofdimension.faiss.swig.swigfaiss_gpu;
import com.gameofdimension.faiss.utils.JniFaissInitializer;
import com.google.common.base.Preconditions;

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

  private floatArray xb;
  private floatArray xq;

  private Random random;
  private Index gpuIndex;

  public FiveMultipleGPUs() {

    Preconditions.checkArgument(JniFaissInitializer.initialized());
    int ngpus = getNumDevices();
    LOG.info(String.format("Number of GPUs: %d", ngpus));
    GpuResourcesVector res = new GpuResourcesVector();
    IntVector devs = new IntVector();
    for (int i = 0; i < ngpus; i++) {
      devs.push_back(i);
      res.push_back(new StandardGpuResources());
    }

    IndexFlatL2 cpuIndex = new IndexFlatL2(d);
    gpuIndex = swigfaiss_gpu.index_cpu_to_gpu_multiple(res, devs, cpuIndex);

    random = new Random(42);
    xb = makeRandomFloatArray(nb, d, random);
    xq = makeRandomFloatArray(nq, d, random);
    LOG.info(String.format("is_trained = %s", gpuIndex.getIs_trained()));
    gpuIndex.add(nb, xb.cast());  // add vectors to the index
    LOG.info(String.format("ntotal = %d", gpuIndex.getNtotal()));
  }

  public void search() {
    int rn = 4;
    floatArray distances = new floatArray(nq * rn);
    longArray indices = new longArray(nq * rn);
    gpuIndex.search(nq, xq.cast(), rn, distances.cast(), indices.cast());

    LOG.info(show(distances, 5, rn));
    LOG.info(show(indices, 5, rn));
  }

  public static void main(String[] args) {
    FiveMultipleGPUs fiveMultipleGPUs = new FiveMultipleGPUs();

    LOG.info("****************************************************");
    fiveMultipleGPUs.search();
  }
}
