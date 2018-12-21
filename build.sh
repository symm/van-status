cd client
echo "doing build"
LD_LIBRARY_PATH=. ./mosquitto_pub -h ec2-34-243-3-198.eu-west-1.compute.amazonaws.com -m '{"status":"BUILDING"}' -t test/espruino
sleep 5
echo "done build"
