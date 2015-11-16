#sudo: sorry, you must have a tty to run sudo
#  #sudo: true

#RUN adduser ${USER_NAME} && gpasswd -a ${USER_NAME} wheel

#- name: Create sudoers.bak
#  command: cp /etc/sudoers /etc/sudoers.bak              
#  # it is also possible to add backup=yes to lineinfile module, but file name is really ugly in that case

#- name: Copy sudoers for safety
#  command: cp /etc/sudoers /etc/sudoers.tmp

#  ## Same thing without a password
#  # %wheel        ALL=(ALL)       NOPASSWD: ALL
#- name: Ensure admin group is in sudoers with NOPASSWD
#  lineinfile: "dest=/etc/sudoers.tmp state=present regexp='^# %wheel' line='%wheel ALL=(ALL) NOPASSWD: ALL' validate='/usr/sbin/visudo -cf %s'"
#  register: sudoers_tmp_ok

#- name: Copy sudoers.tmp to sudoers
#  when: sudoers_tmp_ok|success
#  command: cp /etc/sudoers.tmp /etc/sudoers

https://gist.github.com/marktheunissen/2979474
      #get_url: url=http://download.oracle.com/otn-pub/java/jdk/8u5-b13/jdk-8u5-linux-x64.tar.gz dest="home/{{user}}/" headers='Cookie:oraclelicense=accept-securebackup-cookie'

curl -k

other jdk roles
	to skip cert check
    #- ansiblebit.oracle-java
    #- groover.java

    #util_epel_enable: false
    #util_action_become_enable: false
    #util_local_action_become_enable: false
    #util_local_action_sudo_enable: false
    ##util_action_become_user: ihar.hancharenka
    ##util_local_action_become_user: iharh
