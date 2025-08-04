package jwhs.cheftoo.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jwhs.cheftoo.auth.entity.Member;
import jwhs.cheftoo.auth.entity.QMember;
import jwhs.cheftoo.comment.dto.CommentResponseDetailDto;
import jwhs.cheftoo.comment.dto.CommentResponseDto;
import jwhs.cheftoo.comment.entity.QComment;
import jwhs.cheftoo.image.entity.QImages;
import jwhs.cheftoo.image.enums.S3ImageType;
import jwhs.cheftoo.image.service.S3Service;
import jwhs.cheftoo.recipe.entity.QRecipe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import java.net.URL;
import java.util.List;
import java.util.UUID;

import static jwhs.cheftoo.auth.entity.QMember.member;

@RequiredArgsConstructor
@Slf4j
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final S3Service s3Service;

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

    @Override
    public List<CommentResponseDetailDto> findAllByMember(UUID memberId) {
        QRecipe recipe = QRecipe.recipe;
        QComment comment = QComment.comment;
        QImages images = QImages.images;
        QMember member = QMember.member;

        return queryFactory
                .select(Projections.constructor(CommentResponseDetailDto.class,
                        comment.commentId,
                        recipe.recipeId,
                        recipe.recipeTitle,
                        images.imgPath,
                        member.memberId,
                        member.nickname,
                        comment.commentContent,
                        comment.dataCreated
                ))
                .from(comment, member, recipe, images)
                .where(
                        comment.member.memberId.eq(member.memberId)
                                .and(comment.recipe.recipeId.eq(recipe.recipeId))
                                .and(member.memberId.eq(memberId))
                                .and(images.recipe.recipeId.eq(recipe.recipeId))
                )
                .fetch()
                .stream()
                .map(dto -> {
                    String key = dto.getImgPath();
                    if (key != null && !key.isBlank()) {
                        try {
                            URL presignedUrl = s3Service.generateRecipeImagePresignedGetUrl(key, S3ImageType.RECIPE_GET_DURATION);
                            dto.setImgPath(presignedUrl.toString());
                        } catch ( Exception e ) {
                            log.error("presignedUrl 발급 실패 - key: {}", key, e);
                            dto.setImgPath(null);
                        }
                    }
                    return dto;
                })
                .toList();

    }
}
