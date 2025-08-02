package jwhs.cheftoo.auth.port;

import jwhs.cheftoo.auth.entity.Member;

import java.util.UUID;

public interface MemberReader {
    public Member getById(UUID memberId);
}
