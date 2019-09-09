# 1. Advanced Cloud-Native Development Assignment - Freelancer4j
This is the assignment for the Advanced Cloud-Native Development training. The source code is hosted in [github](https://github.com/jeejeejango/freelance4j)

### 1.1 Business Use Case
Freelance4J Inc. is adopting a new microservices architecture and wishes to make their platform APIs available as REST APIs. They have asked you to lead a proof-of-concept (POC) using Red Hat OpenShift Application Runtimes. The purpose of the POC is to demonstrate the use of Red Hat OpenShift Application Runtimes for development of REST APIs on the OpenShift Container Platform.

### 1.2 REST Services and Architecture
Freelance4J Inc. management requires that you implement the following services in your project:
- API Gateway - Entry point for web-based mobile clients of the application
- Project Service - Provides information about the projects — including project owner, description and status- Freelancer Service - Provides information about the freelancers—including their name, email address and list of skills.

### 1.3 Development Tools
This project requires the following development tools:
- JDK 1.8
- Maven 3.5.x
- OC CLI
- Bash shell
- IDE of your choice

## 2. Project Service
The Project service provides information about the projects.

### 2.1 Design Choice
Project service is implemented with the following technology:
- Vert.x
- Mongo DB

### 2.2 Unit Testing
The unit testing are implemented for the Project Service and Vert.x APIVerticle:
- [ProjectServiceTest](project-service/src/test/java/com/redhat/freelance4j/project/verticle/service/ProjectServiceTest.java) - Testing of Project Service
- [ApiVerticleTest](project-service/src/test/java/com/redhat/freelance4j/project/api/ApiVerticleTest.java) - Testing of API with Vert.x

Change directory into project-service
```bash
cd project-service
```
  
To run the unit test cases:
```bash
mvn clean test
```  

Expected results:
```bash
Results :

Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
```
  
### 2.3 Deploying on OpenShift
Project Service is deployed on OpenShift 3.11 shared instance in OpenTLC. To deploy the project service, 
we will need to setup the project in OpenShift. 

Login to OpenShift
```bash
oc login https://master.na311.openshift.opentlc.com
```
Provide your credentials to login

To setup the project as environment variable:
```bash
export FREELANCE4J_PRJ=edd4-freelance4j
```

After login, create a new project:
```bash
oc new-project edd4-freelance4j
```

When new project is created:
```bash
Now using project "edd4-freelance4j" on server "https://master.na311.openshift.opentlc.com:443".

You can add applications to this project with the 'new-app' command. For example, try:

    oc new-app centos/ruby-25-centos7~https://github.com/sclorg/ruby-ex.git

to build a new example application in Ruby.
```

Change directory into project-service
```bash
cd project-service
```

To deploy MongoDB to OpenShift:
```bash
oc process -f etc/project-service-mongodb-persistent.yaml \
-p PROJECT_DB_USERNAME=mongo \
-p PROJECT_DB_PASSWORD=mongo | oc create -f - -n $FREELANCE4J_PRJ
```

To create the configmap for project-service:
```bash
oc create configmap project-service --from-file=etc/app-config.yml -n $FREELANCE4J_PRJ
```

When configmap is created:
```bash
configmap/project-service created
```

Add the view role to the default service account:
```bash
oc policy add-role-to-user view -z default -n $FREELANCE4J_PRJ
```
The project-service calls the Kubernetes API to retrieve the ConfigMap, which requires view access.

To deploy the codes to the project:
```bash
mvn clean fabric8:deploy -Popenshift -DskipTests -Dfabric8.namespace=$FREELANCE4J_PRJ
```

Wait till the s2i build is completed. To monitor the deployment:
```bash
oc get pods -n $FREELANCE4J_PRJ -w
```
Wait until you see READY 1/1 for project-mongodb-x-xyz and project-service-x-xyz. Press <CTRL+C> once the services are Running.

Retrieve the project-service URL:
```bash
export PROJECT_URL=http://$(oc get route project-service -n $FREELANCE4J_PRJ -o template --template='{{.spec.host}}')

echo $PROJECT_URL
``` 

### 2.4 CURL Request
To Retrieve all projects:
```bash
curl -X GET "$PROJECT_URL/projects"
```
Expected Output:
```json
[
  {
    "projectId": "111111",
    "ownerFirstName": "John",
    "ownerLastName": "Woo",
    "ownerEmail": "johnwoo@example.com",
    "title": "Face Off",
    "description": "American action film directed by John Woo and starring John Travolta and Nicolas Cage.",
    "status": "completed"
  },
  {
    "projectId": "222222",
    "ownerFirstName": "Joss",
    "ownerLastName": "Whedon",
    "ownerEmail": "josswhedon@example.com",
    "title": "The Avengers",
    "description": "American superhero film based on the Marvel Comics superhero team",
    "status": "completed"
  },
  {
    "projectId": "333333",
    "ownerFirstName": "Gary",
    "ownerLastName": "Dauberman",
    "ownerEmail": "gary@example.com",
    "title": "Swamp Thing",
    "description": "American superhero horror web television series",
    "status": "cancelled"
  },
  {
    "projectId": "444444",
    "ownerFirstName": "Jon",
    "ownerLastName": "Watts",
    "ownerEmail": "jonwatts@example.com",
    "title": "Spider-Man Far From Home",
    "description": "Parker is recruited by Nick Fury and Mysterio to face the Elementals",
    "status": "in_progress"
  },
  {
    "projectId": "555555",
    "ownerFirstName": "Todd",
    "ownerLastName": "Phillips",
    "ownerEmail": "toddphillips@example.com",
    "title": "Joker",
    "description": "based on DC Comics characters, stars Joaquin Phoenix as the Joker",
    "status": "open"
  }
]
```

To Retrieve project by projectId:
```bash
curl -X GET "$PROJECT_URL/projects/222222"
```

Expected Output:
```json
{
  "projectId" : "222222",
  "ownerFirstName" : "Joss",
  "ownerLastName" : "Whedon",
  "ownerEmail" : "josswhedon@example.com",
  "title" : "The Avengers",
  "description" : "American superhero film based on the Marvel Comics superhero team",
  "status" : "completed"
}
```

To Retrieve projects by status:
```bash
curl -X GET "$PROJECT_URL/projects/status/open"
```

Expected Output:
```json
[ {
  "projectId" : "555555",
  "ownerFirstName" : "Todd",
  "ownerLastName" : "Phillips",
  "ownerEmail" : "toddphillips@example.com",
  "title" : "Joker",
  "description" : "based on DC Comics characters, stars Joaquin Phoenix as the Joker",
  "status" : "open"
} ]
```
Only status of open, in_progress, completed or cancelled are supported. Any others will result in status code 500.

## 3. Freelancer service
The Freelancer service provides information about the freelancers information.

### 3.1 Design Choice
Freelancer service is implemented with the following technology:
- SpringBoot
- Postgresql DB

### 3.2 Unit Testing
The unit testing are implemented for the freelancer-service SpringBoot endpoint:
- [FreelancerEndpointTest](freelancer-service/src/test/java/com/redhat/freelance4j/freelancer/rest/FreelancerEndpointTest.java) - SpringBoot Rest endpoint tests

As project needs Postgresql for testing, you will need a local version, or deployed via docker.

To run Postgresql on docker:
```json
docker run --name freelancer-postgresql -e POSTGRES_USER=jboss -e POSTGRES_PASSWORD=jboss -e POSTGRES_DB=freelancerdb -d -p 5432:5432 postgres:9.6
```

if the docker host is not running on IP 192.168.99.100, you will need to update [application-test.properties]():
```json
spring.datasource.url=jdbc:postgresql://<docker or machine ip address>:5432/freelancerdb
...
```

Change directory into freelancer-service
```bash
cd freelancer-service
```
  
To run the unit test cases:
```bash
mvn clean test
```  

Expected results:
```bash
Results :

Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

### 3.3 Deploying on OpenShift
To deploy freelancer-service, it is assume the environment setup in section 2.3.

Change directory into freelancer-service
```bash
cd freelancer-service
```

Deploying Postgresql in project:
```bash
oc process -f etc/freelancer-service-postgresql-persistent.yaml \
-p FREELANCER_DB_USERNAME=jboss \
-p FREELANCER_DB_PASSWORD=jboss \
-p FREELANCER_DB_NAME=freelancerdb | oc create -f - -n $FREELANCE4J_PRJ
```

To deploy freelancer-service:
```bash
mvn clean fabric8:deploy -DskipTests -Popenshift -Dfabric8.namespace=$FREELANCE4J_PRJ
```

Wait till the s2i build is completed. To monitor the deployment:
```bash
oc get pods -n $FREELANCE4J_PRJ -w
```
Wait until you see READY 1/1 for freelancer-postgresql-x-xyz and freelancer-service-x-xyz. Press <CTRL+C> once the services are Running.

Retrieve the freelancer-service URL:
```bash
export FREELANCER_URL=http://$(oc get route freelancer-service -n $FREELANCE4J_PRJ -o template --template='{{.spec.host}}')

echo $FREELANCER_URL
```

### 3.4 CURL Request
To Retrieve all freelancers:
```bash
curl -X GET "$FREELANCER_URL/freelancers"
```
Expected Output:
```json
[
    {
        "freelancerId": "1",
        "firstName": "John",
        "lastName": "Woo",
        "emailAddress": "johnwoo@example.com",
        "skills": [
            "Java",
            "Python",
            "SQL"
        ]
    },
    {
        "freelancerId": "2",
        "firstName": "Joss",
        "lastName": "Whedon",
        "emailAddress": "josswhedon@example.com",
        "skills": [
            ".Net Core",
            "SQL"
        ]
    },
    {
        "freelancerId": "3",
        "firstName": "Gary",
        "lastName": "Dauberman",
        "emailAddress": "gary@example.com",
        "skills": [
            "Python",
            "ML"
        ]
    }
]
```

To Retrieve freelancer by freelancerId:
```bash
curl -X GET "$FREELANCER_URL/freelancers/2"
```

Expected Output:
```json
{
    "freelancerId": "2",
    "firstName": "Joss",
    "lastName": "Whedon",
    "emailAddress": "josswhedon@example.com",
    "skills": [
        ".Net Core",
        "SQL"
    ]
}
```

## 4. API gateway
The API gateway is the entry point for front-end clients into the freelance4j application. The gateway orchestrates the calls to the services that make up the application.

### 4.1 Design Choice
API gateway service is implemented with the following technology:
- Thorntail

### 4.2 Unit Testing
The unit testing are implemented for the API gateway endpoints:
- [RestApiTest](gateway-service/src/test/java/com/redhat/freelance4j/gateway/RestApiTest.java) - Thorntail Rest endpoint tests

Change directory into gateway-service
```bash
cd gateway-service
```
  
To run the unit test cases:
```bash
mvn clean test
```  

You will need to update project-local.yml if the endpoints are different:
```yaml
freelance4j:
  project-url: http://project-service-edd4-freelance4j.apps.na311.openshift.opentlc.com
  freelancer-url: http://freelancer-service-edd4-freelance4j.apps.na311.openshift.opentlc.com
```

Expected results:
```bash
Results :

Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
```

### 4.3 Deploying on OpenShift

Change directory into gateway-service
```bash
cd gateway-service
```

You will need to update etc/project-defaults.yml if the endpoints are different:
```yaml
freelance4j:
  project-url: http://project-service-edd4-freelance4j.apps.na311.openshift.opentlc.com
  freelancer-url: http://freelancer-service-edd4-freelance4j.apps.na311.openshift.opentlc.com
```

Create configmap for gateway-service:
```bash
oc create configmap gateway-service --from-file=etc/project-defaults.yml -n $FREELANCE4J_PRJ
```
Verify configmap/gateway-service is created.

To deploy gateway-service to OpenShift:
```bash
mvn clean fabric8:deploy -DskipTests -Popenshift -Dfabric8.namespace=$FREELANCE4J_PRJ
```

Wait till the s2i build is completed. To monitor the deployment:
```bash
oc get pods -n $FREELANCE4J_PRJ -w
```
Wait until you see READY 1/1 for  gateway-service-x-xyz. Press <CTRL+C> once the services are Running.

Retrieve the freelancer-service URL:
```bash
export GATEWAY_URL=http://$(oc get route gateway-service -n $FREELANCE4J_PRJ -o template --template='{{.spec.host}}')

echo $GATEWAY_URL
```

### 4.4 CURL Request
To Retrieve all projects:
```bash
curl -X GET "$GATEWAY_URL/gateway/projects"
```
Expected Output:
```json
[
  {
    "projectId": "111111",
    "ownerFirstName": "John",
    "ownerLastName": "Woo",
    "ownerEmail": "johnwoo@example.com",
    "title": "Face Off",
    "description": "American action film directed by John Woo and starring John Travolta and Nicolas Cage.",
    "status": "completed"
  },
  {
    "projectId": "222222",
    "ownerFirstName": "Joss",
    "ownerLastName": "Whedon",
    "ownerEmail": "josswhedon@example.com",
    "title": "The Avengers",
    "description": "American superhero film based on the Marvel Comics superhero team",
    "status": "completed"
  },
  {
    "projectId": "333333",
    "ownerFirstName": "Gary",
    "ownerLastName": "Dauberman",
    "ownerEmail": "gary@example.com",
    "title": "Swamp Thing",
    "description": "American superhero horror web television series",
    "status": "cancelled"
  },
  {
    "projectId": "444444",
    "ownerFirstName": "Jon",
    "ownerLastName": "Watts",
    "ownerEmail": "jonwatts@example.com",
    "title": "Spider-Man Far From Home",
    "description": "Parker is recruited by Nick Fury and Mysterio to face the Elementals",
    "status": "in_progress"
  },
  {
    "projectId": "555555",
    "ownerFirstName": "Todd",
    "ownerLastName": "Phillips",
    "ownerEmail": "toddphillips@example.com",
    "title": "Joker",
    "description": "based on DC Comics characters, stars Joaquin Phoenix as the Joker",
    "status": "open"
  }
]
```

To Retrieve project by projectId:
```bash
curl -X GET "$GATEWAY_URL/gateway/projects/222222"
```

Expected Output:
```json
{
  "projectId" : "222222",
  "ownerFirstName" : "Joss",
  "ownerLastName" : "Whedon",
  "ownerEmail" : "josswhedon@example.com",
  "title" : "The Avengers",
  "description" : "American superhero film based on the Marvel Comics superhero team",
  "status" : "completed"
}
```

To Retrieve projects by status:
```bash
curl -X GET "$GATEWAY_URL/gateway/projects/status/open"
```

Expected Output:
```json
[ {
  "projectId" : "555555",
  "ownerFirstName" : "Todd",
  "ownerLastName" : "Phillips",
  "ownerEmail" : "toddphillips@example.com",
  "title" : "Joker",
  "description" : "based on DC Comics characters, stars Joaquin Phoenix as the Joker",
  "status" : "open"
} ]
```
Only status of open, in_progress, completed or cancelled are supported. Any others will result in status code 500.

To Retrieve all freelancers:
```bash
curl -X GET "$GATEWAY_URL/gateway/freelancers"
```
Expected Output:
```json
[
    {
        "freelancerId": "1",
        "firstName": "John",
        "lastName": "Woo",
        "emailAddress": "johnwoo@example.com",
        "skills": [
            "Java",
            "Python",
            "SQL"
        ]
    },
    {
        "freelancerId": "2",
        "firstName": "Joss",
        "lastName": "Whedon",
        "emailAddress": "josswhedon@example.com",
        "skills": [
            ".Net Core",
            "SQL"
        ]
    },
    {
        "freelancerId": "3",
        "firstName": "Gary",
        "lastName": "Dauberman",
        "emailAddress": "gary@example.com",
        "skills": [
            "Python",
            "ML"
        ]
    }
]
```

To Retrieve freelancer by freelancerId:
```bash
curl -X GET "$GATEWAY_URL/gateway/freelancers/2"
```

Expected Output:
```json
{
    "freelancerId": "2",
    "firstName": "Joss",
    "lastName": "Whedon",
    "emailAddress": "josswhedon@example.com",
    "skills": [
        ".Net Core",
        "SQL"
    ]
}
```