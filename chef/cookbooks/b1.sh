#! /bin/bash
set -e
CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. $CUR_DIR/env-vars.sh

#RECIPE=-r 'recipe[s1]'
RECIPE='recipe[cb-template-service]'

knife bootstrap $FQDN -N epbygomw0024t2 --ssh-user 'vagrant' --ssh-password 'vagrant' --sudo -r $RECIPE -E local-iharh
#--use-sudo-password
#-E --environment ENV
