FROM centos:centos7

RUN yum -y update && yum -y install man tar curl bzip2 dos2unix git make
RUN yum -y update && yum -y install clang clang-analyzer
#gcc gcc-c++ gcc-gfortran libgfortran.x86_64

ENV PRJ_NAME c1
RUN mkdir -p /usr/local/${PRJ_NAME}
WORKDIR /usr/local/${PRJ_NAME}
