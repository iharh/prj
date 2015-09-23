FROM centos:centos7

RUN yum -y update && yum -y install man tar curl bzip2 dos2unix git make gcc gcc-c++ gcc-gfortran libgfortran.x86_64

ENV PRJ_NAME eigen
RUN mkdir -p /usr/local/${PRJ_NAME}
WORKDIR /usr/local/${PRJ_NAME}

ENV CMAKE_MAJOR_VERSION 3.3
ENV CMAKE_MINOR_VERSION 2
RUN curl -LO http://cmake.org/files/v${CMAKE_MAJOR_VERSION}/cmake-${CMAKE_MAJOR_VERSION}.${CMAKE_MINOR_VERSION}.tar.gz

ENV EIGEN_VERSION 3.2.5
RUN curl -LO http://bitbucket.org/eigen/eigen/get/${EIGEN_VERSION}.tar.bz2 \
    && mv ${EIGEN_VERSION}.tar.bz2 eigen_${EIGEN_VERSION}.tar.bz2 

COPY sums.txt /usr/local/${PRJ_NAME}/
RUN dos2unix sums.txt \
    && sha256sum -c --strict sums.txt \
    && rm sums.txt

RUN tar xvf cmake-${CMAKE_MAJOR_VERSION}.${CMAKE_MINOR_VERSION}.tar.gz \
    && rm cmake-${CMAKE_MAJOR_VERSION}.${CMAKE_MINOR_VERSION}.tar.gz

WORKDIR /usr/local/${PRJ_NAME}/cmake-${CMAKE_MAJOR_VERSION}.${CMAKE_MINOR_VERSION}

RUN ./bootstrap \
    && make \
    && make install 
# ??? gmake

WORKDIR /usr/local/${PRJ_NAME}

RUN rm -rf cmake-${CMAKE_MAJOR_VERSION}.${CMAKE_MINOR_VERSION}

RUN tar xvjf eigen_${EIGEN_VERSION}.tar.bz2 \
    && rm eigen_${EIGEN_VERSION}.tar.bz2 \
    && mv `find . -maxdepth 1 -type d -name "eigen*"` eigen_${EIGEN_VERSION} \
    && mkdir eigen_build_${EIGEN_VERSION}

WORKDIR /usr/local/eigen_build_${EIGEN_VERSION}/

RUN cmake ../eigen_

COPY a.cpp /usr/local/${PRJ_NAME}/
# /usr/local/include/eigen3/Eigen/Dense

RUN g++ -std=c++11 -pthread -O3 a.cpp \
    && ./a.out
