#! /bin/sh
ansible-playbook -vvvv -i inventories/cpsprod pb-dbg.yml -e "host=all user=prediction"
# user=ihar.hancharenka also works
