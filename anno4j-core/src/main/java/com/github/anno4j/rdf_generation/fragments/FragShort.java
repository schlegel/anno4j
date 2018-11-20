package com.github.anno4j.rdf_generation.fragments;

import com.github.anno4j.rdf_generation.namespaces.XSD;

public class FragShort implements Fragment {

	private final static String ns = XSD.NS;

	private final static String fragment = "short";

	private final static String uri = ns + fragment;

	private final static String javaequiv = "class java.lang.Short";
	private final static String javaequiv2 = "short";
	private final static String javaequiv3 = "class [Ljava.lang.Short;";
	private final static String javaequiv4 = "class [S";

	@Override
	public boolean hasRelationTo(String javaval) {
		if (javaequiv.equals(javaval) || javaequiv2.equals(javaval) || javaequiv3.equals(javaval)
				|| javaequiv4.equals(javaval)) {
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

	@Override
	public boolean hasRDFRange() {
		return true;
	}
	
}