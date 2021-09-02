package com.neo.parkguidance.core.impl.storedvalue;

import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.entity.StoredValue;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoredValueServiceImplTest {

    StoredValueServiceImpl subject;

    EntityDao entityDao;
    static List<StoredValue> storedValueList;

    @BeforeAll
    public static void setUpDao() {
        storedValueList = new ArrayList<>();

        StoredValue string = new StoredValue();
        string.setKey("string");
        string.setValue("This is a string");
        storedValueList.add(string);

        StoredValue integer = new StoredValue();
        integer.setKey("integer");
        integer.setValue("5000");
        storedValueList.add(integer);

        StoredValue l0ng = new StoredValue();
        l0ng.setKey("long");
        l0ng.setValue("5000");
        storedValueList.add(l0ng);

        StoredValue b00lean = new StoredValue();
        b00lean.setKey("boolean");
        b00lean.setValue("false");
        storedValueList.add(b00lean);

        StoredValue d0uble = new StoredValue();
        d0uble.setKey("double");
        d0uble.setValue("5000");
        storedValueList.add(d0uble);

        StoredValue jsonObject = new StoredValue();
        jsonObject.setKey("jsonObject");
        jsonObject.setValue(new JSONObject().toString());
        storedValueList.add(jsonObject);

        StoredValue jsonArray = new StoredValue();
        jsonArray.setKey("jsonArray");
        jsonArray.setValue(new JSONArray().toString());
        storedValueList.add(jsonArray);

    }

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(StoredValueServiceImpl.class);

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
}
