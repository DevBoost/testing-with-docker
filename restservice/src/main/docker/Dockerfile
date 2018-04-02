FROM openjdk:8u151-jdk

RUN apt-get update -y && apt-get install -y \
		htop \
		less \
		mc \
		mysql-client \
		net-tools \
		telnet \
		tcpdump \
		vim \
	&& apt-get clean

ENV TERM=xterm

RUN mkdir -p /application/libs
COPY external-libs/* /application/libs/
COPY modules/* /application/libs/
ENTRYPOINT ["java", "-cp", "/application/libs/*", "de.devboost.dockertesting.restservice.SimpleRestApplication"]
