#!/bin/bash

echo "Waiting for mysql"
until mysql -h"database" -P"3306" -uroot -p"root" &> /dev/null
do
  printf "."
  sleep 1
done

echo -e "\nmysql ready"
