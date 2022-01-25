package note.webnote.repository;

import note.webnote.domain.MemberNote;
import note.webnote.web.dto.MemberHomeCondition;
import note.webnote.web.dto.MemberHomeMemberNoteDto;

import java.util.List;

public interface MemberNoteRepository {

    void saveMemberNote(MemberNote memberNote);

    void removeMemberInNote(MemberNote memberNote);

    List<MemberHomeMemberNoteDto> findMemberNoteDto(Long memberId, MemberHomeCondition condition);
}
