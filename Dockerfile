FROM eclipse-mosquitto:1.5.5

ADD mosquitto/config /mosquitto/config

EXPOSE 9001
EXPOSE 1883

