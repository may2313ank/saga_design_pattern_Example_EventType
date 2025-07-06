package com.example.config;

import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.event.OrderEvent;


@EnableKafka
@Configuration
public class KafkaConfig {

	 @Value("${spring.kafka.bootstrap-servers}")
	    private String bootstrapServers;

	    @Bean 
	    NewTopic sagaTopic() {
	        return TopicBuilder.name("saga-topic").partitions(3).replicas(1).build();
	    }

	    @Bean 
	    NewTopic rollbackTopic() {
	        return TopicBuilder.name("saga-rollback").partitions(3).replicas(1).build();
	    }

	    @Bean 
	    ProducerFactory<String, OrderEvent> producerFactory() {
	        return new DefaultKafkaProducerFactory<>(Map.of(
	          ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
	          ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
	          ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
	        ));
	    }

	    @Bean 
	    KafkaTemplate<String, com.example.event.OrderEvent> kafkaTemplate() {
	        return new KafkaTemplate<>(producerFactory());
	    }

	    @Bean 
	    ConsumerFactory<String, OrderEvent> consumerFactory() {
	        var deserializer = new JsonDeserializer<>(OrderEvent.class);
	        deserializer.addTrustedPackages("com.common.event");
	        return new DefaultKafkaConsumerFactory<>(
	          Map.of(
	            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
	            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
	            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer,
	            ConsumerConfig.GROUP_ID_CONFIG, "saga-group"
	          ),
	          new StringDeserializer(), deserializer
	        );
	    }

	    @Bean 
	    ConcurrentKafkaListenerContainerFactory<String, OrderEvent> listenerFactory() {
	        var factory = new ConcurrentKafkaListenerContainerFactory<String, OrderEvent>();
	        factory.setConsumerFactory(consumerFactory());
	        return factory;
	    }
	}


