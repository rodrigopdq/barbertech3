package api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import api.dto.BarbeiroDto;
import api.model.Barbeiro;

@Mapper(componentModel = "spring")
public interface BarbeiroMapper {
    BarbeiroMapper INSTANCE = Mappers.getMapper(BarbeiroMapper.class);
    
    BarbeiroDto toDto(Barbeiro barbeiro);
    Barbeiro toModel(BarbeiroDto barbeiroDto);
}