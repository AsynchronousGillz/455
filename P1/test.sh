#!/usr/bin/env bash

# Select nodes, info in ~info/machines
messaging_nodes="earth jupiter mars mercury neptune saturn uranus venus raleigh"

# Send tarball over to the correct place
make tarball && scp cs455_proj1.tar.bz2 con:cs455/assignment_one

# Login and kick up all messaging nodes
for host in $messaging_nodes; do
  tmux splitw "ssh con@${host}.cs.colostate.edu"
  tmux select-layout even-vertical
done

# Makes all the terminals share the same input
tmux set-window-option synchronize-panes on

# Otherwise the last pane will still be local
ssh con@denver.cs.colostate.edu
