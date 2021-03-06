FROM centos:centos7

RUN yum -y update && yum -y install man curl wget dos2unix git gcc gcc-gfortran libgfortran.x86_64

ENV JAVA_PKG_NAME jdk-8u60-linux-x64
ENV JAVA_PKG_DIR 8u60-b27

# Maven version and options
ENV MAVEN_VERSION 3.3.3
ENV MAVEN_ROOT /var/lib/maven
ENV MAVEN_HOME ${MAVEN_ROOT}/apache-maven-${MAVEN_VERSION}

# Download jdk and apache maven
RUN curl -LO http://download.oracle.com/otn-pub/java/jdk/$JAVA_PKG_DIR/$JAVA_PKG_NAME.rpm -H 'Cookie: oraclelicense=accept-securebackup-cookie'
RUN curl -kLO http://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz

COPY sums.txt /
RUN dos2unix /sums.txt &&\
    sha256sum -c --strict /sums.txt

RUN rpm -i $JAVA_PKG_NAME.rpm &&\
    rm $JAVA_PKG_NAME.rpm

RUN mkdir -p $MAVEN_ROOT &&\
    gunzip apache-maven-$MAVEN_VERSION-bin.tar.gz &&\
    tar -xf apache-maven-$MAVEN_VERSION-bin.tar -C $MAVEN_ROOT &&\
    rm -f apache-maven-$MAVEN_VERSION-bin.tar*

ENV JAVA_HOME=/usr/java/latest
ENV PATH ${PATH}:${MAVEN_HOME}/bin

COPY netlib-privkey.asc /
RUN dos2unix /netlib-privkey.asc &&\
    gpg --allow-secret-key-import --import --armor /netlib-privkey.asc

#COPY netlib-pubkey.asc /
#RUN dos2unix /netlib-pubkey.asc

RUN git clone git://github.com/fommil/netlib-java /usr/local/netlib/netlib-java

RUN cd /usr/local/netlib/netlib-java/generator &&\
    mvn install -Dgpg.passphrase=

WORKDIR /usr/local/netlib
#CMD ["java", "-Xmx12g", "-Duser.language=en", "-Duser.country=US", "-Dfile.encoding=UTF-8", "-jar", "cb-cps-app-0.1.jar"]
