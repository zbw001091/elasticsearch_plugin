package com.zbw.espluginbasic.module;

import org.elasticsearch.common.inject.AbstractModule;

import com.zbw.espluginbasic.listener.HelloListener;

public class HelloModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(HelloListener.class).asEagerSingleton();
	}

}
