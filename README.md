[Ref docs](https://www.espruino.com/Reference#software)

[MQTT client](https://www.espruino.com/MQTT#line=35,39,52,53)


## Bus

run `docker-compose up` to spin up a bus

The docker container does not contain the `mosquitto_sub` and `mosquitto_pub` binaries.
It's hackday so lets install them locally instead:

```
brew install mosquitto
```

Subscribe to Topic:

```
mosquitto_sub -h 10.204.82.73 -t test/espruino
```

Publish message:

```
mosquitto_pub -h 10.204.82.73 -m "chicken" -t test/espruino
```
