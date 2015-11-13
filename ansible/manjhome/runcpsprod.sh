#! /bin/sh
ansible-playbook -vvvv -i inventories/cpsprod pb-site-full.yml -e "hosts=all user=ihar.hancharenka"
