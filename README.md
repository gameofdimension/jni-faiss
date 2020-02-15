# jni-faiss
> linux only so far

## cpu

- git clone https://github.com/gameofdimension/jni-faiss.git && cd jni-faiss && git submodule update --init

- docker build -t jni-faiss .

- docker run -it jni-faiss java -cp cpu-demo/target/cpu-demo-0.0.1.jar com.gameofdimension.faiss.tutorial.OneFlat

## gpu

- git clone https://github.com/gameofdimension/jni-faiss.git && cd jni-faiss && git submodule update --init

- docker build -t jni-faiss-gpu -f DockerfileGpu .

- docker run --gpus 1 -it jni-faiss-gpu java -Xmx8g -cp gpu-demo/target/gpu-demo-0.0.1.jar com.gameofdimension.faiss.tutorial.GpuOneFlat

## reference

- https://github.com/adamheinrich/native-utils

- https://github.com/thenetcircle/faiss4j