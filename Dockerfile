FROM centos:7

RUN yum install -y lapack lapack-devel

# Install necessary build tools
RUN yum install -y gcc-c++ make swig3

RUN yum install -y java-1.8.0-openjdk java-1.8.0-openjdk-devel maven

COPY . /opt/jni-faiss

WORKDIR /opt/jni-faiss/faiss

RUN ./configure --prefix=/usr --libdir=/usr/lib64 --without-cuda
RUN make -j $(nproc)
RUN make install


WORKDIR /opt/jni-faiss/jni

RUN make

WORKDIR /opt/jni-faiss

RUN mvn clean install -pl cpu -am

RUN mvn clean package -pl cpu-demo -am
