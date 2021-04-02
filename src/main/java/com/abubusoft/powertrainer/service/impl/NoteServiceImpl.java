package com.abubusoft.powertrainer.service.impl;

import com.abubusoft.powertrainer.domain.Note;
import com.abubusoft.powertrainer.repository.NoteRepository;
import com.abubusoft.powertrainer.service.NoteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Note}.
 */
@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    private final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Note save(Note note) {
        log.debug("Request to save Note : {}", note);
        return noteRepository.save(note);
    }

    @Override
    public Optional<Note> partialUpdate(Note note) {
        log.debug("Request to partially update Note : {}", note);

        return noteRepository
            .findById(note.getId())
            .map(
                existingNote -> {
                    if (note.getUuid() != null) {
                        existingNote.setUuid(note.getUuid());
                    }
                    if (note.getType() != null) {
                        existingNote.setType(note.getType());
                    }
                    if (note.getUrl() != null) {
                        existingNote.setUrl(note.getUrl());
                    }
                    if (note.getImage() != null) {
                        existingNote.setImage(note.getImage());
                    }
                    if (note.getImageContentType() != null) {
                        existingNote.setImageContentType(note.getImageContentType());
                    }
                    if (note.getDescription() != null) {
                        existingNote.setDescription(note.getDescription());
                    }

                    return existingNote;
                }
            )
            .map(noteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Note> findAll(Pageable pageable) {
        log.debug("Request to get all Notes");
        return noteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Note> findOne(Long id) {
        log.debug("Request to get Note : {}", id);
        return noteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Note : {}", id);
        noteRepository.deleteById(id);
    }
}
