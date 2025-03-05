package com.chronelab.riscc.controller;

import com.chronelab.riscc.dto.PaginationReqDto;
import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.dto.ServiceResponseUtil;
import com.chronelab.riscc.dto.request.NoteReq;
import com.chronelab.riscc.dto.response.NoteRes;
import com.chronelab.riscc.service.NoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/note")
@Api(tags = "Note")
public class NoteController {

    private final NoteService noteService;
    private final ServiceResponseUtil<NoteRes> noteResServiceResponseUtil;

    @Autowired
    public NoteController(NoteService noteService, ServiceResponseUtil<NoteRes> noteResServiceResponseUtil) {
        this.noteService = noteService;
        this.noteResServiceResponseUtil = noteResServiceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Note.")
    public ResponseEntity<ServiceResponse> save(@RequestBody NoteReq note) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Note saved.")
                        .addData("note", noteService.save(note))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Note list. If pagination detail is not provided then all the Note will be fetched.")
    public ResponseEntity<ServiceResponse> getPaginated(PaginationReqDto pagination) {

        return ResponseEntity.ok(noteResServiceResponseUtil.buildServiceResponse(
                true,
                "Notes retrieved",
                noteService.get(pagination)
                )
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing Note.")
    public ResponseEntity<ServiceResponse> update(@RequestBody NoteReq note) {
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Note updated.")
                        .addData("note", noteService.update(note))
        );
    }

    @DeleteMapping(value = "/{noteId}")
    @ApiOperation(value = "Delete an existing Note.")
    public ResponseEntity<ServiceResponse> delete(@PathVariable Long noteId) {
        noteService.delete(noteId);
        return ResponseEntity.ok(
                new ServiceResponse()
                        .setStatus(true)
                        .setMessage("Note deleted.")
        );
    }
}
