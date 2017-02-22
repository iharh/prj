package mygwt.web.client.sentiments.wizard;

import mygwt.foundation.client.csrf.ProjectIdAware;

public class ProjectIdAwareImpl implements ProjectIdAware {
    @Override
    public long getProjectId() {
        // return ApplicationContext.get().getProjectId();
        return 0;
    }
}
