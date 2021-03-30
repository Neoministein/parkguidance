package com.neo.parkguidance.web.admin.pages.sheetlist;

import com.neo.parkguidance.core.entity.DataSheet;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.web.infra.entity.DataSheetEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SheetListFacadeTest {

    SheetListFacade subject;

    @Mock
    DataSheetEntityService dataSheetService;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(SheetListFacade.class);

        dataSheetService = mock(DataSheetEntityService.class);

        subject.dataSheetService = dataSheetService;
    }

    @Test
    void deleteAmount() {
        //Arrange

        List<DataSheet> list = new ArrayList<>();
        list.add(new DataSheet());
        list.add(new DataSheet());
        list.add(new DataSheet());

        int expected = 3;
        //Act
        int result = subject.delete(list);

        //Assert

        assertEquals(expected,result);
    }

    @Test
    void deleteVerifyDao() {
        //Arrange

        List<DataSheet> list = new ArrayList<>();
        list.add(new DataSheet());

        //Act
        subject.delete(list);

        //Assert

        verify(dataSheetService).remove(list.get(0));
    }
}
