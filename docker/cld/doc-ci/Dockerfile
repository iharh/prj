FROM centos:7
# 7 - is the latest one like: centos7.2.1511

RUN yum -y update
RUN yum -y install sudo which vim curl tar unzip

# ansible
RUN yum -y install python-setuptools python-paramiko
RUN easy_install pip
RUN pip install ansible

# Edit sudoers file
# To avoid error: sudo: sorry, you must have a tty to run sudo
RUN sed -i -e "s/Defaults    requiretty.*/Defaults    !requiretty/g" /etc/sudoers

# Fully quoted because of the ': ' on the line
RUN ansible localhost -m lineinfile -a "dest=/etc/sudoers state=present regexp='^%wheel' line='%wheel ALL=(ALL) NOPASSWD: ALL'"

RUN yum -y install gcc gcc-c++

#
# epel (needed for vcs)
#
RUN yum -y install epel-release

#
# vcs
#
#RUN rpm --import http://opensource.wandisco.com/RPM-GPG-KEY-WANdisco # later yum warns
#gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-WANdisco'\
#gpgkey=http://opensource.wandisco.com/RPM-GPG-KEY-WANdisco'\

RUN echo $'[WandiscoSVN]\n\
name=Wandisco SVN Repo\n\
baseurl=http://opensource.wandisco.com/centos/$releasever/svn-1.9/RPMS/$basearch/\n\
enabled=1\n\
gpgcheck=1\n\
gpgkey=http://opensource.wandisco.com/RPM-GPG-KEY-WANdisco'\
>> /etc/yum.repos.d/wandisco-svn.repo

RUN yum -y install subversion.x86_64

RUN echo $'[WandiscoGIT]\n\
name=Wandisco GIT Repo\n\
baseurl=http://opensource.wandisco.com/centos/$releasever/git/$basearch/\n\
enabled=1\n\
gpgcheck=1\n\
gpgkey=http://opensource.wandisco.com/RPM-GPG-KEY-WANdisco'\
>> /etc/yum.repos.d/wandisco-git.repo

RUN yum -y install git

#
# JDK
#

ENV JAVA_PKG_NAME jdk-8u77-linux-x64
ENV JAVA_PKG_DIR 8u77-b03

RUN curl -LO http://download.oracle.com/otn-pub/java/jdk/$JAVA_PKG_DIR/$JAVA_PKG_NAME.rpm -H 'Cookie: oraclelicense=accept-securebackup-cookie'\
  && rpm -i ${JAVA_PKG_NAME}.rpm\
  && rm -f ${JAVA_PKG_NAME}.rpm\
  && yum clean all

#
# gradle
#
#  gradle_version: "2.12"
#  gradle_url: "https://services.gradle.org/distributions/gradle-{{gradle_version}}-all.zip"
#
ENV GRADLE_NAME gradle-2.12
ENV GRADLE_PKG_NAME ${GRADLE_NAME}-all
RUN curl -LO https://services.gradle.org/distributions/$GRADLE_PKG_NAME.zip\
  && unzip $GRADLE_PKG_NAME.zip -d /opt\
  && rm -f $GRADLE_PKG_NAME.zip

ENV GRADLE_HOME /opt/${GRADLE_NAME}
ENV PATH ${PATH}:${GRADLE_HOME}/bin


#
# create user
#
ENV FILESCI_HOME filesci
ENV CISCRIPT add_host_user
COPY ${FILESCI_HOME}/${CISCRIPT}.sh ${USER_HOME}/
RUN chown -R ${USER_NAME}:${USER_NAME} ${USER_HOME}/${CISCRIPT}.sh
RUN chmod +x ${USER_HOME}/${CISCRIPT}.sh
RUN ${USER_HOME}/${CISCRIPT}.sh && rm ${USER_HOME}/${CISCRIPT}.sh

ENV USER_NAME cld
ENV USER_HOME /home/${USER_NAME}

USER ${USER_NAME}

#
# cld2
#

RUN git clone https://github.com/CLD2Owners/cld2.git ${USER_HOME}/cld2

#
# volumes
#
#"${USER_HOME}/.gradle"\
VOLUME  [\
  "/prj"\
]
