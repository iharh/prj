FROM centos:7.7.1908
#8.1.1911

RUN yum -y update && yum -y install \
        curl \
        git \
        tar \
        subversion \
        sudo \
        vim \
        wget \
        which

# Edit sudoers file
# To avoid error: sudo: sorry, you must have a tty to run sudo
RUN sed -i -e "s/Defaults    requiretty.*/Defaults    !requiretty/g" /etc/sudoers

ENV JAVA_HOME=/usr/java/latest

# https://github.com/corretto/corretto-11/releases
# tar.gz package checksum, download will be validated using it
ENV JDK_MD5=cfb0b142edf7ebc2f87a27405c8d39fc
ENV JDK_PACKAGE_VERSION=11.0.6.10.1
ENV JDK_PACKAGE_NAME=amazon-corretto-${JDK_PACKAGE_VERSION}-linux-x64
ENV JDK_URL=https://corretto.aws/downloads/resources/${JDK_PACKAGE_VERSION}/${JDK_PACKAGE_NAME}.tar.gz

RUN set -ex \
 && echo "==> Download Amazon Corretto 11 ..." \
 && wget -q ${JDK_URL} -P /tmp \
 && echo "${JDK_MD5} /tmp/${JDK_PACKAGE_NAME}.tar.gz" | md5sum -c - \
 \
 && echo "==> Install Amazon Corretto 11 ..." \
 && mkdir -p /usr/java \
 && tar zxvf /tmp/${JDK_PACKAGE_NAME}.tar.gz -C /usr/java \
 && ln -s /usr/java/${JDK_PACKAGE_NAME} /usr/java/latest \
 && ln -s /usr/java/latest /usr/java/default \
 && update-alternatives --install "/usr/bin/java" "java" "/usr/java/latest/bin/java" 1 \
 \
 && echo "==> Clean up ..." \
 && rm -f /tmp/${JDK_PACKAGE_NAME}.tar.gz \
 && rm -f ${JAVA_HOME}/lib/src.zip

RUN ANT_PKG_NAME='apache-ant-1.10.7-bin' \
 && curl -LO http://archive.apache.org/dist/ant/binaries/${ANT_PKG_NAME}.tar.gz \
 && tar -xzf ${ANT_PKG_NAME}.tar.gz -C /opt \
 && rm -f ${ANT_PKG_NAME}.tar.gz

ENV ANT_HOME /opt/apache-ant-1.10.7
ENV ANT_OPTS -server -Xms256m -Xmx4096m -XX:NewSize=160m -XX:+UseParallelGC -XX:ParallelGCThreads=1 -Duser.language=en -Duser.country=US -Dfile.encoding=UTF-8 -Dorg.bytedeco.javacpp.maxbytes=4G -Dorg.bytedeco.javacpp.maxphysicalbytes=6G
ENV PATH ${PATH}:${ANT_HOME}/bin

# RUN wget https://dvc.org/rpm/dvc.repo -O /etc/yum.repos.d/dvc.repo
RUN yum -y update && yum -y install \
    gcc-c++
    # compat-gcc-44-c++
    # dvc \

# Accept id through --build-arg and create nlp user matching current logon user ID
ARG uid
RUN useradd -l --uid=$uid nlp
USER nlp
