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
  setColour(255, 200, 0, 0);
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

function rainbow() {
  // Produce an animated rainbow
  var rgb = new Uint8ClampedArray(7 * 3);
  console.log(rgb);
  var pos = 0;
  function getPattern() {
    pos++;
    for (var i=0;i<rgb.length;) {
      rgb[i++] = (1 + Math.sin((i+pos)*0.1324)) * 127;
      rgb[i++] = (1 + Math.sin((i+pos)*0.1654)) * 127;
      rgb[i++] = (1 + Math.sin((i+pos)*0.1)) * 127;
      rgb[i++] = 0;
    }
    rgb[i++] = 0;
    rgb[i++] = 0;
    return rgb;
  }
  setInterval(function() {
    require("neopixel").write(B15, getPattern());
  }, 100);
}
  

var wifiInitialised = false;
var busInitialised = false;
var flashOn = false;
var flasher;

function flash() {
  return setInterval(function() {
    flashOn = !flashOn;
    if (flashOn) {
     blue();
    } else {
     off();
    }
  }, 500);
}

E.on('init', function() {
  red();
  wifi.connect("O2 Wifi", {}, function(err){
    wifiInitialised = true;
    console.log("connected:", err);
     amber();

    wifi.getIP(function(err, ipinfo){
      console.log(ipinfo);
    });

    var mqtt = require("MQTT").connect({
      host: "ec2-34-243-3-198.eu-west-1.compute.amazonaws.com",
    });

    mqtt.on('connected', function () {
      mqtt.subscribe("test/espruino");
      blue();
    });

    mqtt.on('publish', function (pub) {
      // trigger stuff here
      clearInterval();
      console.log("topic: "+pub.topic);
      console.log("message: "+pub.message);
      var message = JSON.parse(pub.message);
      var status = message.status;
      console.log(status);
      switch (status) {
        case 'BUILDING':
          flasher = flash();
          //rainbow();
          break;
        case 'SUCCESS':
          green();
          break;
        case 'FAIL':
        default:
          red();
          break;
      }
    });


  });
});
