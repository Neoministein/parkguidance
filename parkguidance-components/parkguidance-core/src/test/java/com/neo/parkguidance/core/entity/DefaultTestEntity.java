package com.neo.parkguidance.core.entity;

public class DefaultTestEntity {

    private DefaultTestEntity() {}

    public static Address createDefaultAddress() {
        Address address = new Address();
        address.setId(0l);
        address.setCityName("CityName");
        address.setStreet("Street");
        address.setPlz(5000);
        address.setNumber(5001);
        address.setLatitude(5002d);
        address.setLongitude(5003d);
        return address;
    }

    public static ParkingGarage createDefaultParkingGarage() {
        ParkingGarage parkingGarage = new ParkingGarage();
        parkingGarage.setKey("key");
        parkingGarage.setName("name");
        parkingGarage.setAccessKey("accessKey");
        parkingGarage.setSpaces(5000);
        parkingGarage.setOccupied(50001);
        parkingGarage.setDescription("description");
        parkingGarage.setPrice("price");
        parkingGarage.setOperator("operator");
        parkingGarage.setAddress(createDefaultAddress());
        return parkingGarage;
    }

    public static StoredValue createDefaultStoredValue() {
        StoredValue storedValue = new StoredValue();
        storedValue.setKey("key");
        storedValue.setValue("value");
        storedValue.setHidden(false);
        storedValue.setComponent("component");
        storedValue.setDescription("description");
        return storedValue;
    }
}
