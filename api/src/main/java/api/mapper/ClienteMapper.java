package api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import api.dto.ClienteDto;
import api.model.Cliente;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);
    
    ClienteDto toDto(Cliente cliente);
    Cliente toModel(ClienteDto clienteDto);
}