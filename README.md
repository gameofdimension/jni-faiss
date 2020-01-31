# jni-faiss
> linux only so far

- git clone https://github.com/gameofdimension/jni-faiss.git && cd jni-faiss && git submodule update --init

- docker build -t jni-faiss .

- docker run -it jni-faiss java -cp /opt/jni-faiss/target/jni-faiss-0.0.1.jar com.gameofdimension.faiss.tutorial.ThreeIVFPQ
