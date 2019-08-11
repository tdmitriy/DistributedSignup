package com.newage.signupservice.config;

import com.newage.signupservice.model.event.PlayerPersistenceEvent;
import com.newage.signupservice.model.event.PlayerSignUpEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${kafka.reply-topic}")
    private String replyTopic;

    @Value("${kafka.reply-group}")
    private String replyGroup;

    @Value("${kafka.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${kafka.isolation-level}")
    private String isolationLevel;

    @Value("${kafka.enable-autocommit}")
    private boolean enableAutoCommit;

    @Bean
    public ReplyingKafkaTemplate<String, PlayerSignUpEvent, PlayerPersistenceEvent> replyKafkaTemplate(
            ProducerFactory<String, PlayerSignUpEvent> pf, KafkaMessageListenerContainer<String, PlayerPersistenceEvent> container) {
        var replyingKafkaTemplate = new ReplyingKafkaTemplate<>(pf, container);
        replyingKafkaTemplate.setProducerListener(new KafkaProducerListener<>());

        return replyingKafkaTemplate;
    }

    @Bean
    public ProducerFactory<String, PlayerSignUpEvent> producerFactory() {
        DefaultKafkaProducerFactory<String, PlayerSignUpEvent> producerFactory = new DefaultKafkaProducerFactory<>(producerConfig());
        producerFactory.setValueSerializer(new JsonSerializer<>());

        return producerFactory;
    }

    @Bean
    public KafkaMessageListenerContainer<String, PlayerPersistenceEvent> replyContainer(ConsumerFactory<String, PlayerPersistenceEvent> cf) {
        ContainerProperties containerProperties = new ContainerProperties(replyTopic);

        return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }

    @Bean
    public ConsumerFactory<String, PlayerPersistenceEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(), new JsonDeserializer<>(PlayerPersistenceEvent.class, false));
    }

    @Bean
    public Map<String, Object> producerConfig() {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
    }

    @Bean
    public Map<String, Object> consumerConfig() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, replyGroup,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset,
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit,
                ConsumerConfig.ISOLATION_LEVEL_CONFIG, isolationLevel
        );
    }
}
