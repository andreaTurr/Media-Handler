package it.unimib.exercise.andrea.mediahandler.models.playlists;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class PlaylistPartial {
    // Used for Room
    @ColumnInfo(name = "id")
    private String id;
    @Embedded
    private PlaylistSnippet snippet;
    @Embedded
    private PlaylistContentDetails contentDetails;

    @Ignore
    public PlaylistPartial(Playlist playlist) {
        this.id = playlist.getId();
        this.snippet = playlist.getSnippet();
        this.contentDetails = playlist.getContentDetails();
    }

    public PlaylistPartial(String id, PlaylistSnippet snippet, PlaylistContentDetails contentDetails) {
        this.id = id;
        this.snippet = snippet;
        this.contentDetails = contentDetails;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PlaylistSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(PlaylistSnippet snippet) {
        this.snippet = snippet;
    }

    public PlaylistContentDetails getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(PlaylistContentDetails contentDetails) {
        this.contentDetails = contentDetails;
    }
}
