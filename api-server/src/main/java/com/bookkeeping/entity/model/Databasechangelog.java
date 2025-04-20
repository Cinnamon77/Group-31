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
@Schema(name = "Databasechangelog", description = "")
public class Databasechangelog implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String author;

    private String filename;

    private LocalDateTime dateexecuted;

    private Integer orderexecuted;

    private String exectype;

    private String md5sum;

    private String description;

    private String comments;

    private String tag;

    private String liquibase;

    private String contexts;

    private String labels;

    private String deploymentId;
}
