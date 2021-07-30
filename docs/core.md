# ParkGuidance-Core

The core component contains the persistence Entities and Data Access Objects which every module needs.
>This module will not be deployed and only serves as a dependency for the other modules.

## Persistence

The persistence API used in this project is JPA (Jakarta Persistence API) with the hibernate implementation.


### Entities
**Note**
> Each entity also contains a PrimaryKey which can be a Long or String
#### ParkingGarage

  Name | Type | Description | Not Null
-------|------|------------ | -----------
Name        | String    | Name of the ParkingGarage                 | [X]
Spaces      | int       | Max number of free spaces                 | [X]
Occupied    | int       | Current number of occupied spaces         | [X]
AccessKey   | String    | The API Access Key for the ParkingGarages | [X]
Address     | Address   | Address of ParkingGarage                  | [X]
Price       | String    | The tariff                                | [O]
Operator    | String    | Infos of the Grage Operator               | [O]
Description | String    | Other information                         | [O]

##### Address
  Name | Type | Description | Not Null
-------|------|------------ | -----------
CityName    | String    | Name of the City  | [X]
PLZ         | int       | Zipcode           | [X]
Street      | String    | Name of Street    | [X]
Number      | int       | Street Number     | [X]
latitude    | double    | The latitude of the address   | [X]
longitude   | double    | The longitude of the address  | [X]

#### RegisteredUser
  Name | Type | Description | Not Null
-------|------|------------ | -----------
Username    | String    | Username of User          | [X]
Password    | String    | Hashed Password of User   | [X]

#### Permission
  Name | Type | Description | Not Null
-------|------|------------ | -----------
Name   | String    | Name of permission   | [X]

> Permission System currently not in use due to only needing an admin and no normal user.
## Data Access Object

There is one abstract data access object with a generic type associated with it. 
The Class contains the base Database interaction which every Entity is most likely going to need.

- Create
- Edit
- Remove
- Find
- FindAll
- Count
- FindByColumn
- FindLikeExample
- FindCountLikeExample

Each entity has its own extension of the abstract class with its own specific methods if needed. 

Every query is also built using the javax persistence CriteriaQuery to keep the database calls dialect independent.

## Additional Infos

### Properties

The core module also contains a property file in which is define where the API endpoints are located. 

```properties
parkguidance.endpoint.sorter=http://localhost:8080/park-guidance/park-data/sorter/api
parkguidance.endpoint.reciver=http://localhost:8080/park-guidance/park-data/receiver/api
```

This is set up for ease of change for internal calls. This can be used when modules or the entire system is set up on different ports or webservers.

>Back to  [README.MD](../README.md)
