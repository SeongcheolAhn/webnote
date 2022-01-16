package note.webnote.repository;

import note.webnote.domain.MemberNote;

public interface MemberNoteRepository {

    void saveMemberNote(MemberNote memberNote);

    void removeMemberInNote(MemberNote memberNote);
}
