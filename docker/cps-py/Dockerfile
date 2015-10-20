FROM centos:centos7

RUN yum -y update && yum -y install man tar curl bzip2 dos2unix git make gcc gcc-c++ gcc-gfortran libgfortran.x86_64

ENV PRJ_NAME cps-py
RUN mkdir -p /usr/local/${PRJ_NAME}
WORKDIR /usr/local/${PRJ_NAME}

ENV CONDA_SCRIPT Miniconda-3.16.0-Linux-x86_64.sh
RUN curl -LO https://repo.continuum.io/miniconda/${CONDA_SCRIPT}

COPY sums.txt /usr/local/${PRJ_NAME}/
RUN dos2unix sums.txt \
    && sha256sum -c --strict sums.txt \
    && rm sums.txt

RUN /bin/bash ${CONDA_SCRIPT} -b -p /opt/anaconda \
    && rm ${CONDA_SCRIPT} \
    && echo 'export PATH=/opt/anaconda/bin:$PATH' >> ~/.bashrc

# --update-deps is true by default -> update even conda
RUN /opt/anaconda/bin/conda install -qy gensim flask

# TODO: create a dep-list
RUN /opt/anaconda/bin/pip install flask-restful
