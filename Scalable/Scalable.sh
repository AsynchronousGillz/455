#!/usr/bin/env bash

RED='\033[0;31m'
NC='\033[0m' # No Color

mFile="${HOME}/.ssh/randomLab"

javaKill() {
	echo -en "Host: "
	for i in `seq 1 $HOST`; do
		machine=$(sed -n "${i}p" $mFile)
		echo -en "$machine "
		ssh $machine "pkill java > /dev/null 2>&1"
	done
echo ""
}

# trap ctrl-c and call ctrl_c()
trap ctrl_c INT

function ctrl_c() {
	echo "No"
}

if [ $# -ne 3 ];then
	echo -e "Usage: $0 <#> <#>" >&2
	echo -e "\t <#> Number of Hosts" >&2
	echo -e "\t <#> Number of Processes" >&2
	echo -e "\t <#> Number of Server Threads" >&2
	exit 1
fi

HOST="$1"
NUM="$2"
THREAD_NUM="$2"

if ! [ -f $mFile ]; then
	echo -e "[ $RED ERROR $NC ] server list file not found. Will make one for you." >&2
	touch $mFile
	wget -q "https://www.cs.colostate.edu/~info/machines" -O - | grep "120" | head -n50 | awk -F"\t" '{ print $1".cs.colostate.edu" }' | shuf -n50 > $mFile
fi

read -p "Proceed with run config? [yes or no]? " yn
case $yn in
	[Yy]* ) ;;
	* ) echo "Will now exit."; exit 1;;
esac

pkill java* > /dev/null 2>&1
javaKill

DIR=$(pwd)
HOSTNAME=$(hostname)

C="cd $DIR; java cs455.scaling.client.Client ${HOSTNAME} 60100 5"

java cs455.scaling.server.Server 60100 $THREAD_NUM > /tmp/${USER}_$$_server.log &

SERVER_PID=$!

# Login and kick up all messaging nodes
echo -en "Host: "
for i in $(seq 1 $HOST); do
	machine=$(sed -n "${i}p" $mFile)
	echo -en "$machine "
	for k in $(seq 1 $NUM); do
		ssh $machine "$C > /tmp/${USER}_${k}_${i}.log &"
	done
done
echo ""

TOTAL=80

for i in $(seq 1 $TOTAL); do
	echo -n "[running] ["
	for k in $(seq 1 $i); do
		echo -n "#"
	done
	for k in $(seq $i $TOTAL); do
		echo -n "-"
	done
	echo -ne "]\r"
	sleep 1
done
echo ""

kill $SERVER_PID

read -p "Proceed with removal of run files? [y]? " yn
case $yn in
	* ) ;;
esac

rm -v /tmp/${USER}*
C="rm -v /tmp/${USER}*"

echo -en "Host: "
for i in $(seq 1 $HOST); do
	machine=$(sed -n "${i}p" $mFile)
	echo -en "$machine "
	echo -e $(ssh $machine "$C")
done
echo ""

javaKill
