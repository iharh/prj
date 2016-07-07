#! /bin/bash
set -e
CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. $CUR_DIR/env-vars.sh

knife bootstrap $FQDN -N t2 --ssh-user 'vagrant' --ssh-password 'vagrant' --sudo
#--use-sudo-password
