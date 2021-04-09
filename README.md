# Parkguidance

This Parkguidance System is a school project for the [Module 151](https://cf.ict-berufsbildung.ch/modules.php?name=Mbk&a=20101&cmodnr=151&noheader=1) at [BBB](https://bbbaden.ch).

This System is designed to receive data from different parking garages in order to sort and log them into its database.
On request, it displays the current capacity and Infos of the hooked-up parking garages and gives a rough estimate of its free capacity throughout the day. 

## Documentation

### Technologies

This project is designed on a 4 Tier architecture

- **Presentation layer:** [Primefaces](https://primefaces.org)
- **Application layer:** [Wildfly](https://www.wildfly.org)
- **Business layer:** [Java EE](https://de.wikipedia.org/wiki/Jakarta_EE) 
- **Data access layer:** [JPA](https://de.wikipedia.org/wiki/Jakarta_Persistence_API) with [PostgreSQL](https://www.postgresql.org)

This project als uses [Apache Maven](https://maven.apache.org) as it's project management and comprehension tool.

### Modules

This system was developed in a style that all components are structured in such a way that each component can be deployed and exchanged individually without affecting each other. 

Module documentation:
 - **Core:** [link](docs/core.md)
 - **Web:** [link](docs/web.md)
 - **User:** [link](docs/user.md)
 - **Admin:** [link](docs/admin.md)
 - **ParkData-Sorter:** [link](docs/parkingdata-sorter.md)
 - **ParkData-Receiver:** [link](docs/parkingdata-receiver.md)

## Getting Started

### Project installation guide

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

#### Prerequisites

Following programs are needed:
 
 - JDK 1.8
 - Maven 
 - Wildfly 22.0.0.Final
 - Postgres 12.3 

#### Setup Configuration

##### Wildfly

###### JDBC Drivers
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

###### Datasource
 
Add a new driver and datasource to your standalone.xml at **WILDFLY_HOME**\standalone\configuration
```xml
<datasource jndi-name="java:/parkguidance" pool-name="ParkGuidance" enabled="true" use-java-context="true">
    <connection-url>jdbc:postgresql://localhost:5432/parkguidance</connection-url>                <driver>postgresql</driver>
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

###### Webserver Admin-user

Add a new admin user with the provided bat file at **WILDFLY_HOME**\bin\add-user.bat.


##### Postgres

###### User
Create a new User with **Username:** DBACC and **Password:** DBACC. 

###### Database
Add a new Database with the name **parkguidance**

**Please Note:**
Do not use these users in a production system.

#### Installing/Building Sourcecode

You can download the source code as a zip or with git.

```
git clone https://github.com/Neoministein/parkguidance
```

To build the sources use the command: 

```
mvn clean package
```
#### Deployment

##### Starting Webserver
After building the sources you can start wildfly at **WILDFLY_HOME**\bin\standalone.bat.

 > If you want to access the page from other sources than localhost,
 > you would need to add "-b 0.0.0.0" as a parameter to the execution of standalone.bat

##### Deploy
You can now log in to the admin panel at [http://localhost:9990](http://localhost:9990) and add these war Files to your deployment:

- parkguidance-admin-x.x.x.war
- parkguidance-user-x.x.x.war
- parkguidance-parkdata-receiver-x.x.x.war
- parkguidance-parkdata-sorter-x.x.x.war


##### URL'S
After deploying it you can reach these two sites:

- Admin Panel: [http://localhost:8080/park-guidance/admin](http://localhost:8080/park-guidance/admin)
- User Panel: [http://localhost:8080/park-guidance](http://localhost:8080/park-guidance)

And these two API endpoints:

- Parkdata Receiver [http://localhost:8080/park-guidance/park-data/receiver/api](http://localhost:8080/park-guidance/park-data/receiver/api)
- Parkdata Sorter: [http://localhost:8080/park-guidance/park-data/sorter/api](http://localhost:8080/park-guidance/park-data/sorter/api)


## Main Authors

...

## License

No licence specified yet.

## Acknowledgments
