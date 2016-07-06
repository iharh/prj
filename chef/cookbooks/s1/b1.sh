#! /bin/bash
knife bootstrap -N t2 192.168.235.50 --ssh-user vagrant --ssh-password 'vagrant' --sudo -r 'recipe[s1::default]'
#--sudo --use-sudo-password
