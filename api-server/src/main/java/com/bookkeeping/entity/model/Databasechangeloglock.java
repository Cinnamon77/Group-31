package com.bookkeeping.entity.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author bookkeeping
 * @since 2025-04-19
 */
@Getter
@Setter
@Schema(name = "Databasechangeloglock", description = "")
public class Databasechangeloglock implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Byte locked;

    private LocalDateTime lockgranted;

    private String lockedby;
}
