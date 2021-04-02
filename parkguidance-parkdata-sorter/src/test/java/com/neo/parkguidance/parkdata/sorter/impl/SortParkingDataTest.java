package com.neo.parkguidance.parkdata.sorter.impl;

import com.neo.parkguidance.core.entity.DataSheet;
import com.neo.parkguidance.core.impl.dao.DataSheetEntityManager;
import com.neo.parkguidance.core.impl.dao.ParkingDataEntityManager;
import com.neo.parkguidance.core.impl.dao.ParkingGarageEntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SortParkingDataTest {

    SortParkingDataImpl subject;

    @Mock
    ParkingDataEntityManager parkingDataManager;

    @Mock
    ParkingGarageEntityManager parkingGarageManager;

    @Mock
    DataSheetEntityManager dataSheetManager;

    @BeforeEach
    void setUp() {
        subject = Mockito.spy(SortParkingDataImpl.class);

        parkingDataManager = mock(ParkingDataEntityManager.class);
        subject.parkingDataManager = parkingDataManager;

        parkingGarageManager = mock(ParkingGarageEntityManager.class);
        subject.parkingGarageManager = parkingGarageManager;

        dataSheetManager = mock(DataSheetEntityManager.class);
        subject.dataSheetManager = dataSheetManager;
    }

    @Test
    void dataSheetFromDateTest() {
        //Arrange
        Date date = new Date(-1000L);

        DataSheet expected = new DataSheet();
        expected.setYear(1970);
        expected.setWeek(1);
        expected.setDay(5);
        expected.setHalfHour(1);
        //Act
        DataSheet result = subject.dataSheetFromDate(date);

        //Assert
        assertEquals(expected.getYear(),result.getYear());
        assertEquals(expected.getWeek(),result.getWeek());
        assertEquals(expected.getDay(),result.getDay());
        assertEquals(expected.getHalfHour(),result.getHalfHour());

    }

    @Test
    void roundUpTest(){
        //Arrange
        Date date = new Date(2400000L);

        Date expected = new Date(3600000L);
        //Act
        Date result = subject.roundUp(date);

        //Assert

        assertEquals(expected,result);
    }

    @Test
    void roundUpStayTest(){
        //Arrange
        Date date = new Date(2400000L);

        Date expected = new Date(3600000L);
        //Act
        Date result = subject.roundUp(date);

        //Assert

        assertEquals(expected,result);
    }

    @Test
    void roundDownTest(){
        //Arrange
        Date date = new Date(0L);

        Date expected = new Date(0L);
        //Act
        Date result = subject.roundDown(date);

        //Assert

        assertEquals(expected,result);
    }

    @Test
    void roundDownStayTest(){
        //Arrange
        Date date = new Date(0L);

        Date expected = new Date(0L);
        //Act
        Date result = subject.roundDown(date);

        //Assert

        assertEquals(expected,result);
    }
}
