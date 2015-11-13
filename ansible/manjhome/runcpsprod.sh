#! /bin/sh
ansible-playbook -vvvv -i inventories/cpsprod pb-dbg.yml -e "host=all user=ihar.hancharenka"
#user=prediction
# user=ihar.hancharenka also works
