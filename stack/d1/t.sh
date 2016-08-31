#!/bin/zsh
. ~/.sh.d/stack.sh
#setopt aliases
# https://github.com/jarmo/expand-aliases-oh-my-zsh
#which stack

stack build
stack exec d1-exe -- -o d1.svg -w 400
