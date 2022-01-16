package note.webnote.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import note.webnote.domain.Member;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Note;
import note.webnote.web.dto.MemberHomeDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NoteRepositoryOld {

    private final EntityManager em;

    // 완료
    public void save(Note note) {
        em.persist(note);
    }

    // 완료
    public void saveMemberNote(MemberNote memberNote) {
        em.persist(memberNote);
    }

    // 완료
    /**
     * 노트 조회
     */
    public Optional<Note> findById(Long id) {
        return Optional.ofNullable(em.find(Note.class, id));
    }

    // 완료
    /**
     * 파라미터로 전달된 멤버의 모든 노트 조회
     */
    public MemberHomeDto findAllNoteByMember(Member member) {
        log.info("멤버가 가진 노트 전부 조회");
        return new MemberHomeDto(member);
    }

    // 완료
    /**
     * 노트에서 멤버 삭제
     */
    public void removeMemberInNote(MemberNote memberNote) {
        em.remove(memberNote);
    }
}
