package com.github.anno4j.rdf_generation.fragments;

import com.github.anno4j.rdf_generation.namespaces.XSD;

public class FragDate implements Fragment {
	
	private final static String ns = XSD.NS;

	private final static String fragment = "date";

	private final static String uri = ns + fragment;

	private final static String javaequiv = "class java.util.Date";
	private final static String javaequiv2 = "class [Ljava.util.Date;";

	@Override
	public boolean hasRelationTo(String javaval) {
		if (javaequiv.equals(javaval) || javaequiv2.equals(javaval)) {
			return true;
		}
		return false;
	}

	@Override
	public String getURI() {
		return uri;
	}
	
	@Override
	public String getJavaEquiv() {
		return "";
	}
}