# ParkGuidance-ParkingData-Sorter

The parking data sorter component is designed to sort the raw-parkingdata into sorted-parkingdata.

The data is both retrieved and stored in elasticsearch

## Interface

The sorting data is triggered by a normal HTTP POST request to the URL.

> http://localhost:8080/park-guidance/api/park-data/sorter

Access to sorter access point is only possible if the request is made by localhost.


## Logic

 1. Gather all ParkingGarages.
 2. Get the date of the first and last entry of a ParkingGarage.
 3. Both are rounded down to a full hour to reduce using the same data set being used on the next one.
 4. Get average capacity in the first half-hour time frame after the first entry.
 5. Repeat step 5 until reached the last entry.
 6. Set all used entries as form the ParkingGarage in that timeframe as sorted. 
 7. Repeat 2-6 until done with all ParkingGarages.
 
 >Back to  [README.MD](../README.md)
