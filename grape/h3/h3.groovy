#! /usr/bin/groovy

@Grapes(
    @Grab(group='commons-httpclient', module='commons-httpclient', version='3.1')
)

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;

import org.apache.commons.httpclient.UsernamePasswordCredentials;

import org.apache.commons.httpclient.auth.AuthScope;

import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

//import org.apache.commons.httpclient.Header 
//import org.apache.commons.httpclient.HostConfiguration
//import org.apache.commons.httpclient.NameValuePair
//import org.apache.commons.httpclient.methods.GetMethod
//import org.apache.commons.httpclient.cookie.CookiePolicy
//import org.apache.commons.httpclient.params.HttpClientParams

def publishToNexusHttpClient(usr, pass, artifact, version, fileName, url) {
    def group = "clarabridge"
    // def cmd = "curl -k -f -X PUT -u ${env.NEXUS_ADMIN} -T ${fileName} ${url}/${group}/${artifact}/${version}/${fileName}"

    def client = new HttpClient()
    client.getState().setCredentials(
        AuthScope.ANY,
        new UsernamePasswordCredentials(usr, pass)
    )
    client.getParams().setConnectionManagerTimeout(20000);
    client.getParams().setSoTimeout(10 * 60 * 1000);
    def finalUrl = "${url}/${group}/${artifact}/${version}/${fileName}"
    println("PUT: ${finalUrl}")

    def method = new PutMethod(finalUrl)

    def file = new File(fileName)

    print("using file: ${fileName}")
    def fis = new FileInputStream(file)
    print("done with using file: ${fileName} len: ${file.length()}")

    method.setRequestEntity(new InputStreamRequestEntity(fis, file.length()))
    method.setContentChunked(true);
    method.setUseExpectHeader(true); // method.getParams().setBooleanParameter("http.protocol.expect-continue", true);

    try {
        client.executeMethod(method);

        if (method.getStatusCode() == HttpStatus.SC_OK) {
            println("PUBLISH RESP: ${method.getResponseBodyAsString()}")
        } else {
            println("FAILED: ${method.getStatusLine().toString()}")
        }
    } finally {
        method.releaseConnection();
    }
}

publishToNexusHttpClient('admin', 'admin123', 'cb-template-service', '1.0.0', 'a.txt', 'http://10.120.167.243:8081/content/repositories/snapshots')
