#!/bin/bash
java -Xmx128m -jar -Dserver.port=8070 app/spring-boot-microservice-reservation-system/build/libs/spring-boot-microservice-reservation-system.jar &
java -Xmx128m -jar -Dserver.port=8060 app/spring-boot-microservice-library-system/build/libs/spring-boot-microservice-library-system.jar &
java -Xmx128m -jar -Dserver.port=8050 app/spring-boot-microservice-rating-system/build/libs/spring-boot-microservice-rating-system.jar &
java -Xmx128m -jar -Dserver.port=8080 app/spring-boot-microservice-gateway/build/libs/spring-boot-microservice-gateway.jar
