#! /bin/sh
ansible-playbook -i inventories/ihhome pb-dbg.yml -e "host=all user=iharh"
# -v -vv -vvv
