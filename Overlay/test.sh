#!/usr/bin/env bash

RED='\033[0;31m'
NC='\033[0m' # No Color

mFile="${HOME}/.ssh/randomLab"

if [ $TMUX -z ];then
	echo must run from tmux. >&2
	exit 1
fi

if [ $# -ne 1 ];then
	echo "Usage: $0 <#>" >&2
	exit 1
fi


if ! [ -f $mFile ]; then
	echo "[ $RED ERROR $NC ] server list file not found. Will make one for you." >&2
	wget -q "https://www.cs.colostate.edu/~info/machines" -O machines
	touch $mFile
	grep "120" machines | head -n50 | awk -F"\t" '{ print $1".cs.colostate.edu" }' | shuf -n50 > $mFile
	rm machines
fi
	
# Login and kick up all messaging nodes
for i in `seq 1 $1`; do
	machine=$(cat $mFile | shuf -n 1)
	tmux splitw "ssh $machine"
	tmux select-layout even-vertical
done

# Makes all the terminals share the same input
tmux set-window-option synchronize-panes on

# Otherwise the last pane will still be local
ssh $(cat $mFile | shuf -n 1)
