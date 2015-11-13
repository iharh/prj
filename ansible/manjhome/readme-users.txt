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



