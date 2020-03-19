#!/bin/sh

dir="png$1px"
mkdir -p $dir

for file in svg/*.svg;
do
	name="$dir/`basename $file svg`png"
	echo "$file -> $name"
	inkscape -z -e $name -w $1 $file
done
