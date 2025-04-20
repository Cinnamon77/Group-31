package com.bookkeeping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bookkeeping.core.api.ResultCode;
import com.bookkeeping.core.exception.Asserts;
import com.bookkeeping.entity.dto.NoteDTO;
import com.bookkeeping.entity.model.Notes;
import com.bookkeeping.mapper.NotesMapper;
import com.bookkeeping.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author bookkeeping
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NotesMapper notesMapper;

    @Override
    public void addNote(Long userId, NoteDTO dto) {
        Notes notes = new Notes();
        notes.setUserId(userId);
        notes.setContent(dto.getContent());
        notesMapper.insert(notes);
    }

    @Override
    public void deleteNote(Long userId, Long noteId) {
        Notes notes = notesMapper.selectById(noteId);
        if (notes == null || !notes.getUserId().equals(userId)) {
            Asserts.fail(ResultCode.CHECK_PARAMS_ERROR);
        }
        notesMapper.deleteById(noteId);
    }

    @Override
    public List<Notes> listNote(Long userId) {
        LambdaQueryWrapper<Notes> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notes::getUserId, userId);
        queryWrapper.orderByDesc(Notes::getId);
        return notesMapper.selectList(queryWrapper);
    }
}
