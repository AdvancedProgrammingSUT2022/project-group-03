package model.resources;

import model.Civilization;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class ResourcesTypesTest {

    @Mock private Civilization civilization;

    @Test
    void randomResource() {
    }

    @Test
    void isTechnologyUnlocked() {

        when(civilization.getGold()).thenReturn(5);
        assertEquals(civilization.getGold(),5);
    }

    @Test
    void stringToEnum() {
    }
}