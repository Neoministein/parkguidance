# ParkGuidance-ParkingData-Receiver

The parking data receiver component is designed to accept data from parking garages and store them correctly in elasticsearch.
## Interface

Data can be sent to the servlet with an HTTP POST request to the URL, but it requires a body with specified information.

### HTTP POST Body

Parkdata REST endpoint: 

```http request
PUT /api/v1/parkdata
```

Parameter | required | description
----------|----------| ---
accessKey | [X] | unique authentication key per parking garage
type      | [X] | see type table |
count     | [O] | a specfied count if the type is "set"

Type | What does it do | Use case
---- | ----------------|---------
incr    | +1 to the current number of occupied spaces           | Sensor at Garage
decr    | -1 to the current number of occupied spaces           | Sensor at Garage
set     | The current number of occupied spaces is set to this | Web scraper 
    
**Note**
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
404 | Not Found             | Wrong server URL or the module is currently down
405 | Method not allowed    | Wrong type
500 | Internal Server Error | Something went wrong at the server's end and most likely not your fault

>Back to  [README.MD](../README.md)
