# Copyright (c) Facebook, Inc. and its affiliates.
#
# This source code is licensed under the MIT license found in the
# LICENSE file in the root directory of this source tree.

-include ../faiss/makefile.inc

ifneq ($(strip $(NVCC)),)
	SWIGFLAGS += -DGPU_WRAPPER
endif

JAVACFLAGS = -I /usr/lib/jvm/java/include/ -I /usr/lib/jvm/java/include/linux/
SWIGJAVASRC = ../cpu/src/main/java/com/gameofdimension/faiss/swig

all: build

# Also silently generates swigfaiss.py.
swigfaiss.cpp: swigfaiss.swig ../faiss/libfaiss.a
	mkdir -p $(SWIGJAVASRC)
	$(SWIG) -java -c++ -Doverride= -I../ $(SWIGFLAGS) -package com.gameofdimension.faiss.swig -outdir $(SWIGJAVASRC) -o $@ $<

swigfaiss_avx2.cpp: swigfaiss.swig ../faiss/libfaiss.a
	mkdir -p $(SWIGJAVASRC)
	$(SWIG) -java -c++ -Doverride= -module swigfaiss_avx2 -I../ $(SWIGFLAGS) -package com.gameofdimension.faiss.swig -outdir $(SWIGJAVASRC) -o $@ $<

%.o: %.cpp
	$(CXX) $(CPPFLAGS) $(CXXFLAGS) $(CPUFLAGS) $(JAVACFLAGS) \
               -I../ -c $< -o $@

# Extension is .so even on OSX.
_%.so: %.o ../faiss/libfaiss.a
	$(CXX) $(SHAREDFLAGS) $(LDFLAGS) -o $@ $^ $(LIBS)

build: _swigfaiss.so
	mkdir -p ../cpu/src/main/resources
	cp _swigfaiss.so ../cpu/src/main/resources/

# install: build
# 	$(PYTHON) setup.py install
# 

clean:
	rm -f swigfaiss*.cpp swigfaiss*.o  _swigfaiss*.so
	rm -rf $(SWIGJAVASRC)


.PHONY: all build clean install
