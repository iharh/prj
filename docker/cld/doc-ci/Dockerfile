FROM centos:7
# 7 - is the latest one like: centos7.2.1511

RUN yum -y update
RUN yum -y install sudo which vim curl tar

# Edit sudoers file
# To avoid error: sudo: sorry, you must have a tty to run sudo
RUN sed -i -e "s/Defaults    requiretty.*/Defaults    !requiretty/g" /etc/sudoers

RUN yum -y install gcc gcc-c++

#
# epel (needed for vcs)
#
RUN yum -y install epel-release

#
# vcs
#
RUN rpm --import http://opensource.wandisco.com/RPM-GPG-KEY-WANdisco 

RUN echo $'[WandiscoSVN]\n\
name=Wandisco SVN Repo\n\
baseurl=http://opensource.wandisco.com/centos/$releasever/svn-1.9/RPMS/$basearch/\n\
enabled=1\n\
gpgcheck=1\n\
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-WANdisco'\
>> /etc/yum.repos.d/wandisco-svn.repo
#gpgkey=http://opensource.wandisco.com/RPM-GPG-KEY-WANdisco

RUN yum -y install subversion.x86_64

RUN echo $'[WandiscoGIT]\n\
name=Wandisco GIT Repo\n\
baseurl=http://opensource.wandisco.com/centos/$releasever/git/$basearch/\n\
enabled=1\n\
gpgcheck=1\n\
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-WANdisco'\
>> /etc/yum.repos.d/wandisco-git.repo

RUN yum -y install git

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
# volumes
#
#"${USER_HOME}/.gradle"\
VOLUME  [\
  "/prj"\
]
