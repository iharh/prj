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

    #- name: copy oracle java role
    #  copy: src="files/williamyeh-oracle-java.tar.gz" dest="/home/{{user}}/williamyeh-oracle-java.tar.gz" owner="{{user}}" group="{{user}}"
    ## mode=0644

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


williamyeah-jdk:

    - name: set internal vars for 1.8.0_65
      set_fact:
        jdk_version:      1.8.0_65
        jdk_tarball_file: jdk-8u65-linux-x64
        jdk_tarball_url:  http://download.oracle.com/otn-pub/java/jdk/8u65-b17/jdk-8u65-linux-x64
      when: java_version == 8 and java_subversion == 65

    - name: get JDK tarball (as RPM file)
      command: curl -L -H 'Cookie:oraclelicense=accept-securebackup-cookie'  -o {{ jdk_tarball_file }}.rpm  {{ jdk_tarball_url }}.rpm
      args:
        creates: "{{ java_download_path }}/jdk-tarball-{{ jdk_version }}"
        chdir:   "{{ java_download_path }}"
