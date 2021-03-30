package com.neo.parkguidance.web.user.pages.parklist;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

class ParkListControllerTest {


    ParkListController subject;

    @Mock
    ParkListModel model;

    @Mock
    ParkListFacade facade;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(ParkListController.class);

        model = mock(ParkListModel.class);
        subject.model = model;

        facade = mock(ParkListFacade.class);
        subject.facade = facade;
    }
}
