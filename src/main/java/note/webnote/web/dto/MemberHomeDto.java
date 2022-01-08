package note.webnote.web.dto;

import lombok.Getter;
import note.webnote.domain.Member;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MemberHomeDto {

    private Long memberId;
    private List<MemberHomeMemberNoteDto> memberHomeMemberNoteDtos;

    public MemberHomeDto(Member member) {
        memberId = member.getId();
        memberHomeMemberNoteDtos = member.getMemberNotes().stream()
                .map(MemberHomeMemberNoteDto::new)
                .collect(Collectors.toList());
    }
}
