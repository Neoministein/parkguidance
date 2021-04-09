#ParkGuidance-User

The user module contains all pages a normal user will interact with or get in contact with. 

## Interface

You can access the user web pages at http://localhost:8080/park-guidance/.

### Index Page

#### Concept

The index page should have a table containing all parking garages with their max and current capacity.
There should also be a way to click on the parking garage to get to a page with more information.

![Concept Images of index Screen](images/c-User-Index.png)

#### Product

The final screen displays an application scoped table which is updated only every 5 seconds. 

![Image showing index screen](images/f-User-Index.png)

### Parking Garage Data Page

#### Concept

The parking garage data page should display all the relevant data in regards to the selected parking garage.
Under the data lies a chart with the time of day, and its correlating capacity.

![Concept Images of Parking Garage Data Screen](images/c-User-GarageData.png)

#### Product

The final screen has an application scoped chart which is updated only every 30 min or when a new parking garage is added.

![Image showing Parking Garage Data screen](images/f-User-GarageData.png)

>Back to  [README.MD](../README.md)
