package model;

import java.io.File;

public class Song {
    private final String fileName;
    private final String displayName;
    private final String fileFormat;
    private long duration;
    private final File file;

    public Song(String fileName, File file) {
        this(fileName, file, extractDisplayName(fileName));
    }

    public Song(String fileName, File file, String displayName) {
        this.fileName = fileName;
        this.file = file;
        this.displayName = (displayName == null || displayName.isBlank())
            ? extractDisplayName(fileName)
            : displayName;
        this.fileFormat = getFileFormat(fileName);
        this.duration = 0;
    }

    private static String extractDisplayName(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(0, lastDot) : fileName;
    }

    private String getFileFormat(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            return fileName.substring(lastDot + 1).toLowerCase();
        }
        return "unknown";
    }

    public boolean fileExists() {
        return file != null && file.exists();
    }

    public String getAbsolutePath() {
        return file != null ? file.getAbsolutePath() : null;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return displayName + " (" + fileFormat.toUpperCase() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Song other)) {
            return false;
        }
        return this.fileName.equals(other.fileName);
    }

    @Override
    public int hashCode() {
        return fileName.hashCode();
    }
}
