FROM centos:7

# Install MKL
# RUN yum-config-manager --add-repo https://yum.repos.intel.com/mkl/setup/intel-mkl.repo
# RUN rpm --import https://yum.repos.intel.com/intel-gpg-keys/GPG-PUB-KEY-INTEL-SW-PRODUCTS-2019.PUB
# RUN yum install -y intel-mkl-2019.3-062
# ENV LD_LIBRARY_PATH /opt/intel/mkl/lib/intel64:$LD_LIBRARY_PATH
# ENV LIBRARY_PATH /opt/intel/mkl/lib/intel64:$LIBRARY_PATH
# ENV LD_PRELOAD /usr/lib64/libgomp.so.1:/opt/intel/mkl/lib/intel64/libmkl_def.so:\
# /opt/intel/mkl/lib/intel64/libmkl_avx2.so:/opt/intel/mkl/lib/intel64/libmkl_core.so:\
# /opt/intel/mkl/lib/intel64/libmkl_intel_lp64.so:/opt/intel/mkl/lib/intel64/libmkl_gnu_thread.so

RUN yum install -y lapack lapack-devel

# Install necessary build tools
RUN yum install -y gcc-c++ make swig3

RUN yum install -y java-1.8.0-openjdk java-1.8.0-openjdk-devel maven

RUN ls -al /usr/lib/jvm/

COPY . /opt/jni-faiss

WORKDIR /opt/jni-faiss/faiss

# --with-cuda=/usr/local/cuda-8.0 
RUN ./configure --prefix=/usr --libdir=/usr/lib64 --without-cuda
RUN make -j $(nproc)
RUN make install


WORKDIR /opt/jni-faiss/jni

RUN make

WORKDIR /opt/jni-faiss

RUN mvn clean install -pl cpu -am

RUN mvn clean package -pl cpu-demo -am
