package com.google.play;

import com.google.tools.RequestContext;

import java.util.Map;

public class DfeContext extends RequestContext {
	public DfeContext() {
	}

	public DfeContext(Map<? extends String, ? extends String> m) {
		super(m);
	}
}
