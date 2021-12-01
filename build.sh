#!/bin/bash

app_name="docker-idle-start-stopper"

mvn clean package

rm ./$app_name.tar.gz
rm -rf ./$napp_ame
mkdir ./$app_name

cp ./target/$app_name-*-shaded.jar ./$app_name/diss.jar
cp ./install.sh ./$app_name/install.sh

rm ./$app_name.tar.gz
tar -czvf $app_name.tar.gz $app_name
rm -rf ./$app_name
rm -rf ./target