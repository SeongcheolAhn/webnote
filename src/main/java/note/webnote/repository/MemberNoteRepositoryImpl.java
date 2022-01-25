package note.webnote.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Permission;
import note.webnote.domain.QMemberNote;
import note.webnote.web.dto.MemberHomeCondition;
import note.webnote.web.dto.MemberHomeMemberNoteDto;
import note.webnote.web.dto.QMemberHomeMemberNoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static note.webnote.domain.QMember.member;
import static note.webnote.domain.QMemberNote.*;
import static note.webnote.domain.QNote.*;


public class MemberNoteRepositoryImpl implements MemberNoteRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public MemberNoteRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

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

    /**
     * 회원의 노트 목록 조회
     */
    @Override
    public List<MemberHomeMemberNoteDto> findMemberNoteDto(Long memberId, MemberHomeCondition condition) {
        return queryFactory
                .select(new QMemberHomeMemberNoteDto(
                        note.id,
                        note.title,
                        note.lastModifiedDate,
                        memberNote.permission
                ))
                .from(memberNote)
                .join(memberNote.note, note)
                .where(
                        memberEq(memberId),
                        permissionHOSTEq(condition.getPermission())
                )
                .fetch();
    }

    private BooleanExpression memberEq(Long memberId) {
        return memberId != null ? memberNote.member.id.eq(memberId) : null;

    }

    private BooleanExpression permissionHOSTEq(String permission) {
        return StringUtils.hasText(permission) ? memberNote.permission.eq(Permission.HOST) : null;
    }

}
