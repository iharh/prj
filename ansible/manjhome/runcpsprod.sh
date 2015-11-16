#! /bin/sh
ansible-playbook -vvvv -i env/cpsprod/inventory pb-site-full.yml -e "hosts=all user=ihar.hancharenka" -e "@env/cpsprod/extra-vars.json"
