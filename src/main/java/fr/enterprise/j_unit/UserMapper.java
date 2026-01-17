package fr.enterprise.j_unit;

public class UserMapper {

    public UserDto toDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return new UserDto(
                entity.getId(),
                entity.getName()
        );
    }

    public UserEntity toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }

        return new UserEntity(
                dto.getId(),
                dto.getName(),
                null
        );
    }
}
