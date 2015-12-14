FROM centos:centos7


RUN yum -y update && yum -y install sudo dos2unix openssh-clients
#bzip2 sudo shadow-utils(useradd/del/mod)
RUN yum -y install epel-release
RUN yum -y install ansible
RUN yum -y groupinstall "Development tools"
RUN yum -y install clang clang-analyzer
#gcc gcc-c++ gcc-gfortran libgfortran.x86_64
RUN yum -y install vim man tar curl bzip2 dos2unix git make

# Edit sudoers file
# To avoid error: sudo: sorry, you must have a tty to run sudo
RUN sed -i -e "s/Defaults    requiretty.*/Defaults    !requiretty/g" /etc/sudoers

# Fully quoted because of the ': ' on the line
RUN ansible localhost -m lineinfile -a "dest=/etc/sudoers state=present regexp='^%wheel' line='%wheel ALL=(ALL) NOPASSWD: ALL'"

#
# create user
#
ENV USER_NAME root
ENV USER_HOME /${USER_NAME}

ENV FILESCI_HOME filesci

ENV CISCRIPT add_host_user
COPY ${FILESCI_HOME}/${CISCRIPT}.sh ${USER_HOME}/
RUN sudo chown -R ${USER_NAME}:${USER_NAME} ${USER_HOME}/${CISCRIPT}.sh
RUN sudo chmod +x ${USER_HOME}/${CISCRIPT}.sh
RUN ${USER_HOME}/${CISCRIPT}.sh


ENV USER_NAME c1
ENV USER_HOME /home/${USER_NAME}

WORKDIR ${USER_HOME}
USER ${USER_NAME}

#
#
#

COPY a.cpp /home/${USER_NAME}
RUN sudo chown $(id -un):$(id -gn) ${USER_HOME}/a.cpp

RUN clang++ -std=c++11 a.cpp
RUN ./a.out 
