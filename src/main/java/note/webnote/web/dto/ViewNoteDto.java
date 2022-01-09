package note.webnote.web.dto;

import lombok.Getter;
import lombok.Setter;
import note.webnote.domain.Note;
import note.webnote.domain.Permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class ViewNoteDto {

    private Long noteId;
    private String title;
    private String content;
    private List<ViewNoteParticipantDto> viewNoteParticipantDtos = new ArrayList<>();
    private String hostMemberName;
    private Long hostMemberId;


    public ViewNoteDto(Note note, Long hostId) {

        noteId = note.getId();
        title = note.getTitle();
        content = note.getContent();
        viewNoteParticipantDtos = note.getMemberNote().stream()
                .map(ViewNoteParticipantDto::new)
                .collect(Collectors.toList());

        // 참가자중 HOST 권한 찾기
        Optional<ViewNoteParticipantDto> host = viewNoteParticipantDtos.stream()
                .filter(ViewNoteParticipantDto -> ViewNoteParticipantDto.getMemberId().equals(hostId))
                .findFirst();
        host.ifPresent(viewNoteParticipantDto -> {
            hostMemberName = viewNoteParticipantDto.getMemberName();
            hostMemberId = viewNoteParticipantDto.getMemberId();
        });
    }

}
