package note.webnote.service;

import note.webnote.domain.Member;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Note;
import note.webnote.domain.Permission;
import note.webnote.repository.MemberNoteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberNoteServiceTest {

    @Autowired MemberNoteService memberNoteService;
    @Autowired EntityManager em;
    @Autowired MemberService memberService;
    @Autowired NoteService noteService;


    @Test
    void 멤버노트_저장() {

        // given
        Member memberA = new Member("loginA", "1234", "memberA");
        memberService.join(memberA);

        Note noteA = new Note("NoteA", "aaaa");
        noteService.saveNote(noteA);

        MemberNote memberNoteA = new MemberNote(memberA, noteA, Permission.HOST);

        // when
        memberNoteService.saveMemberNote(memberNoteA);

        // then
        MemberNote findMemberNoteA = em.find(MemberNote.class, memberNoteA.getId());

        assertThat(findMemberNoteA.getMember()).isEqualTo(memberA);
        assertThat(findMemberNoteA.getNote()).isEqualTo(noteA);
    }

    @Test
    void 멤버노트_조회() {

        // given
        Member memberA = new Member("loginA", "1234", "memberA");
        memberService.join(memberA);

        Note noteA = new Note("NoteA", "aaaa");
        noteService.saveNote(noteA);

        MemberNote memberNoteA = new MemberNote(memberA, noteA, Permission.HOST);
        Long memberNoteId = memberNoteService.saveMemberNote(memberNoteA);

        // when
        Long findMemberNoteId = memberNoteService.findOne(memberNoteId)
                .map(MemberNote::getId)
                .orElse(0L);

        // then
        assertThat(findMemberNoteId).isEqualTo(memberNoteId);
    }

    @Test
    void 멤버가_가진_모든_노트_조회() {

        // give
        // 한 명의 멤버
        Member memberA = new Member("loginA", "1234", "memberA");
        memberService.join(memberA);

        // 두 개의 노트
        Note noteA = new Note("NoteA", "aaaa");
        noteService.saveNote(noteA);
        Note noteB = new Note("NoteB", "bbbb");
        noteService.saveNote(noteB);

        MemberNote memberNoteA = new MemberNote(memberA, noteA, Permission.HOST);
        memberNoteService.saveMemberNote(memberNoteA);
        MemberNote memberNoteB = new MemberNote(memberA, noteB, Permission.HOST);
        memberNoteService.saveMemberNote(memberNoteB);

        // when
        List<MemberNote> findMemberNotes = memberNoteService.findByMember(memberA);

        // then
        assertThat(findMemberNotes.size()).isEqualTo(2);

    }
}