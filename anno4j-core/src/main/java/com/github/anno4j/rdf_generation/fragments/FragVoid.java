package com.github.anno4j.rdf_generation.fragments;

public class FragVoid implements Fragment {
	
	private final static String ns = "";

	private final static String fragment = "";

	private final static String uri = ns + fragment;

	private final static String javaequiv = "void";

	@Override
	public boolean hasRelationTo(String javaval) {
		return false;
	}

	@Override
	public String getURI() {
		return uri;
	}
	
	@Override
	public String getJavaEquiv() {
		return javaequiv;
	}

}