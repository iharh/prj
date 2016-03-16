FROM centos:centos6

# Centos 7 has svn 1.7 by default - making it compatible with the host version
# add svn 1.7 repo
ADD WANdisco-1.8.repo /etc/yum.repos.d/
ADD RPM-GPG-KEY-WANdisco /etc/pki/rpm-gpg/
RUN yum -y update && yum -y install \
  curl \
  gcc \
  gcc-c++ \
  git \
  subversion.x86_64 \
  tar

# Oracle Java
RUN curl -LO 'http://download.oracle.com/otn-pub/java/jdk/8u45-b14/jdk-8u45-linux-x64.rpm' -H 'Cookie: oraclelicense=accept-securebackup-cookie' && rpm -i jdk-8u45-linux-x64.rpm; rm -f jdk-8u45-linux-x64.rpm; yum clean all

#RUN curl -LO 'http://pkgs.repoforge.org/rpmforge-release/rpmforge-release-0.5.3-1.el6.rf.x86_64.rpm' && rpm -i rpmforge-release-0.5.3-1.el6.rf.x86_64.rpm && rm rpmforge-release-0.5.3-1.el6.rf.x86_64.rpm && yum -y install git; yum clean all

RUN curl -LO 'http://archive.apache.org/dist/ant/binaries/apache-ant-1.9.4-bin.tar.gz' && tar -xzf apache-ant-1.9.4-bin.tar.gz -C /opt; rm -f apache-ant-1.9.4-bin.tar.gz

# RUN ln -s /usr/bin/g++44 /usr/local/bin/g++ && ln -s /usr/bin/gcc44 /usr/local/bin/gcc

ENV JAVA_HOME /usr/java/latest
ENV ANT_HOME /opt/apache-ant-1.9.4
ENV ANT_OPTS -server -Xms256m -Xmx512m -XX:NewSize=160m -XX:+UseParallelGC -XX:ParallelGCThreads=1 -Duser.language=en -Duser.country=US -Dfile.encoding=UTF-8
ENV PATH ${PATH}:${ANT_HOME}/bin

ENV CUR_USER ...
ENV CUR_USER_ID ...
# don't create home folder, don't add to to the lastlog database (too big of a number, runs out of space https://github.com/docker/docker/issues/5419)
RUN useradd ${CUR_USER} -M -l -u ${CUR_USER_ID} -s /bin/bash

WORKDIR /home/${CUR_USER}/
ENV HOME /home/${CUR_USER}

#VOLUME ["/home/${CUR_USER}"]
#USER ${CUR_USER}

CMD /bin/bash
