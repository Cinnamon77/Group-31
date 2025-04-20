package com.bookkeeping.service;

import com.bookkeeping.entity.dto.NoteDTO;
import com.bookkeeping.entity.model.Notes;

import java.util.List;

/**
 * 备忘笔记服务
 * @author bookkeeping
 */
public interface NoteService {

    /**
     * 新增笔记
     * @param userId 用户ID
     * @param dto 笔记内容
     */
    void addNote(Long userId, NoteDTO dto);

    /**
     * 删除笔记
     * @param userId 用户ID
     * @param noteId 笔记ID
     */
    void deleteNote(Long userId, Long noteId);

    /**
     * 获取笔记列表
     * @param userId 用户ID
     * @return 笔记列表
     */
    List<Notes> listNote(Long userId);
}
