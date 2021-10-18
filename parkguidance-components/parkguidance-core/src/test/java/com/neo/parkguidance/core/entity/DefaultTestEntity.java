package com.neo.parkguidance.core.entity;

import com.neo.parkguidance.core.impl.config.ConfigType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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

    public static Configuration createDefaultConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setId(0l);
        configuration.setKey("key");
        configuration.setType(ConfigType.SINGLE);
        configuration.setComponent("component");
        configuration.setDescription("description");
        configuration.setConfigValues(new ArrayList<>(Arrays.asList(createDefaultConfigValue())));
        return configuration;
    }

    public static ConfigValue createDefaultConfigValue() {
        ConfigValue configValue = new ConfigValue();
        configValue.setId(0l);
        configValue.setKey("key");
        configValue.setValue("value");
        configValue.setHidden(false);
        configValue.setDescription("description");
        return configValue;
    }

    public static RegisteredUser createDefaultRegisteredUser() {
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setId(0l);
        registeredUser.setUsername("username");
        registeredUser.setEmail("email@email.email");
        registeredUser.setCreatedOn(new Date(0));
        registeredUser.setDeactivatedOn(new Date(0));
        registeredUser.setDeactivated(false);
        registeredUser.setPermissions(new ArrayList<>(Arrays.asList(createDefaultPermission())));
        registeredUser.setTokens(new ArrayList<>(Arrays.asList(createDefaultUserToken())));
        return registeredUser;
    }

    public static UserToken createDefaultUserToken() {
        UserToken userToken = new UserToken();
        userToken.setId(0l);
        userToken.setKey("key");
        userToken.setName("name");
        userToken.setPermissions(new ArrayList<>(Arrays.asList(createDefaultPermission())));
        return userToken;
    }

    public static Permission createDefaultPermission() {
        Permission permission = new Permission();
        permission.setId(0l);
        permission.setName("name");
        return permission;
    }

    public static Configuration createSingleConfiguration(String key, String value) {
        ConfigValue configValue = new ConfigValue();
        configValue.setKey(key);
        configValue.setValue(value);
        Configuration configuration = new Configuration();
        configuration.setKey(key);
        configuration.setType(ConfigType.SINGLE);
        configuration.setConfigValues(Arrays.asList(configValue));
        return configuration;
    }
}
