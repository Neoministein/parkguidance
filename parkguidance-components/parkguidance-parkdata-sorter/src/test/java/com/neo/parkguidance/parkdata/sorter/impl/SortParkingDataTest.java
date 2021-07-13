package com.neo.parkguidance.parkdata.sorter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

class SortParkingDataTest {

    SortParkingDataImpl subject;

    @BeforeEach
    void setUp() {
        subject = Mockito.spy(SortParkingDataImpl.class);
    }
}
