package com.ylsislove.tomdog.connector.http.enumeration;

public enum ContentType {

	CSS("CSS"),
	JS("JS"),
	GIF("GIF"),
	HTM("HTM"),
	HTML("HTML"),
	ICO("ICO"),
	JPG("JPG"),
	JPEG("JPEG"),
	PNG("PNG"),
	TXT("TXT"),
	XML("XML");

	final String extension;

	ContentType(String extension) {
		this.extension = extension;
	}

	@Override
	public String toString() {
		switch (this) {
			case CSS:
				return "text/css";
			case JS:
				return "application/x-javascript";
			case GIF:
				return "image/gif";
			case HTM:
			case HTML:
				return "text/html";
			case ICO:
				return "image/gif";
			case JPG:
			case JPEG:
				return "image/jpeg";
			case PNG:
				return "image/png";
			case TXT:
				return "text/plain";
			case XML:
				return "text/xml";
			default:
				return null;
		}
	}
	
}
