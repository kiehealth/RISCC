package com.chronelab.riscc.dto.util;

import com.chronelab.riscc.dto.request.NoteReq;
import com.chronelab.riscc.dto.response.NoteRes;
import com.chronelab.riscc.entity.NoteEntity;
import org.springframework.stereotype.Component;

@Component
public class NoteDtoUtil implements DtoUtil<NoteEntity, NoteReq, NoteRes> {
    @Override
    public NoteEntity reqToEntity(NoteReq noteReq) {
        return new NoteEntity()
                .setTitle(noteReq.getTitle())
                .setDescription(noteReq.getDescription());
    }

    @Override
    public NoteRes entityToRes(NoteEntity noteEntity) {
        return new NoteRes()
                .setId(noteEntity.getId())
                .setTitle(noteEntity.getTitle())
                .setDescription(noteEntity.getDescription())
                .setCreatedDateTime(noteEntity.getCreatedDate());
    }

    @Override
    public NoteRes prepRes(NoteEntity noteEntity) {
        return entityToRes(noteEntity);
    }

    @Override
    public void setUpdatedValue(NoteReq noteReq, NoteEntity noteEntity) {
        if (noteReq != null && noteEntity != null) {
            if (noteReq.getTitle() != null && !noteReq.getTitle().equals(noteEntity.getTitle())) {
                noteEntity.setTitle(noteReq.getTitle());
            }
            if (noteReq.getDescription() != null
                    && (noteEntity.getDescription() == null || !noteReq.getDescription().equals(noteEntity.getDescription()))) {
                noteEntity.setDescription(noteReq.getDescription());
            }
        }
    }
}
