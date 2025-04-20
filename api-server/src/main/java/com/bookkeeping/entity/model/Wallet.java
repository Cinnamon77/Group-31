package com.bookkeeping.entity.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 钱包信息
 * </p>
 *
 * @author bookkeeping
 * @since 2025-04-19
 */
@Getter
@Setter
@Schema(name = "Wallet", description = "钱包信息")
public class Wallet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户 user.id")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "账号类型名称")
    private String accountName;

    @Schema(description = "账号类型图标")
    private String accountIcon;

    @Schema(description = "钱包余额")
    private Long balance;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @Schema(description = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
