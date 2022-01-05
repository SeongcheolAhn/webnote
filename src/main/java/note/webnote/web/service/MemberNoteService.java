package note.webnote.web.service;

import lombok.RequiredArgsConstructor;
import note.webnote.domain.Member;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Note;
import note.webnote.repository.MemberNoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberNoteService {

    private final MemberNoteRepository memberNoteRepository;

    @Transactional
    public Long saveMemberNote(MemberNote memberNote) {
        memberNoteRepository.save(memberNote);
        return memberNote.getId();
    }

    public Optional<MemberNote> findOne(Long id) {
        return memberNoteRepository.findById(id);
    }

    /**
     * 멤버가 속해있는 노트 전부 조회
     */
    public List<MemberNote> findByMember(Member member) {
        return memberNoteRepository.findByMember(member);
    }
}
