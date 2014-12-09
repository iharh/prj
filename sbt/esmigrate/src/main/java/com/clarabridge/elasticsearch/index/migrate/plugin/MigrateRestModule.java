package com.clarabridge.elasticsearch.index.migrate.plugin;

import org.elasticsearch.common.inject.AbstractModule;

public class MigrateRestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MigrateRestHandler.class).asEagerSingleton();
    }
}
