#!/usr/bin/env bash

# Login and kick up all messaging nodes
for i in `seq 1 10`; do
	tmux splitw "ssh $(rSSH)"
	tmux select-layout even-vertical
done

# Makes all the terminals share the same input
tmux set-window-option synchronize-panes on

# Otherwise the last pane will still be local
ssh $(rSSH)
