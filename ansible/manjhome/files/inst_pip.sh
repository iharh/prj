#! /bin/bash
source ~/miniconda/bin/activate $(whoami)
pip install -r ~/pip-requirements.txt
# this fails if we pass some parameters to this shell-script, so - can't use "$@" here
source deactivate
