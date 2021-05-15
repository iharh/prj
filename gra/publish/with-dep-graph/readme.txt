Published pivy-1.1-SNAPSHOT.jar (mycompany:pivy:1.1-SNAPSHOT) to
    http://localhost:8081/nexus/content/repositories/snapshots/mycompany/pivy/1.1-SNAPSHOT/pivy-1.1-SNAPSHOT.jar
Published ivy-1.1-SNAPSHOT.xml (mycompany:pivy:1.1-SNAPSHOT) to
    http://localhost:8081/nexus/content/repositories/snapshots/mycompany/pivy/1.1-SNAPSHOT/ivy-1.1-SNAPSHOT.xml
Published pivy-1.1-SNAPSHOT.module (mycompany:pivy:1.1-SNAPSHOT) to
    http://localhost:8081/nexus/content/repositories/snapshots/mycompany/pivy/1.1-SNAPSHOT/pivy-1.1-SNAPSHOT.module


    public RepositoryTransport createTransport(String scheme, String name, Collection<Authentication> authentications, HttpRedirectVerifier redirectVerifier) {
        ...
        ResourceConnectorSpecification connectionDetails = new DefaultResourceConnectorSpecification(authentications, redirectVerifier);

    @Override
    public ExternalResourceConnector createResourceConnector(ResourceConnectorSpecification connectionDetails) {
        HttpClientHelper http = httpClientHelperFactory.create(DefaultHttpSettings.builder()
            .withAuthenticationSettings(connectionDetails.getAuthentications()) // !!!
            .withSslContextFactory(sslContextFactory) // !!! temporary - without this
            .withRedirectVerifier(connectionDetails.getRedirectVerifier())
            .build()
        );
        HttpResourceAccessor accessor = new HttpResourceAccessor(http);
        HttpResourceLister lister = new HttpResourceLister(accessor);
        HttpResourceUploader uploader = new HttpResourceUploader(http);
        return new DefaultExternalResourceConnector(accessor, lister, uploader);
    }


// 1. authentications
    public Collection<Authentication> getConfiguredAuthentication() { // !!!
            populateAuthenticationCredentials();
            if (usesCredentials() && authenticationContainer.size() == 0) {
                return Collections.singleton(new AllSchemesAuthentication(credentials.get())); // !!!

    PasswordCredentials credentials = new DefaultPasswordCredentials('username', 'password')
    // or
    def httpHeaderCredentials = new DefaultHttpHeaderCredentials()
    httpHeaderCredentials.setName("TestHttpHeaderName")
    httpHeaderCredentials.setValue("TestHttpHeaderValue")

// 2. sslContextFactory
    new DefaultSslContextFactory

// 3. redirectVerifier
    private IvyResolver createResolver(Set<String> schemes) {
        // !!!
        return createResolver(transportFactory.createTransport(schemes, getName(), getConfiguredAuthentication(), urlArtifactRepository.createRedirectVerifier()));
    }

    HttpRedirectVerifier createRedirectVerifier() {
        @Nullable
        URI uri = getUrl();
        return HttpRedirectVerifierFactory
            .create(
                uri,
                allowInsecureProtocol,
                this::throwExceptionDueToInsecureProtocol,
                redirection -> throwExceptionDueToInsecureRedirect(uri, redirection)
            );
    }

    private static class NoopHttpRedirectVerifier implements HttpRedirectVerifier {
        private static NoopHttpRedirectVerifier instance = new NoopHttpRedirectVerifier();

        @Override
        public void validateRedirects(Collection<URI> redirectLocations) {
            // Noop
        }
    }


curl -XPOST -u admin:admin123 -T pivy/build/libs/pivy-1.1-SNAPSHOT.jar http://localhost:8081/nexus/content/repositories/snapshots/mycompany/pivy/1.1-SNAPSHOT/pivy-1.1-SNAPSHOT.jar

curl -XPOST -u admin:admin123 -T a.txt http://localhost:8081/nexus/content/repositories/snapshots/mycompany/pivy/1.1-SNAPSHOT/a.txt
