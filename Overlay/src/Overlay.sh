#!/usr/bin/env bash

RED='\033[0;31m'
NC='\033[0m' # No Color

mFile="${HOME}/.ssh/randomLab"

if [ "$TMUX" == "" ];then
	echo must run from tmux. >&2
	exit 1
fi

if [ $# -ne 1 ];then
	echo "Usage: $0 <#>" >&2
	exit 1
fi

read -p "Proceed with changing tmux layout? [yes or no]? " yn
case $yn in
	[Yy]* ) ;;
	* ) echo "Will now exit."; exit 1;;
esac

if ! [ -f $mFile ]; then
	echo -e "[ $RED ERROR $NC ] server list file not found. Will make one for you." >&2
	touch $mFile
	wget -q "https://www.cs.colostate.edu/~info/machines" -O - | grep "120" | head -n50 | awk -F"\t" '{ print $1".cs.colostate.edu" }' | shuf -n50 > $mFile
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
