# spring-docker-101



Build small focused microservices – flexibility to innovate and build application in different languages

Common way to deploy multiple microservices not depending language or framework. One way of deploying by using containers

Docker and docker compose for containerization

Create docker image for each microservices(contains application runtime, application code and dependencies). 

Install docker

Virtual machines use hardware, hostos, hypervisor, guest os 1, software 1, application 1 etc.

 Docker – cloudinfrastructure, hostos, dockerengine, containers


 Old approach – premiase – hardware – install linux – install java and tomcat , download jar y run command.  Docker automate all this.


 Docker command

 Docker –-version

 Docker run in28min/todo-rest-api-h2:1.0.0.RELEASE

 VERSION

 1.0.0.RELEASE



 When cannot find in local its search on the docker hub and pull – image Is downloader

 Docker hub is registry – contains a lot of repositories, different version of different application

 Docker hub is registry

 Registry – hub.docker.com.  and contain number of repositories

 Entepraise work private repository

 Repository -  in28min/todo-rest-api-h2

 Container contains library, java version

 Image(static) -static – bytes

 Container(dynamic) – running version of your image

 Image is downloader in repository and is like a class and container is like an object




 To run correctly :

  Docker run -p 5000:5000 in28min/todo-rest-api-h2:1.0.0.RELEASE

 Host system port: internal container port

 By default the container is part of something called a bridge network(internal docker network) , no body can access unless you expose it on to the host.

 http://localhost:5000/hello-world
 http://localhost:5000/hello-world-bean


 -d -> detach mode – run in background – you can ctrl c to exit and you have container id

 Docker run -p 5000:5000 -d in28min/todo-rest-api-h2:1.0.0.RELEASE

 You can see logs

 Docker logs 2e07d5f53761dfc2689453f8e21a2947e623a58c138d49072f78ab06774e8e6d

 -f -> tailling logs , when you click on the web page you can see what happen in the log

 Docker logs -f 2e07d5f53761dfc2689453f8e21a2947e623a58c138d49072f78ab06774e8e6d

 To see what container is running

 Docker container ls

 We can run multiple container from same image in different ports

 Docker run -p 5001:5000 -d in28min/todo-rest-api-h2:1.0.0.RELEASE

 Docker images – command to see all docker images we have
 docker container ls -a -> command show me all the containers – running and stopped

 docker container stop cc34a(container id of first 5 digits) -> stop the continers

 docker app has docker client and docker daemon. Docker daemon manage the containers, local images and image registry(somebody else can use, like repository)(nginx, mysql,eureka, you-app)

 docker app knows the location of the local images, if not is going to download docker hub.

  ![image](https://user-images.githubusercontent.com/25869911/121826164-fea1ea80-cc7b-11eb-8c14-aacce7fecea0.png)


 Enviroments is in cloud, docker can installed in cloud.

 Virtual machines – hardware- host os – hypervisor(to manage virtual machines)  -> has guest os, software and application1.   Disvantage – heavy

 Docker – cloudinfrastruture, hostOS. And docker engine(install the os, etc) -> advantage is not heavier.


 AWS – elastic container service

 Single image can have multiple tag – custom tag

 Docker tag  in28min/todo-rest-api-h2:1.0.0.RELEASE in28min/todo-rest-api-h2:1.0.0.latest


 For pull images from docker hub

 Docker pull mysql   -> this going to pull latest tag , can be not most latest

 Mysql is offitial image

 docker search mysql  -> to search all images that cotains name mysql 

 ofitial images – has meeting certain standard

 image for java or tomcat – use offitial image


 docker image history joij98(image id).  -> to see the history

 to see the numbers or process(steps to create) of instructions -> shell commands


 docker image inspect c0cdc95609f1.  -> you can see in json configuration like version, what java run , etc, meta data.


 Docker image remove c0cdc95609f1 -> remove image from local machine


 Containers – 

 You can run container with docker container run

 You can pause and unpause container

 Docker container pause 6478.  – first 4 digits of the id container.  – pause the run container

 Docker container logs -f 6478 ->.  You can see the run state iner container

 Docker container unpause 6478.  – for run again



 Docker container inspect 6478 – json – you can see metadata about container, current state, platform, volume. 


 Docker container prune-> delete all container that is stopped


 Stop command is for graceful shutdown -> signal is send like SIGTERM

 Docker container stop 6478 -> if you are using docker container logs -f dockerId CAN see how to stopped.
 Docker container kill 6478 -> if you are using docker container logs -f dockerId CAN see stop instantly.  Kill send SIGKILL


 -	Restart policy ->  always(when you docker client restart, this run automatically), no(default),  database always running, daemon restart by accident,

 docker container run -p 5000:5000 -d --restart=always in28min/todo-rest-api-h2:0.0.1-SNAPSHOT



 docker commands –

 docker events – can see what events is happening, when some commands is executing. What happening in background

 docker top cfe23 – use to check the used to check what is the top process which is running in a specific container – what is the process is running – to see the specific container id

 docker stats – show all the states of running containers , like memory and cpu usage etc.

 you can run container with memory and cpu limits. Maximum of 512 megabyte of memory.
 Cpu Quota is 100000 = 100 % so 5000 is 5% 

 docker container run -p 5001:5000 -m 512m --cpu-quota 5000 -d in28min/todo-rest-api-h2:0.0.1-SNAPSHOT


 docker system df   -  see what docker daemon manages like images, containers, local volumnes and build cache


 Distritubted tracing(complex call chain when the microservices depends another microservices)

 How do you debug problems, how do you trace requests across microservices, what microservices is causing the problem.

  ![image](https://user-images.githubusercontent.com/25869911/121826183-0cf00680-cc7c-11eb-9d47-c487d2ff1828.png)


 All microservices send information to one distrusted tracing server(store database in memory or persistent database) and distributed tracing server provide interface to trace the issues in microservices.


 Distributed tracing server is zipkin and launch in a container

 docker run -p 9411:9411 openzipkin/zipkin:2.23

 http://localhost:9411/zipkin/


 connect the microservices to zipkin –
 in the currency exchange service -> pom.xml add, the rabbit is for later use

    <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-sleuth</artifactId>
    </dependency>
    <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-sleuth-zipkin</artifactId>
    </dependency>
    <dependency>
       <groupId>org.springframework.amqp</groupId>
       <artifactId>spring-rabbit</artifactId>
    </dependency>


 each microservices need unique id, so sleuth(framework) assigns a unique id to each request. We don’t want trace every request so we are going to use sample configuration(we just trace some percentage of the request, for performance impact)

 application.property file add
 ## samplig configuration for distributed tracing, trace every request 1.0
 ## for the production 0.05 is 5 %

 spring.sleuth.sampler.probability=1.0


 add Logger, when you see the logs now you can see the request has unique id.

 By default configuration in zipkin microservices can find the distributed tracing server. 
 Default is working same url if you are running zipkin in another url
 In the application properties of micrservices you need to add

 spring.zipkin.baseUrl=http://locahost:9411/


 each microservices running individual containers.


 Create containers for services

 In pom.xml add in the build.  – configuration the name is the docker hub id, name of the image.  V2 – version 2
 Spring maven plugin use to create docker images, configure pull policy – default is ALWAYS.     IF_NOT_PRESENT – only images is not in the local , search in the docker hub

    <build>
         <plugins>
             <plugin>
                 <groupId>org.springframework.boot</groupId>
                 <artifactId>spring-boot-maven-plugin</artifactId>
                 <configuration>
                     <image>
                         <name>dockerguemamusa101/mmv2-${project.artifactId}:${project.version}</name>
                     </image>
                     <pullPolicy>IF_NOT_PRESENT</pullPolicy>
                 </configuration>
             </plugin>
         </plugins>
     </build>




 Run the command 
 ./mvnw spring-boot:build-image
 ./mvnw spring-boot:build-image -DskipTests.  -> for skip the test

 This create jar file
 -	First time is taking some time, because its download all 

 This will create docker image

 For run docker image in the terminal

 Docker run -p 8000:8000 dockerguemamusa101/mmv2-currency-exchange-service:0.0.1-SNAPSHOT

 -	You need to launch zipkin and eureka simultaneously



 Docker compose – to launch multiple microservices simultaneously(autmate)

 Compose is a tool for defining and running multi-container docker applications. With compose , you use a YAML file to configure your applications services


 Single commando to launch YAML file – all launches
 Docker desktop for mac and windows have docker compose, for linux you need to install

 Docker-compose –version.  -> to see docker composion version in terminal

 Create in the root carpeta that contains all carpet of microservices
 docker-compose.yaml file

 put version, in docker compose, each container is service
 define services

 YAML file is sensible to space, use only 2 spaces not tab

 For services use the same name of the project

 Edit yaml file

 Go to the root carpet where all the services is in the folder and where you can find the yaml file in the terminal

 docker-compose up

 this will launch the yaml docker compose file

 good practice in docker-compose is create the network

     version: '3.7'

     services:
       currency-exchange:
         image: dockerguemamusa101/mmv2-currency-exchange-service:0.0.1-SNAPSHOT
         mem_limit: 700m
         ports: 
           - "8000:8000"
         networks:
           - currency-network

     networks:
       currency-network:


 always build the image first – docker

 you can specify the service that depends_on another service

     version: '3.7'

     services:
       currency-exchange:
         image: dockerguemamusa101/mmv2-currency-exchange-service:0.0.1-SNAPSHOT
         mem_limit: 700m
         ports: 
           - "8000:8000"
         networks:
           - currency-network
         depends_on:
           - naming-server

       naming-server:
         image: dockerguemamusa101/mmv2-naming-server:0.0.1-SNAPSHOT
         mem_limit: 700m
         ports: 
           - "8761:8761"
         networks:
           - currency-network

     networks:
       currency-network:


 from the inside of the docker container , locahost is not the same as in running in your machine.
 Option 1 – you can configure application.properties of the currency exchange service 
 If you change application.property you need to rebuild the container image.
 ## for docker container
 eureka.client.serviceUrl.defaultZone=http://naming-server:8761/eureka

 option 2 - you can add environment variable in the docker componse yaml file. Use all mayuscule for environment and :

 environment:
       EUREKA.CLIENT.SERVICEURL.DEFAULTZONE: http://naming-server:8761/eureka



 You can add all  your own services like this and add services from docker hub library like zipkin and rabbit mq


 Docker swarm is a container orchestration tool, is like kubernetes




 Rabbit mq


 we using message queue, because if the distrusted tracing server is down, the data will be losed, so we can store in the queue(rabbit mq).


 ![image](https://user-images.githubusercontent.com/25869911/121826198-1b3e2280-cc7c-11eb-98b8-677fb5d010d6.png)


 Add rabbit mq in pom.xml

    <dependency>
        <groupId>org.springframework.amqp</groupId>
        <artifactId>spring-rabbit</artifactId>
    </dependency>


 In application.properties add this
 spring.zipkin.sender.type=rabbit

 or add environment, dependencies and the rabbitmq service in the yaml docker compose file.


    rabbitmq:
        image: rabbitmq:3.8.12-management
        mem_limit: 700m
        ports: 
          - "5672:5672"
          - "15672:15672"
        networks:
          - currency-network


 For rest of the rest api :

    environment:
          EUREKA.CLIENT.SERVICEURL.DEFAULTZONE: http://naming-server:8761/eureka
          SPRING.ZIPKIN.BASEURL: http://zipkin-server:9411/
          RABBIT_URI: amqp://guest:guest@rabbitmq:5672
          SPRING_RABBITMQ_HOST: rabbitmq
          SPINRG_ZIPKIN_SENDER_TYPE: rabbit

 For zipkin
 
       environment:
             RABBIT_URI: amqp://guest:guest@rabbitmq:5672



 For zipkin and rest of the rest api add dependencies
 
     depends_on:
         - rabbitmq



 Final docker compose

        version: '3.7'

        services:
          currency-exchange:
            image: dockerguemamusa101/mmv2-currency-exchange-service:0.0.1-SNAPSHOT
            mem_limit: 700m
            ports: 
              - "8000:8000"
            networks:
              - currency-network
            depends_on:
              - naming-server
              - rabbitmq
            environment:
              EUREKA.CLIENT.SERVICEURL.DEFAULTZONE: http://naming-server:8761/eureka
              SPRING.ZIPKIN.BASEURL: http://zipkin-server:9411/
              RABBIT_URI: amqp://guest:guest@rabbitmq:5672
              SPRING_RABBITMQ_HOST: rabbitmq
              SPINRG_ZIPKIN_SENDER_TYPE: rabbit

          currency-conversion:
            image: dockerguemamusa101/mmv2-currency-conversion-service:0.0.1-SNAPSHOT
            mem_limit: 700m
            ports: 
              - "8100:8100"
            networks:
              - currency-network
            depends_on:
              - naming-server
              - rabbitmq
            environment:
              EUREKA.CLIENT.SERVICEURL.DEFAULTZONE: http://naming-server:8761/eureka
              SPRING.ZIPKIN.BASEURL: http://zipkin-server:9411/
              RABBIT_URI: amqp://guest:guest@rabbitmq:5672
              SPRING_RABBITMQ_HOST: rabbitmq
              SPINRG_ZIPKIN_SENDER_TYPE: rabbit

          api-gateway:
            image: dockerguemamusa101/mmv2-api-gateway:0.0.1-SNAPSHOT
            mem_limit: 700m
            ports: 
              - "8765:8765"
            networks:
              - currency-network
            depends_on:
              - naming-server
              - rabbitmq
            environment:
              EUREKA.CLIENT.SERVICEURL.DEFAULTZONE: http://naming-server:8761/eureka
              SPRING.ZIPKIN.BASEURL: http://zipkin-server:9411/
              RABBIT_URI: amqp://guest:guest@rabbitmq:5672
              SPRING_RABBITMQ_HOST: rabbitmq
              SPINRG_ZIPKIN_SENDER_TYPE: rabbit

          naming-server:
            image: dockerguemamusa101/mmv2-naming-server:0.0.1-SNAPSHOT
            mem_limit: 700m
            ports: 
              - "8761:8761"
            networks:
              - currency-network

          zipkin-server:
            image: openzipkin/zipkin:2.23
            mem_limit: 700m
            ports: 
              - "9411:9411"
            networks:
              - currency-network
            environment:
              RABBIT_URI: amqp://guest:guest@rabbitmq:5672
            depends_on:
              - rabbitmq
            restart: always #Restart if there is a problem starting up

          rabbitmq:
            image: rabbitmq:3.5.3-management
            mem_limit: 700m
            ports: 
              - "5672:5672"
              - "15672:15672"
            networks:
              - currency-network

        networks:
          currency-network:
