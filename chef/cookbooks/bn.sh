#! /bin/bash
knife bootstrap 192.168.235.55 -N t2 --ssh-user 'vagrant' --ssh-password 'vagrant' --sudo
#-r 'recipe[cb-template-service]'
#--sudo --use-sudo-password
