package mygwt.common.client.context;

public class ApplicationContext /*implements ProjectIdAware*/ {
    private static ApplicationContext context;

    public static ApplicationContext get() { //alias for getContext()
        return getContext();
    }

    public static ApplicationContext getContext() {
    	if (context == null) {
            context = new ApplicationContext();
        }
    	return context;
    }

    private ApplicationContext() {
    }

    //@Override
    public long getProjectId() {
        return 0; // projectId
    }
}
