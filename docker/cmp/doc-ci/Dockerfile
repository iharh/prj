FROM centos:centos6.9

RUN yum -y update && yum -y install sudo which vim curl tar

# Edit sudoers file
# To avoid error: sudo: sorry, you must have a tty to run sudo
RUN sed -i -e "s/Defaults    requiretty.*/Defaults    !requiretty/g" /etc/sudoers

RUN yum -y install gcc gcc-c++

ENV FILESCI_HOME filesci

COPY ${FILESCI_HOME}/wandisco-svn.repo /etc/yum.repos.d/
#ADD RPM-GPG-KEY-WANdisco /etc/pki/rpm-gpg/
RUN yum -y install subversion.x86_64

COPY ${FILESCI_HOME}/wandisco-git.repo /etc/yum.repos.d/
RUN yum -y install git

# Oracle Java

ENV JAVA_PKG_NAME jdk-8u152-linux-x64
ENV JAVA_PKG_DIR 8u152-b16
ENV JAVA_PKG_HASH aa0333dd3019491ca4f6ddbe78cdb6d0

#http://download.oracle.com/otn-pub/java/jdk/8u152-b16/aa0333dd3019491ca4f6ddbe78cdb6d0/jdk-8u152-linux-x64.rpm

RUN curl -LO http://download.oracle.com/otn-pub/java/jdk/$JAVA_PKG_DIR/$JAVA_PKG_HASH/$JAVA_PKG_NAME.rpm -H 'Cookie: oraclelicense=accept-securebackup-cookie'

RUN rpm -i ${JAVA_PKG_NAME}.rpm &&\
    rm -f ${JAVA_PKG_NAME}.rpm &&\
    yum clean all

ENV ANT_PKG_NAME apache-ant-1.9.6-bin
RUN curl -LO http://archive.apache.org/dist/ant/binaries/$ANT_PKG_NAME.tar.gz &&\
    tar -xzf $ANT_PKG_NAME.tar.gz -C /opt &&\
    rm -f $ANT_PKG_NAME.tar.gz

ENV JAVA_HOME /usr/java/latest
ENV ANT_HOME /opt/apache-ant-1.9.6
ENV ANT_OPTS -server -Xms256m -Xmx512m -XX:NewSize=160m -XX:+UseParallelGC -XX:ParallelGCThreads=1 -Duser.language=en -Duser.country=US -Dfile.encoding=UTF-8
ENV PATH ${PATH}:${ANT_HOME}/bin

#
# create user
#
ENV USER_NAME root
ENV USER_HOME /${USER_NAME}

ENV CISCRIPT add_host_user
COPY ${FILESCI_HOME}/${CISCRIPT}.sh ${USER_HOME}/
RUN chown -R ${USER_NAME}:${USER_NAME} ${USER_HOME}/${CISCRIPT}.sh
RUN chmod +x ${USER_HOME}/${CISCRIPT}.sh
RUN ${USER_HOME}/${CISCRIPT}.sh && rm ${USER_HOME}/${CISCRIPT}.sh

ENV USER_NAME nlp
ENV USER_HOME /home/${USER_NAME}

USER ${USER_NAME}
##WORKDIR ${USER_HOME}

#
## volumes
#
VOLUME  [\
  "${USER_HOME}/.gradle"\
  , "${USER_HOME}/.ssh"\
  , "/cmp"\
  , "/fx"\
]
