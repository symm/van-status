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

function red() {
  setColour(255, 0, 0, 0);
}

function amber() {
  setColour(200, 200, 0, 0);
}

function blue() {
  setColour(0, 50, 200, 0);
}

function green() {
  setColour(0, 255, 0, 0);
}

function off() {
  setColour(0, 0, 0, 0);
}

function sleep (time) {
  return new Promise((resolve) => setTimeout(resolve, time));
}

var wifiInitialised = false;
var busInitialised = false;

function onInit(){
  red();
  wifi.connect("O2 Wifi", {}, function(err){
    wifiInitialised = true;
    console.log("connected:", err);
     amber();

    wifi.getIP(function(err, ipinfo){
      console.log(ipinfo);
    });

    var mqtt = require("MQTT").connect({
      host: "10.204.82.73",
    });
    mqtt.subscribe("test/espruino");
    blue();

    mqtt.on('publish', function (pub) {
      // trigger stuff here
      console.log("topic: "+pub.topic);
      console.log("message: "+pub.message);
     // var status = 'FAILED';
      var status = 'BUILDING';
      //var status = 'SUCCESS';
      // var status = pub.message.status;
      switch (status) {
        case 'building':
          amber();
          break;
        case 'passed':
          green();
          break;
        case 'failed':
        default:
          red();
          break;
      }
    });


  });
}

onInit();