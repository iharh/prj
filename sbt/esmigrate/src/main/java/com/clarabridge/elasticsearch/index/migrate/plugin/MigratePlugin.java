package com.clarabridge.elasticsearch.index.migrate.plugin;

import java.util.Collection;

import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.plugins.AbstractPlugin;

public class MigratePlugin extends AbstractPlugin {

    @Override
    public String name() {
        return "cmp-index-migrate"; // $NON-NLS-1$
    }

    @Override
    public String description() {
        return "CMP index migration support"; // $NON-NLS-1$
    }

    @Override
    public Collection<Class<? extends Module>> modules() {
        //Collection<Class<? extends Module>> modules = Lists.newArrayList();
        //modules.add(ExampleRestModule.class);
        //return modules;

        return ImmutableList.<Class<? extends Module>>of(MigrateRestModule.class);
    }

    /**
     * Automatically called with the analysis module.
     */
    //public void onModule(AnalysisModule module) {
        //module.addProcessor(new CmpAnalysisBinderProcessor());
    //}
}
