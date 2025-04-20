package com.bookkeeping.entity.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 预算表
 * </p>
 *
 * @author bookkeeping
 * @since 2025-04-19
 */
@Getter
@Setter
@Schema(name = "Budget", description = "预算表")
public class Budget implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户 user.id")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "预算金额（分）")
    private Long amount;

    @Schema(description = "预算月份 yyyy-MM格式")
    private String budgetMonth;

    @Schema(description = "预算开始日期")
    private LocalDate startDate;

    @Schema(description = "预算结束日期")
    private LocalDate endDate;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @Schema(description = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
