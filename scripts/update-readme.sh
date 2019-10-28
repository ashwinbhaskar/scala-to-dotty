#!/bin/sh
pwd=$PWD
cd scripts
python3 generate-md.py
cd $pwd
