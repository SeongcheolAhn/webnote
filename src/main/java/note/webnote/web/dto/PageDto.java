package note.webnote.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter @Setter
public class PageDto {
    private int totalPages;
    private int pageNumber;
    private boolean hasNextPage;
    private boolean isHostPermission;

    public PageDto(int totalPages, int pageNumber, String isHost) {
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;

        hasNextPage = (totalPages-1) != pageNumber;
        isHostPermission = StringUtils.hasText(isHost);
    }
}
