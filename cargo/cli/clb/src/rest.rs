use crate::errors::ResT;
use crate::hbs::{PrjCreateData, PmvdData};
use reqwest::{Client, RequestBuilder, StatusCode};
use handlebars::Handlebars;
use std::time::Duration;

pub fn get_client() -> ResT<Client> {
    Ok(Client::builder()
        .timeout(Duration::from_secs(5 * 60))
        .build()?)
}

fn post_wsdl(client: &Client, name: &str) -> RequestBuilder {
    // ? "text/xml"
    let url = format!("http://localhost:18080/cbapi/{}?wsdl", name);
    client.post(&url)
        .basic_auth("admin", Some("admin"))
    // ? "text/xml"
}

pub fn prj_create(client: &Client, hbs: &Handlebars, lang_id: &str, prj_name: &str) -> ResT<String> {
    let req_data = PrjCreateData { lang_id: lang_id.to_string(), prj_name: prj_name.to_string(), };
    let req_body = hbs.render("prjCreate", &req_data)?;
    println!("prjCreate request body: {}", req_body);

    let mut resp = post_wsdl(client, "project")
        .body(req_body)
        .send()?;

    assert!(resp.status() == StatusCode::OK);

    // <?xml version='1.0' encoding='UTF-8'?><S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/"><S:Body><ns2:createProjectResponse xmlns:ns2="http://project.cbapi.clarabridge.com/"><return><status>SUCCESS</status><projectName>bn1</projectName></return></ns2:createProjectResponse></S:Body></S:Envelope>

    Ok(resp.text()?)
}

pub fn pmvd(client: &Client, hbs: &Handlebars, prj_name: &str, verbatim_text: &str) -> ResT<String> {
    let req_data = PmvdData { prj_name: prj_name.to_string(), verbatim_text: verbatim_text.to_string(), };
    let req_body = hbs.render("pmvd", &req_data)?;
    println!("pmvd request body: {}", req_body);

    let mut resp = post_wsdl(client, "realtime")
        .body(req_body)
        .send()?;

    assert!(resp.status() == StatusCode::OK);

    Ok(resp.text()?)
}
