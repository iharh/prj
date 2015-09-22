FROM centos:centos7

RUN yum -y update && yum -y install man tar curl wget bzip2 dos2unix git gcc gcc-c++

ENV PRJ_NAME ublas
RUN mkdir -p /usr/local/${PRJ_NAME}
WORKDIR /usr/local/${PRJ_NAME}

# Download latest boost
# https://hub.docker.com/r/steeve/libtorrent-go/~/dockerfile/
ENV BOOST_VERSION 1.59.0
#curl -LO http://sourceforge.net/projects/boost/files/boost/1.59.0/boost_1_59_0.tar.bz2/download > boost_1_59_0.tar.bz2
RUN curl -LO http://sourceforge.net/projects/boost/files/boost/${BOOST_VERSION}/boost_`echo ${BOOST_VERSION} | sed 's/\\./_/g'`.tar.bz2/download \
    && mv download boost_`echo ${BOOST_VERSION} | sed 's/\\./_/g'`.tar.bz2

COPY sums.txt /usr/local/${PRJ_NAME}/
#RUN dos2unix sums.txt \
#    && sha256sum -c --strict sums.txt \
#    && rm sums.txt

#RUN tar xvjf boost_`echo ${BOOST_VERSION} | sed 's/\\./_/g'`.tar.bz2 \
#    && rm boost_`echo ${BOOST_VERSION} | sed 's/\\./_/g'`.tar.bz2

#RUN cd boost_`echo ${BOOST_VERSION} | sed 's/\\./_/g'` \
#    && ./bootstrap.sh \
#    && ./b2 --build-type=minimal --with-system --with-thread link=static runtime-link=shared variant=release threading=multi install

#COPY a.cpp /usr/local/${PRJ_NAME}/

#RUN g++ -std=c++11 -pthread a.cpp \
#    && ./a.out
