FROM openjdk:11
RUN mkdir -p /appl/springjms/log
RUN groupadd -r santanu && useradd -r -g santanu santanu
COPY target/*.jar /appl/springjms/spring_jms.jar
WORKDIR /appl
RUN chown -R santanu:santanu /appl
RUN chmod 766 /appl
USER santanu
CMD ["java", "-jar", "/appl/springjms/spring_jms.jar"]