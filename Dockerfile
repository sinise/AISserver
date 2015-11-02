FROM java:7

RUN mkdir -p /AISserver
COPY . /AISserver
WORKDIR /AISserver
RUN javac *.java

