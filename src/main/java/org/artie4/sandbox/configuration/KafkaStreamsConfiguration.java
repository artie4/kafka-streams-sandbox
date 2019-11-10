package org.artie4.sandbox.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.Properties;

@Configuration
@EnableKafka
@Slf4j
public class KafkaStreamsConfiguration {

	private final KafkaProperties kafkaProperties;

	public KafkaStreamsConfiguration(KafkaProperties kafkaProperties) {
		this.kafkaProperties = kafkaProperties;
	}

	@Bean(name = "streamProperties")
	public Properties properties() {
		Properties properties = new Properties();
		properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
		properties.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaProperties.getStreams().getApplicationId());
		properties.put(StreamsConfig.CLIENT_ID_CONFIG, kafkaProperties.getClientId());
		properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class.getName());
		properties.put(JsonDeserializer.KEY_DEFAULT_TYPE, String.class);
		properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Object.class);
		properties.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);
		return properties;
	}

	@Bean(name = "simpleStreamsBuilder")
	public StreamsBuilder createStreamsBuilder() {
		return new StreamsBuilder();
	}
}
