const neopixel = require("neopixel");

var wifi = require("Wifi");


function setColour(red, green, blue, white) {
  let data = [];
  for (var i=0; i< 7; i++) {
    data.push(green, red, blue, white);
  }
  // because library is special
  data.push(0, 0);
  neopixel.write(B15, data);
}

function onInit(){
  setColour(255, 0, 0, 0);


  wifi.connect("O2 Wifi", {}, function(err){
    console.log("connected:", err);
     setColour(0, 255, 0, 0);

    wifi.getIP(function(err, ipinfo){
      console.log(ipinfo);
    });

    var mqtt = require("MQTT").connect({
      host: "10.204.82.73",
    });
    mqtt.subscribe("test/espruino");

    mqtt.on('publish', function (pub) {
      // trigger stuff here
      console.log("topic: "+pub.topic);
      console.log("message: "+pub.message);
      setColour(0, 0, 255, 0);
    });


  });
}

onInit();