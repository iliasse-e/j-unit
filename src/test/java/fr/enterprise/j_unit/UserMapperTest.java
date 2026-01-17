package fr.enterprise.j_unit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    void testToDto() {
        UserEntity entity = new UserEntity(1L, "Alice", "alice@mail.com");

        UserDto dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Alice", dto.getName());
    }

    @Test
    void testToEntity() {
        UserDto dto = new UserDto(2L, "Bob");

        UserEntity entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("Bob", entity.getName());
        assertNull(entity.getEmail()); // logique du mapper
    }

    @Test
    void testToEntity_Null() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void testToEntity_InvalidName_ShouldThrowException() {
        UserDto dto = new UserDto(3L, "  ");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mapper.toEntity(dto)
        );

        assertEquals("User name cannot be null or empty", exception.getMessage());
    }
}
