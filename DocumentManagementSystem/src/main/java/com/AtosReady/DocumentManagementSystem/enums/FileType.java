package com.AtosReady.DocumentManagementSystem.enums;

public enum FileType {

    Pdf(new String[]{"application/pdf"}),
    Image(new String[]{"image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp", "image/tiff", "image/svg+xml"}),
    Word(new String[]{"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"}),
    Excel(new String[]{"application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}),
    Presentation(new String[]{"application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", "application/vnd.openxmlformats-officedocument.presentationml.slideshow"}),
    Text(new String[]{"text/plain", "text/csv", "text/html", "text/css", "text/javascript", "application/json", "application/xml"}),
    Video(new String[]{"video/mp4", "video/x-ms-wmv", "video/x-flv", "video/webm", "video/avi", "video/mpeg"}),
    Audio(new String[]{"audio/mpeg", "audio/x-wav"}),
    Archive(new String[]{"application/zip", "application/x-zip-compressed"}),

    Folder(new String[]{"application/vnd.google-apps.folder"}),

    Unknown(new String[]{"-"});

    private final String[] mimeTypes;

    FileType(String[] mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    public String[] getValues() {
        return mimeTypes;
    }

    /**
     * Get the correct FileType based on MIME type.
     *
     * @param mimeType The MIME type to match.
     * @return The corresponding FileType or FileType.Unknown if no match is found.
     */
    public static FileType fromMimeType(String mimeType) {
        for (FileType fileType : FileType.values())
            for (String type : fileType.getValues())
                if (type.equalsIgnoreCase(mimeType))
                    return fileType;

        return FileType.Unknown;
    }
}