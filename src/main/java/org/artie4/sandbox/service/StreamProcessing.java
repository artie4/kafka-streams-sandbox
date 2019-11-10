package org.artie4.sandbox.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Properties;

@Service
@Slf4j
public class StreamProcessing {

	private final StreamsBuilder streamsBuilder;
	private final Properties properties;
	@Value("${application.kafka.topic.input}")
	public String startTopic;
	@Value("${application.kafka.topic.output}")
	public String finishTopic;

	public StreamProcessing(@Qualifier("simpleStreamsBuilder") StreamsBuilder streamsBuilder,
	                        @Qualifier("streamProperties") Properties properties) {
		this.streamsBuilder = streamsBuilder;
		this.properties = properties;
	}

	@PostConstruct
	public void kStreamJson() {
		KStream<String, Map> kStream = streamsBuilder.stream(startTopic);
		kStream.peek((k, v) -> {
			log.info(v.toString());
		}).map((k, v) -> {
			if (v != null) v.put("readable", true);
			return KeyValue.pair(k, v);
		}).to(finishTopic);

		KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), properties);
		kafkaStreams.start();
	}
}
