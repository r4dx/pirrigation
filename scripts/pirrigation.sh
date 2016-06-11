#!/bin/sh
cd /usr/local/pirrigation/
nohup java -Dlogging.config=conf/logback.xml -jar pirrigation.jar&
echo $! > /var/run/pirrigation.pid