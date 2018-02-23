
# How to run

cd $KAFKA_HOME

======= KAFKA BASIC ( PRODUCER/ CONSUMER) =======
1. Start Zookeeper
./bin/zookeeper-server-start.sh ./config/zookeeper.properties

2. Start server (brokers)
./bin/kafka-server-start.sh ./config/server.properties

3. Start topic
./bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 2 --topic account_open

  Describe topic
./bin/kafka-topics.sh --zookeeper localhost:2181 --describe account_open


============== KAFKA CONNECT ==============

Two modes of execution:
- standalone (single process)
  bin/connect-standalone.sh config/connect-standalone.properties
- distributed  
  bin/connect-distributed.sh config/connect-distributed.properties
