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

setColour(255, 0, 0, 0);


wifi.connect("O2 Wifi", {}, function(err){
  console.log("connected:", err);
   setColour(0, 255, 0, 0);

  wifi.getIP(function(err, ipinfo){
    console.log(ipinfo);
  });

});