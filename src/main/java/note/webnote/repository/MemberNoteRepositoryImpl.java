package note.webnote.repository;

import lombok.RequiredArgsConstructor;
import note.webnote.domain.MemberNote;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
public class MemberNoteRepositoryImpl implements MemberNoteRepository {

    private final EntityManager em;

    @Override
    public void saveMemberNote(MemberNote memberNote) {
        em.persist(memberNote);
    }

    /**
     * 노트에서 멤버 삭제
     */
    @Override
    public void removeMemberInNote(MemberNote memberNote) {
        em.remove(memberNote);
    }
}
