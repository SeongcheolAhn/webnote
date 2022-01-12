package note.webnote.web.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import note.webnote.domain.Member;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Note;
import note.webnote.domain.Permission;
import note.webnote.repository.MemberRepository;
import note.webnote.repository.NoteRepository;
import note.webnote.web.dto.EditPermissionDto;
import note.webnote.web.dto.ParticipantsDto;
import note.webnote.web.dto.ViewNoteParticipantDto;
import note.webnote.web.dto.ViewNoteDto;
import note.webnote.web.form.EditNoteForm;
import note.webnote.web.form.NoteSaveForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NoteService {

    private final NoteRepository noteRepository;
    private final MemberRepository memberRepository;
    private final LoginService loginService;

    public Long saveNote(Note note) {
        noteRepository.save(note);
        return note.getId();
    }

    public Optional<Note> findOne(Long id) {
        return noteRepository.findById(id);
    }

    @Transactional
    public void saveEditNote(Long noteId, EditNoteForm editNoteForm) {
        Note note = findOne(noteId).get();
        note.editTitle(editNoteForm.getTitle());
        note.editContent(editNoteForm.getContent());
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
     * viewNote 페이지에서 필요한 dto를 생성한다.
     */
    public ViewNoteDto findNote(Long noteId, Long hostId) {

        Optional<Note> findNote = noteRepository.findById(noteId);
        if(findNote.isEmpty()) {
            return null;
        }

        // ViewNoteDto 생성
        Note note = findNote.get();
        return new ViewNoteDto(note, hostId);

    }

    /**
     * 회원이 속한 노트의 hostId를 찾아준다.
     */
    public Long findHost(Note note, Long memberId) {
        MemberNote memberNote = note.getMemberNote().stream()
                .filter(mn -> mn.getPermission() == Permission.HOST)
                .findFirst().get();
        return memberNote.getMember().getId();
    }


    /**
     * 회원 권한 수정 페이지에 필요한 Dto 반환
     */
    public EditPermissionDto findEditPermissionDto(Long editMemberId, Long noteId) {
        Optional<Note> findNote = findOne(noteId);
        if(findNote.isEmpty()) {
            return null;
        }
        Note note = findNote.get();
        MemberNote memberNote = note.getMemberNote().stream()
                .filter(mn -> mn.getMember().getId().equals(editMemberId))
                .findFirst().get();

        return new EditPermissionDto(editMemberId, memberNote.getMember().getName(), memberNote.getPermission().toString());
    }

    /**
     * 권한 변경 저장
     */
    @Transactional
    public void editPermission(Long noteId, Long editMemberId, EditPermissionDto dto) {
        Note note = findOne(noteId).get();
        MemberNote memberNote = note.getMemberNote().stream()
                .filter(mn -> mn.getMember().getId().equals(editMemberId))
                .findFirst().get();
        if (dto.getEditPermission().equals(Permission.READ_WRITE.toString())) {
            memberNote.editPermission(Permission.READ_WRITE);
        } else {
            memberNote.editPermission(Permission.READ_ONLY);
        }
    }

    /**
     * 참여자 수정 페이지 - 참여자 목록 반환
     */
    public ParticipantsDto participants(Long noteId, Long loginId) {
        Optional<Note> findNote = findOne(noteId);
        if(findNote.isEmpty()) {
            log.info("노트 없음");
        }
        Note note = findNote.get();
        return new ParticipantsDto(note, loginId);
    }

    /**
     * 참여자 삭제
     */
    @Transactional
    public void deleteMember(Long loginId, Long noteId, Long deleteMemberId
    , HttpServletRequest request) {

        // 삭제 권한 확인
        Long findLoginId = loginService.checkMember(request);
        if (!findLoginId.equals(loginId)) {
            log.info("잘못된 접근");
            return;
        }

        // 노트에서 회원 삭제
        Optional<Note> findNote = findOne(noteId);
        if(findNote.isEmpty()) {
            log.info("노트 없음");
            return;
        }
        Note note = findNote.get();

        Optional<MemberNote> result = note.getMemberNote().stream()
                .filter(mn -> mn.getMember().getId().equals(deleteMemberId))
                .findFirst();
        result.ifPresent(noteRepository::removeMemberNote);
    }
}
