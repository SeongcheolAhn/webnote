package note.webnote.web.dto;

import lombok.Getter;
import lombok.Setter;
import note.webnote.domain.Note;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class ParticipantsDto {
    private Long loginId;
    private Long noteId;
    private List<ViewNoteParticipantDto> participants;

    public ParticipantsDto(Note note, Long loginId) {
        this.loginId = loginId;
        noteId = note.getId();
        participants = note.getMemberNote().stream()
                .filter(mn -> !Objects.equals(mn.getMember().getId(), loginId))
                .map(ViewNoteParticipantDto::new)
                .collect(Collectors.toList());

    }
}
