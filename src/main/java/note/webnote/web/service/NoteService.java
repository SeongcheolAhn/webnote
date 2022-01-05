package note.webnote.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import note.webnote.domain.Member;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Note;
import note.webnote.domain.Permission;
import note.webnote.repository.MemberRepository;
import note.webnote.repository.NoteRepository;
import note.webnote.web.dto.ParticipantDto;
import note.webnote.web.dto.ViewNoteDto;
import note.webnote.web.form.NoteSaveForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteService {

    private final NoteRepository noteRepository;
    private final MemberRepository memberRepository;

    public Long saveNote(Note note) {
        noteRepository.save(note);
        return note.getId();
    }

    public Optional<Note> findOne(Long id) {
        return noteRepository.findById(id);
    }

    /* Note 엔티티와 MemberNote 모두 저장 */
    @Transactional
    public Long saveMemberNoteWithNote(NoteSaveForm form) {
        Note note = new Note(form.getTitle(), form.getContent());
        Long noteId = saveNote(note);

        // form 저장 마무리
        form.setNoteId(noteId);

        // MemberNote 저장
        Optional<Member> member = memberRepository.findById(form.getMemberId());
        if (member.isEmpty()) {
            log.info("존재하지 않는 회원입니다.");
            return null;
        }

        MemberNote memberNote = new MemberNote(member.get(), note, Permission.HOST);
        noteRepository.saveMemberNote(memberNote);
        return noteId;
    }


    /**
     * 노트 1개 조회시 필요한 정보를 dto로 반환한다.
     */
    public ViewNoteDto findNote(Long noteId) {
        Optional<Note> findNote = noteRepository.findById(noteId);
        if(findNote.isEmpty()) {
            return null;
        }

        Note note = findNote.get();
        Member member = note.getMemberNote().stream()
                .filter(mn -> mn.getPermission() == Permission.HOST)
                .findFirst().get().getMember();
        ViewNoteDto viewNoteDto = new ViewNoteDto(note.getTitle(), note.getContent(),
                member.getName(), member.getId(), note.getId());

        // 노트 참여자 ParticipantDto 추가
        List<MemberNote> memberNote = note.getMemberNote();

        List<Member> members = memberNote.stream()
                .filter(mn -> mn.getPermission() != Permission.HOST)
                .map(MemberNote::getMember)
                .collect(Collectors.toList());

        for (Member m : members) {

            Permission permission = memberNote.stream()
                    .filter(mn -> mn.getMember().getId().equals(m.getId()))
                    .findFirst().map(MemberNote::getPermission).get();
            ParticipantDto participantDto = new ParticipantDto(m.getId(), m.getName(), permission);

            viewNoteDto.addParticipantDtos(participantDto);
        }

        return viewNoteDto;
    }
}
