package org.cryptomator.presentation.util;

import org.cryptomator.presentation.R;
import org.cryptomator.presentation.util.FileUtil.FileInfo;

import java.util.Optional;
import java.util.function.Predicate;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public enum FileIcon {

	ARCHIVE(R.drawable.node_file_archive, //
			forExtensions("7z", "bz2", "bzip2", "gz", "gzip", "rar", "tar", "zip")), //
	AUDIO(R.drawable.node_file_audio, //
			forMediatype("audio")), //
	MARKUP(R.drawable.node_file_html, //
			forExtensions("html", "xhtml", "xml", "xsl", "xslt")), //
	IMAGE(R.drawable.node_file_image, //
			forMediatype("image")), //
	MOVIE(R.drawable.node_file_movie, //
			forMediatype("video")), //
	PDF(R.drawable.node_file_pdf, //
			forExtensions("pdf", "ps")), //
	SLIDES(R.drawable.node_file_presentation, //
			forExtensions("key", "keynote", "odp", "ppt", "pot", "pps", "ppa", "pptx", "potx", "ppsx", "ppam", "pptm", "potm", "ppsm")), //
	SOURCECODE(R.drawable.node_file_sourcecode, //
			forExtensions("bat", "c", "cs", "cpp", "coffee", "d", "e", "for", "go", "h", "java", "js", "lua", "php", "pl", "ps1", "py", "r", "rb", "sh", "vb", "vbs")), //
	SPREADSHEET(R.drawable.node_file_spreadsheet, //
			forExtensions("csv", "numbers", "ods", "ots", "xls", "xlt", "xla", "xlsx", "xltx", "xlsm", "xltm", "xlam", "xlsb")), //
	TEXT(R.drawable.node_file_text, //
			forMediaTypeOrExtensions("text", "md", "todo", "odts", "ods", "doc", "dot", "docx", "dotx", "docm", "dotm")), //
	VAULT(R.drawable.node_vault, //
			forExtensions("cryptomator")), //
	UNKNOWN(R.drawable.node_file_unknown);

	private final int iconResource;
	private final Predicate<FileInfo>[] predicates;

	FileIcon(int iconResource, Predicate<FileInfo>... predicates) {
		this.iconResource = iconResource;
		this.predicates = predicates;
	}

	public static Optional<FileIcon> knownFileIconFor(String name, FileUtil fileUtil) {
		FileInfo fileInfo = fileUtil.fileInfo(name);
		for (FileIcon icon : values()) {
			if (icon.matches(fileInfo)) {
				return Optional.of(icon);
			}
		}
		return Optional.empty();
	}

	public static FileIcon fileIconFor(String name, FileUtil fileUtil) {
		return knownFileIconFor(name, fileUtil).orElse(UNKNOWN);
	}

	private static Predicate<FileInfo> forExtensions(final String... extensions) {
		return fileInfo -> {
			if (fileInfo.getExtension() == null) {
				return FALSE;
			}

			for (String extension : extensions) {
				if (fileInfo.getExtension().equalsIgnoreCase(extension)) {
					return TRUE;
				}
			}
			return FALSE;
		};
	}

	private static Predicate<FileInfo> forMediatype(final String mediatype) {
		return fileInfo -> fileInfo.getMimeType().getMediatype().equalsIgnoreCase(mediatype);
	}

	private static Predicate<FileInfo> forMediaTypeOrExtensions(final String mediatype, final String... extensions) {
		return forMediatype(mediatype).or(forExtensions(extensions));
	}

	private boolean matches(FileInfo fileInfo) {
		for (Predicate<FileInfo> predicate : predicates) {
			if (predicate.test(fileInfo)) {
				return true;
			}
		}
		return false;
	}

	public int getIconResource() {
		return iconResource;
	}
}
