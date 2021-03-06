# Documentation

## Technologies

This project is designed on a 4 Tier architecture

- **Presentation layer:** [Primefaces](https://primefaces.org)
- **Application layer:** [Wildfly](https://www.wildfly.org)
- **Business layer:** [Jakarta EE](https://de.wikipedia.org/wiki/Jakarta_EE) 
- **Data access layer:** 
    - **Relational Database:** [JPA](https://de.wikipedia.org/wiki/Jakarta_Persistence_API) with [PostgreSQL](https://www.postgresql.org) 
    - **Document Oriented Database:** [ElasticSearch](https://www.elastic.co)

This project also uses [Apache Maven](https://maven.apache.org) as it's project management and comprehension tool.

## Modules

This project was developed in a modular architecture in which the components contain their specific logic 
and are combined in the Main module and afterwards deployed in the War module.

Modules:
 - **Components** 
 - **Main** 
 - **War** 
 
Component documentation:
  - **Core:** [doc](core.md)
  - **Elastic:** [doc](elastic.md)
  - **GCS:** [doc](gcs.md)
  - **ParkData-Sorter:** [doc](parkingdata-sorter.md)
  - **ParkData-Receiver:** [doc](parkingdata-receiver.md)
  - **Web:** [doc](web.md)
  - **User:** [doc](user.md)
  - **Admin:** [doc](admin.md)

# Getting Started

## Project installation guide

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
See deployment for notes on how to deploy the project on a live system.

### Prerequisites

Following programs are needed:
 
 - JDK 1.8
 - Maven 
 - Wildfly 22.0.0.Final
 - Postgres 12.3 
 - ElasticSearch 7.13.1
 - Kibana 7.13.1 (Recommended)

### Setup Configuration

#### Wildfly

##### JDBC Drivers
In order to connect to the postgres database a JDBC driver is needed. You can download the Postgres 42.2.18 driver [here](https://jdbc.postgresql.org/download.html).

Place the driver in **WILDFLY_HOME**\modules\org\postgresql\main create folders if necessary.
Afterward add a **module.xml** with the following content to the same directory:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.3" name="org.postgresql">
    <resources>
        <resource-root path="postgresql-42.2.18.jar"/>
        <!-- Make sure this matches the name of the JAR you are installing -->
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```

##### Datasource
 
Add a new driver and datasource to your standalone.xml at **WILDFLY_HOME**\standalone\configuration
```xml
<datasource jndi-name="java:/parkguidance" pool-name="ParkGuidance" enabled="true" use-java-context="true">
    <connection-url>jdbc:postgresql://localhost:5432/parkguidance</connection-url>                
    <driver>postgresql</driver>
    <security>
        <user-name>DBACC</user-name>
        <password>DBACC</password>
    </security>
</datasource>
```
```xml
<driver name="postgresql" module="org.postgresql">
    <driver-class>org.postgresql.Driver</driver-class>
    <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
</driver>
```

##### Logging

Add a new console-handler and logger to your standalone.xml at **WILDFLY_HOME**\standalone\configuration
```xml
   <subsystem xmlns="urn:jboss:domain:logging:8.0">
    ....
    <console-handler name="stdout-console" autoflush="true">
        <level name="ALL"/>
        <formatter>
            <pattern-formatter pattern="%s%n"/>
        </formatter>
    </console-handler>
    <logger category="stdout" use-parent-handlers="false">
        <handlers>
            <handler name="stdout-console"/>
        </handlers>
    </logger>
    ....
   </subsystem>
```

##### Webserver Admin-user

Add a new admin user with the provided bat file at **WILDFLY_HOME**\bin\add-user.bat.


#### Postgres

##### User
Create a new User with **Username:** DBACC and **Password:** DBACC. 

##### Database
Add a new Database with the name **parkguidance**

**Please Note:**
Do not use these users in a production system.

### Installing/Building Sourcecode

#### Dependencies

This project requires a forked version of [Primefaces 8.0](https://github.com/Neoministein/primefaces/tree/8.0%40Neo). 
You can download it as zip or with git.

```
git clone --branch 8.0@Neo https://github.com/Neoministein/primefaces/
```

After installing, you need to add it to your local maven repository using.
```
mvn clean install
```

#### Source Code
You can download the source code as a zip or with git.

```
git clone https://github.com/Neoministein/parkguidance
```

To build the sources use the command: 

```
mvn clean package
```
### Deployment

#### Starting Webserver
After building the sources you can start wildfly at **WILDFLY_HOME**\bin\standalone.bat.

 > If you want to access the page from other sources than localhost,
 > you would need to add "-b 0.0.0.0" as a parameter to the execution of standalone.bat

#### Deploy
You can now log in to the admin panel at [http://localhost:9990](http://localhost:9990) and add the war Files to your deployment:

- parkguidance-war-x.x.x.war

#### URL'S
After deploying it you can reach the website at:

- User Panel: [http://localhost:8080/park-guidance](http://localhost:8080/park-guidance)

To see the admin features you'll need to login at:

- Admin Panel: [http://localhost:8080/park-guidance/login](http://localhost:8080/park-guidance/login)

And these two API endpoints:

- Parkdata Receiver [http://localhost:8080/park-guidance/api/park-data/receiver](http://localhost:8080/park-guidance/api/park-data/receiver)
- Parkdata Sorter: [http://localhost:8080/park-guidance/api/park-data/sorter](http://localhost:8080/park-guidance/api/park-data/sorter)
