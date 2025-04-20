package com.bookkeeping.entity.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 交易记录表
 * </p>
 *
 * @author bookkeeping
 * @since 2025-04-19
 */
@Getter
@Setter
@TableName("transaction_record")
@Schema(name = "TransactionRecord", description = "交易记录表")
public class TransactionRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户 user.id")
    private Long userId;

    @Schema(description = "用户账号")
    private String username;

    @Schema(description = "交易金额（分）")
    private Long amount;

    @Schema(description = "交易日期")
    private LocalDate transactionDate;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "分类图标")
    private String categoryIcon;

    @Schema(description = "账号类型名称")
    private String accountName;

    @Schema(description = "账号类型图标")
    private String accountIcon;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @Schema(description = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
