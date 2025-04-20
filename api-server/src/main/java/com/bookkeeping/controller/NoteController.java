package com.bookkeeping.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.bookkeeping.core.api.CommonResult;
import com.bookkeeping.entity.dto.NoteDTO;
import com.bookkeeping.entity.model.Notes;
import com.bookkeeping.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author bookkeeping
 **/
@Tag(name = "备忘笔记")
@RequestMapping(value = "api/note")
@RestController(value = "NoteController")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @Operation(summary = "新增")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<Void> addNote(@Validated @RequestBody NoteDTO dto) {
        noteService.addNote(StpUtil.getLoginIdAsLong(), dto);
        return CommonResult.success();
    }

    @Operation(summary = "删除")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<Void> deleteNote(@PathVariable("id") Long noteId) {
        noteService.deleteNote(StpUtil.getLoginIdAsLong(), noteId);
        return CommonResult.success();
    }

    @Operation(summary = "列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<Notes>> listNote() {
        return CommonResult.success(noteService.listNote(StpUtil.getLoginIdAsLong()));
    }
}
