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
mosquitto_sub -h ec2-34-243-3-198.eu-west-1.compute.amazonaws.com -t test/espruino
```

Publish message:

```
mosquitto_pub -h ec2-34-243-3-198.eu-west-1.compute.amazonaws.com -m "chicken" -t test/espruino
```


## Docker image

docker build -t van-queue .
docker tag van-queue:latest 636189516019.dkr.ecr.eu-west-1.amazonaws.com/van-queue:latest
docker push 636189516019.dkr.ecr.eu-west-1.amazonaws.com/van-queue:latest


