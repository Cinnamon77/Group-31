package com.bookkeeping.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author bookkeeping
 **/
@Data
@Builder
@Schema(name = "UserinfoVO", description = "用户信息")
public class UserinfoVO {

    @Schema(description = "用户 id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "角色")
    private List<String> roles;
}
