#ParkGuidance-ParkingData-Receiver

The parking data receiver module is designed to accept data from parking garages and sort them correctly in the database.
## Interface

The API endpoint is located at [http://localhost:8080/park-guidance/park-data/receiver/api](http://localhost:8080/park-guidance/park-data/receiver/api).
Data can be sent to the endpoint with an HTTP POST request to the URL, but it requires a body with specified information.

### HTTP POST Body

The HTTP Post request is required to have a body with JSON syntax.

```json
{
  "accessKey": "",
  "type": ""
}
```

The access key for each parking garage can be found in the [Admin Panel](admin.md) under Parking Garage and by clicking on the wanted garage.

There are currently three different types you can use.

Type | What does it do | Use case
---- | ----------------|---------
incr    | +1 to the current number of occupied spaces           | Sensor at Garage
decr    | -1 to the current number of occupied spaces           | Sensor at Garage
set     | The current number of occupied spaces is set to this | Web scraper 
    
** Note**
If you use set you will need to add an amount tag to the JSON string.
```json
{
  "accessKey": "",
  "type": "set",
  "amount": 0
}
```

### HTTP Responses

The most likely cause of what happened when you get this response.

Number |  Name | Meaning
------ | ------| -------
400 | Bad request           | Something wrong with the JSON string
403 | Forbidden             | Wrong Parking Garage accessKey
404 | Not Found             | Wrong serer URL or the module is currently down
405 | Method not allowed    | Wrong type
500 | Internal Server Error | Something went wrong at the server's end and most likely not your fault

>Back to  [README.MD](../README.md)
