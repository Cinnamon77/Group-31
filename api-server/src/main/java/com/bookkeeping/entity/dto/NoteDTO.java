package com.bookkeeping.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author bookkeeping
 **/
@Data
@Schema(description = "备忘笔记")
public class NoteDTO {
    @Schema(description = "备忘内容")
    private String content;
}
