package com.newage.persistenceservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.util.Map;
import java.util.UUID;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.signup-topic}")
    private String signUpTopic;

    @Value("${kafka.reply-topic}")
    private String replyTopic;

    @Value("${kafka.reply-group}")
    private String replyGroup;
    @Value("${kafka.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${kafka.enable-autocommit}")
    private boolean enableAutoCommit;

//    @Autowired
//    private EntityManagerFactory entityManagerFactory;

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Object, Object>> kafkaJsonListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setReplyTemplate(kafkaTemplate());
//        factory.getContainerProperties().setTransactionManager(kafkaTransactionManager());
        factory.setMessageConverter(new StringJsonMessageConverter());
        return factory;
    }

    @Bean
    public ConsumerFactory<Object, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    @Bean
    public KafkaTemplate<Object, Object> kafkaTemplate() {
        var template = new KafkaTemplate<>(producerFactory());
        template.setProducerListener(new KafkaProducerListener<>());

        return template;
    }

    @Bean
    public ProducerFactory<Object, Object> producerFactory() {
        DefaultKafkaProducerFactory<Object, Object> producerFactory = new DefaultKafkaProducerFactory<>(producerConfig());
//        producerFactory.setTransactionIdPrefix(String.format("persist-service:%s", UUID.randomUUID().toString()));
        return producerFactory;
    }

//    @Bean
//    public KafkaTransactionManager<Object, Object> kafkaTransactionManager() {
//        var ktm = new KafkaTransactionManager<>(producerFactory());
//        ktm.setTransactionSynchronization(AbstractPlatformTransactionManager.SYNCHRONIZATION_ON_ACTUAL_TRANSACTION);
//        return ktm;
//    }
//
//    @Bean
//    @Primary
//    public JpaTransactionManager transactionManager() {
//        return new JpaTransactionManager(entityManagerFactory);
//    }
//
//    @Bean(name = "chainedTransactionManager")
//    public ChainedTransactionManager chainedTransactionManager() {
//        return new ChainedTransactionManager(kafkaTransactionManager(), transactionManager());
//    }

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
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset,
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit,
                ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed"
        );
    }
}
