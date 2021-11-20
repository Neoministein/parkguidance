package com.neo.parkguidance.framework.impl.config;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.entity.ConfigValue;
import com.neo.parkguidance.framework.entity.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.neo.parkguidance.framework.entity.DefaultTestEntity.createSingleConfiguration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ConfigServiceImplTest {

    ConfigServiceImpl subject;

    EntityDao entityDao;
    static List<Configuration> storedValueList;

    @BeforeAll
    public static void setUpDao() {
        storedValueList = new ArrayList<>();

        storedValueList.add(createSingleConfiguration("string","This is a string"));
        storedValueList.add(createSingleConfiguration("integer","5000"));
        storedValueList.add(createSingleConfiguration("long","5000"));
        storedValueList.add(createSingleConfiguration("boolean","false"));
        storedValueList.add(createSingleConfiguration("double","5000"));
        storedValueList.add(createSingleConfiguration("jsonObject",new JSONObject().toString()));
        storedValueList.add(createSingleConfiguration("jsonArray",new JSONArray().toString()));


        ConfigValue entryOne = new ConfigValue();
        entryOne.setKey("entryOne");
        entryOne.setValue("entryOne");
        ConfigValue entryTwo = new ConfigValue();
        entryTwo.setValue("entryTwo");
        entryTwo.setValue("entryTwo");
        Configuration configurationMap = new Configuration();
        configurationMap.setKey("map");
        configurationMap.setType(ConfigType.MAP);
        configurationMap.setConfigValues(Arrays.asList(entryOne,entryTwo));
        storedValueList.add(configurationMap);
    }

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(ConfigServiceImpl.class);

        entityDao = Mockito.mock(EntityDao.class);
        subject.dao = entityDao;

        Mockito.doReturn(storedValueList).when(entityDao).findAll();
        subject.init();
    }

    @Test
    void stringExistsTest() {
        //Arrange
        String expected = "This is a string";
        String result;
        //Act

        result = subject.getString("string","NOT_EXIST");

        //Assert

        assertEquals(expected, result);
    }

    @Test
    void stringNotExistsTest() {
        //Arrange
        String expected = "NOT_EXIST";
        String result;
        //Act

        result = subject.getString("string1","NOT_EXIST");

        //Assert

        assertEquals(expected, result);
    }

    @Test
    void intExistsTest() {
        //Arrange
        int expected = 5000;
        int result;
        //Act

        result = subject.getInteger("integer",-1);

        //Assert

        assertEquals(expected, result);
    }

    @Test
    void intNotExistsTest() {
        //Arrange
        int expected = -1;
        int result;
        //Act

        result = subject.getInteger("integer1",-1);

        //Assert

        assertEquals(expected, result);
    }

    @Test
    void longExistsTest() {
        //Arrange
        long expected = 5000;
        long result;
        //Act

        result = subject.getLong("long",-1);

        //Assert

        assertEquals(expected, result);
    }

    @Test
    void longNotExistsTest() {
        //Arrange
        long expected = -1;
        long result;
        //Act

        result = subject.getLong("long1",-1);

        //Assert

        assertEquals(expected, result);
    }

    @Test
    void booleanExistsTest() {
        //Arrange
        boolean expected = false;
        boolean result;
        //Act

        result = subject.getBoolean("boolean",true);

        //Assert

        assertEquals(expected, result);
    }

    @Test
    void booleanNotExistsTest() {
        //Arrange
        boolean expected = true;
        boolean result;
        //Act

        result = subject.getBoolean("boolean1",true);

        //Assert

        assertEquals(expected, result);
    }

    @Test
    void doubleExistsTest() {
        //Arrange
        double expected = 5000;
        double result;
        //Act

        result = subject.getDouble("double",-1);

        //Assert

        assertEquals(expected, result);
    }

    @Test
    void doubleNotExistsTest() {
        //Arrange
        double expected = -1;
        double result;
        //Act

        result = subject.getDouble("double1",-1);

        //Assert

        assertEquals(expected, result);
    }

    @Test
    void jsonObjectExistsTest() {
        //Arrange
        JSONObject expected = new JSONObject();
        JSONObject result;
        //Act

        result = subject.getJSONObject("jsonObject",null);

        //Assert

        assertEquals(expected.toString(), result.toString());
    }

    @Test
    void jsonObjectNotExistsTest() {
        //Arrange
        JSONObject expected = null;
        JSONObject result;
        //Act

        result = subject.getJSONObject("jsonObject1",null);

        //Assert

        assertEquals(expected, result);
    }

    @Test
    void jsonArrayExistsTest() {
        //Arrange
        JSONArray expected = new JSONArray();
        JSONArray result;
        //Act

        result = subject.getJSONArray("jsonArray",null);

        //Assert

        assertEquals(expected.toString(), result.toString());
    }

    @Test
    void jsonArrayNotExistsTest() {
        //Arrange
        JSONArray expected = null;
        JSONArray result;
        //Act

        result = subject.getJSONArray("jsonArray1",null);

        //Assert

        assertEquals(expected, result);
    }

    @Test
    void mapExistTest() {
        //Arrange

        //Act
        Map<String, ConfigValue> map = subject.getConfigMap("map");
        //Assert
        assertNotEquals(null,map);
    }
}
