package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.model.objects.GeneralCanvasObject;
import cz.thewhiterabbit.electronicapp.model.rawdocument.RawObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

class FileServiceTest {
    private FileService service;
    private RawObject rawObject;
    private RawObject invalidRawObject;
    @BeforeEach
    void setUp() {
        service = new FileService();
        rawObject = new GeneralCanvasObject().toRawObject();
        invalidRawObject = new GeneralCanvasObject().toRawObject();
        invalidRawObject.removeProperty(invalidRawObject.getProperties().get(1));
    }

    @AfterEach
    void tearDown() {
        service = null;
    }

    @Test
    void validateTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        /*Method method = FileService.class.getDeclaredMethod("validateRawObject", RawObject.class);
        method.setAccessible(true);
        method.invoke(service, invalidRawObject);*/
    }

}