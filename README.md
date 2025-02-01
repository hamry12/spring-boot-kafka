# Kafka Setup and Configuration Guide

## Prerequisites
Ensure you have Kafka installed and properly configured on your system.

## Setup and Validate Kafka Installation

### Generate a Random UUID
Run the following command to generate a unique UUID:
```sh
./bin/kafka-storage.sh random-uuid
```
Example output:
```sh
vVEMcrX5TAOsr2giksO2yA
```

### Format the Server with Unique UUID
Use the generated UUID to format the Kafka metadata directory:
```sh
./bin/kafka-storage.sh format -t vVEMcrX5TAOsr2giksO2yA -c config/kraft/server.properties
```
Example output:
```sh
Formatting metadata directory /tmp/kraft-combined-logs with metadata.version 3.9-IV0.
```

### Start the Kafka Server
```sh
./bin/kafka-server-start.sh config/kraft/server.properties
```

## Setup Multiple Kafka Brokers
1. Create multiple `server.properties` files under the `config/kraft/` directory (e.g., `server-1.properties`, `server-2.properties`, etc.).
2. Modify the configuration files with appropriate details:
   - `node.id`
   - `logs.dir`
   - `controller.quorum.voters`
   - `listeners`
   - `advertised.listeners`
3. Generate a unique UUID for each broker:
   ```sh
   ./bin/kafka-storage.sh random-uuid
   ```
   Example output:
   ```sh
   hcwDDDvjQzCO4nZQj-WVDQ
   ```
4. Format each server with the generated UUID:
   ```sh
   ./bin/kafka-storage.sh format -t hcwDDDvjQzCO4nZQj-WVDQ -c config/kraft/server-1.properties
   ./bin/kafka-storage.sh format -t hcwDDDvjQzCO4nZQj-WVDQ -c config/kraft/server-2.properties
   ./bin/kafka-storage.sh format -t hcwDDDvjQzCO4nZQj-WVDQ -c config/kraft/server-3.properties
   ```

## Creating Kafka Topics
### Create a Topic
```sh
./bin/kafka-topics.sh --create --topic product-created-events-topic --partitions 3 --replication-factor 3 --bootstrap-server localhost:9092,localhost:9094,localhost:9096
```
Example output:
```sh
Created topic product-created-events-topic.
```

### Create a Topic with `min.insync.replicas`
```sh
./bin/kafka-topics.sh --create --topic topic3 --partitions 3 --replication-factor 3 --bootstrap-server localhost:9092,localhost:9094,localhost:9096 --config min.insync.replicas=2
```
Example output:
```sh
Created topic topic3.
```

## Listing and Describing Kafka Topics
### List Topics
```sh
./bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```
Example output:
```sh
topic1
topic2
```

### Describe Topics
```sh
./bin/kafka-topics.sh --describe --bootstrap-server localhost:9092
```
Example output:
```sh
Topic: topic1	PartitionCount: 3	ReplicationFactor: 3
Topic: topic2	PartitionCount: 3	ReplicationFactor: 3
```

## Deleting Kafka Topics
```sh
./bin/kafka-topics.sh --delete --topic topic1 --bootstrap-server localhost:9092
./bin/kafka-topics.sh --delete --topic topic2 --bootstrap-server localhost:9092
```

## Kafka Producer Setup
```sh
./bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic product-created-events-topic --property "parse.key=true" --propert
y "key.separator=:"
```

## Kafka Consumer Setup
### Basic Consumer Command
```sh
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic product-created-events-topic
```

### Create Consumer Group
Specify a group ID when consuming messages.

## Acknowledgment and Retries
### Application Properties
Configure appropriate acknowledgment (`acks`) and retry properties in the application configuration.

### Java Configuration
Use Java-based configuration beans to fine-tune producer settings.

## Idempotent Producer Configuration
1. Set `max.in.flight.requests.per.connection` to a value no greater than 5.
2. Set `acks=all` for reliable message delivery.
3. If using retries, set it to a high value for resilience.

---

This guide covers the essential steps for setting up and managing a Kafka cluster, creating topics, and configuring producers and consumers. Modify configurations based on your specific requirements for optimal performance and reliability.

