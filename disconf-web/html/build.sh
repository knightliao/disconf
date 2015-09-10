#!/bin/bash

cd "${0%/*}"

#
# 打包FE包
#

#
if [ -d "output" ]; then
    printf '%s\n' "Removing output"
    rm -rf output
fi

mkdir -p output

cp -rp assets output
cp -rp dep output
cp -rp *.html output
