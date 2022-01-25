package note.webnote.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import note.webnote.domain.MemberNote;
import note.webnote.domain.Permission;
import note.webnote.domain.QMemberNote;
import note.webnote.web.dto.MemberHomeCondition;
import note.webnote.web.dto.MemberHomeMemberNoteDto;
import note.webnote.web.dto.QMemberHomeMemberNoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.count;
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
    public Page<MemberHomeMemberNoteDto> findMemberNoteDto(Long memberId, MemberHomeCondition condition, Pageable pageable) {
        List<MemberHomeMemberNoteDto> content = queryFactory
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리 분리
        JPAQuery<Long> countQuery = queryFactory
                .select(count(memberNote))
                .from(memberNote)
                .where(
                        memberEq(memberId),
                        permissionHOSTEq(condition.getPermission())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression memberEq(Long memberId) {
        return memberId != null ? memberNote.member.id.eq(memberId) : null;

    }

    private BooleanExpression permissionHOSTEq(String permission) {
        return StringUtils.hasText(permission) ? memberNote.permission.eq(Permission.HOST) : null;
    }

}
