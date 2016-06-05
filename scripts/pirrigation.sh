#!/bin/sh
cd /usr/local/pirrigation/
nohup java -jar pirrigation.jar&
echo $! > /var/run/pirrigation.pid