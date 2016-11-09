#! /usr/bin/groovy

@Grapes([
    @Grab(group='org.apache.commons', module='commons-lang3', version='3.5'),
    @Grab(group='com.typesafe', module='config', version='1.3.1'),
    @Grab(group='commons-httpclient', module='commons-httpclient', version='3.1')
])

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

import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config

import org.apache.commons.lang3.SystemUtils

def publishToNexusHttpClient(usr, pass, artifact, version, fileName, url) {
    def group = "clarabridge"
    // def cmd = "curl -k -f -X PUT -u ${env.NEXUS_ADMIN} -T ${fileName} ${url}/${group}/${artifact}/${version}/${fileName}"

    def client = new HttpClient()
    client.getState().setCredentials(
        AuthScope.ANY,
        new UsernamePasswordCredentials(usr, pass)
    )
    //client.getParams().setConnectionManagerTimeout(20000);
    //client.getParams().setSoTimeout(10 * 60 * 1000);
    def finalUrl = "${url}/${group}/${artifact}/${version}/${fileName}"
    println("PUT: ${finalUrl}")

    def method = new PutMethod(finalUrl)

    def file = new File(fileName)

    def fis = new FileInputStream(file)
    println("using file: ${fileName} len: ${file.length()}")

    method.setRequestEntity(new InputStreamRequestEntity(fis, file.length()))
    method.setContentChunked(true);
    method.setUseExpectHeader(true); // method.getParams().setBooleanParameter("http.protocol.expect-continue", true);

    try {
        client.executeMethod(method);

        if (method.getStatusCode() != HttpStatus.SC_CREATED) { // SC_OK
            println("FAILED: ${method.getStatusLine().toString()}")
        }
    } finally {
        method.releaseConnection();
    }
}

def getJenkinsPropFile() {
    def JENKINS_CFG_ROOT = (SystemUtils.IS_OS_LINUX)?
        '/data/wrk/clb/hosts/jenkins' :
        'D:\\dev\\notes\\wrk\\clb\\hosts\\jenkins\\'
    // File.separator
    def cfgFileName = JENKINS_CFG_ROOT + File.separator + "jenkins.properties"
    return new File(cfgFileName)
}

def getJenkinsConf() { 
    return ConfigFactory.parseFile(getJenkinsPropFile())
}

def getJenkinsProp(conf, propName) {
    return conf.getString(propName)
}

def conf = getJenkinsConf()
def nexusReposUrl = getJenkinsProp(conf, 'nexusReposUrl') 
def nexusUsr = getJenkinsProp(conf, 'usr') 
def nexusPwd = getJenkinsProp(conf, 'pwd') 
println("nexusReposUrl: ${nexusReposUrl}")

publishToNexusHttpClient(nexusUsr, nexusPwd, 'cb-template-service', '1.0.0', 'a.txt', "${nexusReposUrl}/snapshots")
