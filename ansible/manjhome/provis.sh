#! /bin/sh
pb_env=cpsprod
pb_usr=ihar.hancharenka
pb_new_usr=prediction
pb_hosts=all

ansible-playbook -vvvv -i env/$pb_env/inventory pb-site-full.yml -e "hosts=$pb_hosts user=$pb_usr new_user=$pb_new_usr" -e @env/$pb_env/extra-vars.json
