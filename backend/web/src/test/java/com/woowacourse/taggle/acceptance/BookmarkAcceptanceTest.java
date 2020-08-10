package com.woowacourse.taggle.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import com.woowacourse.taggle.setup.domain.TagSetup;
import com.woowacourse.taggle.tag.domain.Tag;
import com.woowacourse.taggle.tag.dto.BookmarkResponse;
import com.woowacourse.taggle.tag.dto.BookmarkTagResponse;

public class BookmarkAcceptanceTest extends AcceptanceTest {

    @Autowired
    private TagSetup tagSetup;

    @Transactional
    @WithMockUser(roles = "ADMIN")
    @Test
    void manageBookmark() {
        // 북마크를 생성한다.
        createBookmark("http://taggle.com");
        List<BookmarkResponse> bookmarks = findBookmarks();

        assertThat(bookmarks).hasSize(1);

        // 태그에 북마크를 추가한다.
        final Long bookmarkId = bookmarks.get(0).getId();
        final Tag tag = tagSetup.save();
        addBookmarkOnTag(tag.getId(), bookmarkId);

        // 북마크에 있는 태그를 조회한다.
        final BookmarkTagResponse bookmark = findBookmark(bookmarkId);

        assertThat(bookmark.getId()).isEqualTo(bookmarkId);
        assertThat(bookmark.getTags()).hasSize(1);

        // 북마크를 제거한다.
        removeBookmark(bookmarkId);

        bookmarks = findBookmarks();
        assertThat(bookmarks).hasSize(0);
    }

    public void createBookmark(final String url) {
        final Map<String, String> request = new HashMap<>();
        request.put("url", url);

        post("/api/v1/bookmarks", request, "/api/v1/bookmarks");
    }

    public List<BookmarkResponse> findBookmarks() {
        return getAsList("/api/v1/bookmarks", BookmarkResponse.class);
    }

    public BookmarkTagResponse findBookmark(final Long id) {
        return get("/api/v1/bookmarks/" + id + "/tags", BookmarkTagResponse.class);
    }

    public void removeBookmark(final Long id) {
        delete("/api/v1/bookmarks/" + id);
    }

    public void addBookmarkOnTag(final Long tagId, final Long bookmarkId) {
        post("/api/v1/tags/" + tagId + "/bookmarks/" + bookmarkId, new HashMap<>(), "/api/v1/tags/");
    }
}