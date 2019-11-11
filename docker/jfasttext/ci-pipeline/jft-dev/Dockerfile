FROM centos:7.6.1810

RUN yum -y update && yum -y install \
        sudo \
        which \
        vim \
        curl \
        tar \
        wget \
        git \
        gcc \
        gcc-c++

# Edit sudoers file
# To avoid error: sudo: sorry, you must have a tty to run sudo
RUN sed -i -e "s/Defaults    requiretty.*/Defaults    !requiretty/g" /etc/sudoers

ENV JAVA_HOME=/usr/java/latest

# tar.gz package checksum, download will be validated using it
ENV JDK_MD5=69afd4bba0831fd552c390d1cf3e17ed
ENV JDK_PACKAGE_NAME=amazon-corretto-11.0.2.9.3-linux-x64
ENV JDK_URL=https://d3pxv6yz143wms.cloudfront.net/11.0.2.9.3/${JDK_PACKAGE_NAME}.tar.gz

RUN set -ex \
 && echo "===> Downloading Amazon Corretto 11 ..." \
 && wget -q ${JDK_URL} -P /tmp \
 && echo "${JDK_MD5} /tmp/${JDK_PACKAGE_NAME}.tar.gz" | md5sum -c - \
 \
 && echo "===> Installing Amazon Corretto 11 ..." \
 && mkdir -p /usr/java \
 && tar zxvf /tmp/${JDK_PACKAGE_NAME}.tar.gz -C /usr/java \
 && ln -s /usr/java/${JDK_PACKAGE_NAME} /usr/java/latest \
 && ln -s /usr/java/latest /usr/java/default \
 && update-alternatives --install "/usr/bin/java" "java" "/usr/java/jdk-${JAVA_VERSION}/bin/java" 1 \
 \
 && echo "===> Cleaning up ..." \
 && rm -f /tmp/${JDK_PACKAGE_NAME}.tar.gz \
 && rm -f ${JAVA_HOME}/lib/src.zip

RUN ANT_PKG_NAME='apache-ant-1.10.5-bin' \
 && curl -LO http://archive.apache.org/dist/ant/binaries/${ANT_PKG_NAME}.tar.gz \
 && tar -xzf ${ANT_PKG_NAME}.tar.gz -C /opt \
 && rm -f ${ANT_PKG_NAME}.tar.gz

ENV ANT_HOME /opt/apache-ant-1.10.5
ENV ANT_OPTS -server -Xms256m -Xmx512m -XX:NewSize=160m -XX:+UseParallelGC -XX:ParallelGCThreads=1 -Duser.language=en -Duser.country=US -Dfile.encoding=UTF-8
ENV PATH ${PATH}:${ANT_HOME}/bin

RUN MVN_VER='3.6.2' \
 && curl -LO https://www-us.apache.org/dist/maven/maven-3/${MVN_VER}/binaries/apache-maven-${MVN_VER}-bin.tar.gz \
 && tar -xzf apache-maven-${MVN_VER}-bin.tar.gz -C /opt \
 && rm -f apache-maven-${MVN_VER}-bin.tar.gz

ENV M2_HOME /opt/apache-maven-3.6.2
ENV PATH ${PATH}:${M2_HOME}/bin

# Accept id through --build-arg and create jft user matching current logon user ID
ARG uid
RUN useradd -l --uid=$uid jft
USER jft
