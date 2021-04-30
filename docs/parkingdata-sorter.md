# ParkGuidance-ParkingData-Sorter

The parking data sorter module is designed to sort the received parking data into datasheets.

## Interface

The sorting data is triggered by a normal HTTP POST request to the URL.

**Note**
> An authentication system to restrict who can start is not implemented.
> 
> Also, always returns HTTP 200 if there is no server error then 500.


## Logic

 1. Gather all ParkingData entries which aren't yet sorted ordered by date.
 2. Get the date of the first and last entry.
    1.Both are rounded down to a full hour to reduce using the same data set being used on the next one.
 3. Get average capacity in the first half-hour time frame after the first entry.
 4. Repeat step 3 until reached the last entry.
 5. Set all used entries as sorted. 
 
 >Back to  [README.MD](../README.md)
