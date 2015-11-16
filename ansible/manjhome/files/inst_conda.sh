#! /bin/bash
source ~/miniconda/bin/activate $(whoami)
conda install -qy --no-update-deps --file ~/conda-requirements.txt
# this fails if we pass some parameters to this shell-script, so - can't use "$@" here
source deactivate
