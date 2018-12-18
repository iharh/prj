use crate::errors::ResT;
use crate::hbs::{PrjCreateData};
use reqwest::{Client, StatusCode};
use handlebars::Handlebars;
use std::time::Duration;

pub fn get_client() -> ResT<Client> {
    Ok(Client::builder()
        .timeout(Duration::from_secs(5 * 60))
        .build()?)
}

pub fn prj_create(client: &Client, hbs: &Handlebars, lang_id: String, prj_name: String) -> ResT<String> {
    let req_data = PrjCreateData { lang_id: lang_id, prj_name: prj_name, };
    let req_body = hbs.render("prjCreate", &req_data)?;
    println!("req_body: {}", req_body);

    // ? "text/xml"
    let mut resp = client.post("http://localhost:18080/cbapi/project?wsdl")
        .basic_auth("admin", Some("admin"))
        .body(req_body)
        .send()?;

    assert!(resp.status() == StatusCode::OK);

    Ok(resp.text()?)
}
