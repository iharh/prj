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

RUN /opt/anaconda/bin/conda install -qy conda=3.18.3

COPY cps-conda-requirements.txt /usr/local/${PRJ_NAME}/
# --update-deps is true by default -> update even conda
RUN /opt/anaconda/bin/conda install -qy --no-update-deps --file /usr/local/${PRJ_NAME}/cps-conda-requirements.txt

COPY cps-pip-requirements.txt /usr/local/${PRJ_NAME}/
RUN /opt/anaconda/bin/pip install -r /usr/local/${PRJ_NAME}/cps-pip-requirements.txt
