#!/bin/awk -f
#{ print }
#/[[:digit:]]{4}-[[:digit:]]{2}-[[:digit:]]{2}/
#/[[:digit:]]{4}-[[:digit:]]{2}-[[:digit:]]{2}/ { print $3 }
#$3 ~ /\[0x[[:xdigit:]]{8}\]/
/[[:digit:]]{4}-[[:digit:]]{2}-[[:digit:]]{2}/ && $3 ~ /\[0x[[:xdigit:]]{8}\]/ {
	tid = substr($3,4,length($3) - 4);
	print >> tid ".log";
}
