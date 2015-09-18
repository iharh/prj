FROM centos:centos7

RUN yum -y update && yum -y install curl wget git

ENV JAVA_PKG_NAME jdk-8u60-linux-x64
ENV JAVA_PKG_DIR 8u60-b27

# Install Java 8
# We do not pull Java base image as Docker does not discribute Oracle Java
RUN \
  wget http://download.oracle.com/otn-pub/java/jdk/$JAVA_PKG_DIR/$JAVA_PKG_NAME.rpm \
  --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" && \
  rpm -i $JAVA_PKG_NAME.rpm && \
  rm -f $JAVA_PKG_NAME.rpm

RUN curl -LO 'http://download.oracle.com/otn-pub/java/jdk/$JAVA_PKG_DIR/$JAVA_PKG_NAME.rpm' -H 'Cookie: oraclelicense=accept-securebackup-cookie' && rpm -i $JAVA_PKG_NAME.rpm && rm $JAVA_PKG_NAME.rpm

RUN yum -y install libgfortran.x86_64

#RUN yum -y install blas

ENV JAVA_HOME=/usr/java/latest 


COPY readme.txt /usr/local/netlib

RUN git clone git://github.com/fommil/netlib-java/

WORKDIR /usr/local/netlib
#CMD ["java", "-Xmx12g", "-Duser.language=en", "-Duser.country=US", "-Dfile.encoding=UTF-8", "-jar", "cb-cps-app-0.1.jar"]
