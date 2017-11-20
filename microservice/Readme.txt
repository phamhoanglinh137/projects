MicroService

1. Tech stack.
  ![Alt text](rhttps://cdn-images-1.medium.com/max/1600/1*PQgn_cxo9sohjEf5Kwm6Tg.png)

- Spring Cloud : spring eureka discovery/registry, feign, hytrix, dashboard, spring config, spring auth services and security.
- Apache kafka
- ELK (elasticsearch, logstask, kibana) for centralized logging
- Docker

2. How to install.

2.1 install Elasticsearch
 https://www.elastic.co/guide/en/elasticsearch/reference/current/docker.html

 - healthcheck api
 curl -XGET http://localhost:9200/_cluster/health?pretty=true
