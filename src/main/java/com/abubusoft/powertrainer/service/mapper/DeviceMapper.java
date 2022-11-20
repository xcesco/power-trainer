package com.abubusoft.powertrainer.service.mapper;

import com.abubusoft.powertrainer.domain.*;
import com.abubusoft.powertrainer.service.dto.DeviceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Device} and its DTO {@link DeviceDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeviceMapper extends EntityMapper<DeviceDTO, Device> {}
