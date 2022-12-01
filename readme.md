This is a sample sprintboot jms application, which will read messages from one queue and write to another queue
IBMMQ will be running in one container and java jms application will run in another container and both container will interact with each other in docker network.

Prerequisites :-install docker desktop, maven and InteliJ

step1:-clone this repo to your local
buld application jar file using below command or via Injelij
mvn clean install

step2:-go to the project in your local directory, open command prompt and nevigate to project directory where .Dockerfile is present
build jmsmq image by using command
docker build -t jmsmq .
Now you should see a image jmsmq is created under image section of your docker desktop

step3:- Build a docker mq image and run mq container in developermode, this does need a licence 
Use Dockerfile in folder ibmmq (inside your project )to build image using below command,
docker build -t ibmmq .

step4:-
Run the container using below command
docker run --env LICENSE=accept --env MQ_QMGR_NAME=QM1 --publish 1414:1414 --publish 9443:9443 --detach --env MQ_APP_PASSWORD=passw0rd --name QM1 ibmmq
Access the MQ console URL via
https://localhost:9443/ibmmq/console
username:-admin
password:-passw0rd
This for testing only , for now just stop this container , we will start this later with docker compose using services

step5:-Now both images are available in docekr desktop to run
in command prompt go to project directory which you have cloned above.

Start both container using below command
docker-compose -f jmsmq.yml up -d

step6:-You can see both container running sucessfully

step7:- to test login to IBMMQ console using https://localhost:9443/ibmmq/console
username:-admin
password:-passw0rd
put message in ORDER.REQUEST queue , you should immediately see message set sucessfully to ORDER.RESPONSE queue.

Note:-We have used docker volumes to map host file system to container file system , so application logs are synced outside container and wse can see log files in text
below is your windows system path for applicaiton log \\wsl$\docker-desktop-data\version-pack-data\community\docker\volumes
you can also login to container to check the logs from /appl/springjms/log folder, just login to container in interactive mode with below command
docker exec -it jmsmq