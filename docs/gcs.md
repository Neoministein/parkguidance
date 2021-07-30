# ParkGuidance-GCS

The Google Cloud Platform component is designed to handle all traffic between the parkguidance system, and 
the Google Cloud Platform API.

## Connection

The Google Cloud Platform Services are all reached through it's REST API in the cloud. 
Therefore, request are sent using the cores components HTTPRequestSender classes.

## Services 

These are the currently used API's by parkguidance

### Maps

#### Distance Matrix

>The distance matrix API calculates the time and distance between multiple location. 

#### GeoCoding

> Returns the Longitude or Latitude from a address or vice versa

#### CrossPlatformURL

> Used to receive url to forward the user to Google maps for a location or direction info.

> Back to  [README.MD](../README.md)