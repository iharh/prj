FROM centos:centos7

RUN yum -y update && yum -y install man tar curl bzip2 dos2unix git make gcc gcc-c++ gcc-gfortran libgfortran.x86_64

ENV PRJ_NAME eigen
RUN mkdir -p /usr/local/${PRJ_NAME}
WORKDIR /usr/local/${PRJ_NAME}


ENV CMAKE_MAJOR_VERSION 3.3
ENV CMAKE_MINOR_VERSION 2
RUN curl -LO http://cmake.org/files/v${CMAKE_MAJOR_VERSION}/cmake-${CMAKE_MAJOR_VERSION}.${CMAKE_MINOR_VERSION}.tar.gz


# Download latest boost
# https://hub.docker.com/r/steeve/libtorrent-go/~/dockerfile/
ENV BOOST_VERSION 1.59.0
#curl -LO http://sourceforge.net/projects/boost/files/boost/1.59.0/boost_1_59_0.tar.bz2/download > boost_1_59_0.tar.bz2
RUN curl -LO http://sourceforge.net/projects/boost/files/boost/${BOOST_VERSION}/boost_`echo ${BOOST_VERSION} | sed 's/\\./_/g'`.tar.bz2/download \
    && mv download boost_`echo ${BOOST_VERSION} | sed 's/\\./_/g'`.tar.bz2


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

RUN tar xvjf boost_`echo ${BOOST_VERSION} | sed 's/\\./_/g'`.tar.bz2 \
    && rm boost_`echo ${BOOST_VERSION} | sed 's/\\./_/g'`.tar.bz2

RUN cd /usr/local/${PRJ_NAME}/boost_`echo ${BOOST_VERSION} | sed 's/\\./_/g'` \
    && ./bootstrap.sh \
    && ./b2 --build-type=minimal --with-system --with-thread link=static runtime-link=shared variant=release threading=multi install


WORKDIR /usr/local/${PRJ_NAME}

RUN tar xvjf eigen_${EIGEN_VERSION}.tar.bz2 \
    && rm eigen_${EIGEN_VERSION}.tar.bz2 \
    && mv `find . -maxdepth 1 -type d -name "eigen*"` eigen_${EIGEN_VERSION} \
    && mkdir eigen_build_${EIGEN_VERSION}

WORKDIR /usr/local/${PRJ_NAME}/eigen_build_${EIGEN_VERSION}/

RUN cmake ../eigen_${EIGEN_VERSION} \
    && make \
    && make install

WORKDIR /usr/local/${PRJ_NAME}

#RUN rm -rf eigen_build_${EIGEN_VERSION} \
#    && rm -rf eigen_${EIGEN_VERSION}

COPY a.cpp /usr/local/${PRJ_NAME}/
# /usr/local/include/eigen3/Eigen/Dense

RUN g++ -std=c++11 -pthread -O3 -I /usr/local/include/eigen3 a.cpp \
    && ./a.out

#-fomit-frame-pointer
#-funroll-all-loops

