package note.webnote.web.dto;

import lombok.Getter;
import lombok.Setter;
import note.webnote.domain.Member;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ViewNoteDto {

    private String title;
    private String content;
    private String hostMemberName;
    private Long hostMemberId;
    private Long noteId;
    private List<ParticipantDto> participantDtos = new ArrayList<>();

    public ViewNoteDto(String title, String content, String hostMemberName, Long hostMemberId, Long noteId) {
        this.title = title;
        this.content = content;
        this.hostMemberName = hostMemberName;
        this.hostMemberId = hostMemberId;
        this.noteId = noteId;
    }

    public void addParticipantDtos(ParticipantDto participantDto) {
        this.participantDtos.add(participantDto);
    }
}
