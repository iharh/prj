#! /bin/sh
pb_env=cpsprod
pb_usr=ihar.hancharenka
pb_hosts=all

ansible-playbook -vvvv -i env/$pb_env/inventory pb-site-full.yml -e "hosts=$pb_hosts user=$pb_usr" -e @env/$pb_env/extra-vars.json
