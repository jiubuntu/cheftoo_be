package jwhs.cheftoo.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jwhs.cheftoo.auth.entity.QMember;
import jwhs.cheftoo.comment.dto.CommentResponseDto;
import jwhs.cheftoo.comment.entity.QComment;
import lombok.RequiredArgsConstructor;


import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentResponseDto> findByRecipeId(UUID recipeId) {
        QComment comment = QComment.comment;
        QMember member = QMember.member;

        return queryFactory
                .select(Projections.constructor(CommentResponseDto.class,
                    comment.commentId,
                    comment.recipe.recipeId,
                    comment.member.memberId,
                    member.nickname,
                    comment.commentContent,
                    comment.dataCreated
                ))
                .from(comment)
                .join(comment.member, member)
                .where(comment.recipe.recipeId.eq(recipeId))
                .fetch();

    }
}
