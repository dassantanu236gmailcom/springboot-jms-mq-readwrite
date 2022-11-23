step1:-install docker desktop

step2:- Build a docker mq image and run mq container
Use attached Dockerfile to build image using below command

docker build -t ibmmq .
Run the container using below command

docker run --env LICENSE=accept --env MQ_QMGR_NAME=QM1 --publish 1414:1414 --publish 9443:9443 --detach --env MQ_APP_PASSWORD=passw0rd --name QM1 ibmmq

Access the MQ console URL via

https://localhost:9443/ibmmq/console
username:-admin
password:-passw0rd

step3:- setup and buld jar to connect to Queue manager and read message from one queue and write to another queue