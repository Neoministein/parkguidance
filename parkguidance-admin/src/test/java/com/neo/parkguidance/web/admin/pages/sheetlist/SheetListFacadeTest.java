package com.neo.parkguidance.web.admin.pages.sheetlist;

import com.neo.parkguidance.core.api.HTTPRequestSender;
import com.neo.parkguidance.core.entity.ApiRequest;
import com.neo.parkguidance.core.entity.DataSheet;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SheetListFacadeTest {

    DataSheetListFacade subject;

    @Mock AbstractEntityDao<DataSheet> dataSheetService;

    @Mock HTTPRequestSender httpRequestSender;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(DataSheetListFacade.class);

        dataSheetService = mock(AbstractEntityDao.class);

        httpRequestSender = mock(HTTPRequestSender.class);

        subject.dataSheetDao = dataSheetService;

        subject.httpRequestSender = httpRequestSender;
    }

    /**
     * Verifies if the facade returns the right amount
     */
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

    /**
     * Verifies if the dao gets called to delete a object
     */
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
