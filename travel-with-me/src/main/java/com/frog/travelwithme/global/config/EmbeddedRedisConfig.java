package com.frog.travelwithme.global.config;

import com.frog.travelwithme.global.exception.BusinessLogicException;
import com.frog.travelwithme.global.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * EmbeddedRedisConfig 설명: 내장 Redis가 profile=local일 때만 실행되도록 설정
 * 작성자: 김찬빈
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/08
 **/
@Slf4j
@Configuration
public class EmbeddedRedisConfig {

    @Value("${spring.redis.port}")
    private int redisPort;
    @Value("${spring.redis.maxmemory}")
    private String redisMaxMemory;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() throws IOException {
        int port = isRedisRunning() ? findAvailablePort() : redisPort;
        if (isArmArchitecture()) {
            log.info("ARM Architecture");
            redisServer = new RedisServer(Objects.requireNonNull(getRedisServerExecutable()), port);
        } else {
            redisServer = RedisServer.builder()
                    .port(port)
                    .setting("maxmemory " + redisMaxMemory)
                    .build();
        }
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }

    public int findAvailablePort() throws IOException {
        for (int port = 10000; port <= 65535; port++) {
            Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }

        throw new BusinessLogicException(ExceptionCode.NOT_FOUND_AVAILABLE_PORT);
    }

    /**
     * Embedded Redis가 현재 실행중인지 확인
     */
    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(redisPort));
    }

    /**
     * 해당 port를 사용중인 프로세스를 확인하는 sh 실행
     */
    private Process executeGrepProcessCommand(int redisPort) throws IOException {
        String command = String.format("netstat -nat | grep LISTEN|grep %d", redisPort);
        String[] shell = {"/bin/sh", "-c", command};

        return Runtime.getRuntime().exec(shell);
    }

    /**
     * 해당 Process가 현재 실행중인지 확인
     */
    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }
        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.ERROR_EXECUTING_EMBEDDED_REDIS);
        }
        return StringUtils.hasText(pidInfo.toString());
    }

    private File getRedisServerExecutable() {
        try {
            // return new File("src/main/resources/binary/redis/redis-server-linux-arm64-arc"); // TODO : 서버 배포후 삭제
            return new File("src/main/resources/binary/redis/redis-server-6.2.5-mac-arm64");
        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.REDIS_SERVER_EXCUTABLE_NOT_FOUND);
        }
    }

    private boolean isArmArchitecture() {
        return System.getProperty("os.arch").contains("aarch64");
    }
}
