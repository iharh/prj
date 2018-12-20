use crate::errors::ResT;
use crate::hbs::{PrjCreateData, PmvdData};
use reqwest::{Client, RequestBuilder, StatusCode};
use handlebars::Handlebars;

use xml::escape::escape_str_attribute;
use std::time::Duration;
use std::io::{Error, ErrorKind};

pub fn get_client() -> ResT<Client> {
    Ok(Client::builder()
        .timeout(Duration::from_secs(5 * 60))
        .build()?)
}

//&'static str
const RES_STATUS: &str = "<return><status>";
const RES_STATUS_LEN: usize = 16; // RES_STATUS.len();

const RES_STATUS_SUCCESS: &str = "SUCCESS";
const RES_STATUS_SUCCESS_LEN: usize = 7; // RES_STATUS_SUCCESS.len();

fn get_return_status(response_body: &String) -> ResT<String> {
    let idx: usize = response_body.find(RES_STATUS).ok_or_else(|| Error::new(ErrorKind::Other, "Status start"))? + RES_STATUS_LEN;
    let result: String = response_body.get(idx..(idx + RES_STATUS_SUCCESS_LEN)).ok_or_else(|| Error::new(ErrorKind::Other, "Status get"))?.to_string();
    Ok(result)
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
    // println!("prjCreate request body:\n {}", req_body);

    let mut resp = post_wsdl(client, "project")
        .body(req_body)
        .send()?;

    assert!(resp.status() == StatusCode::OK);
    let resp_text = resp.text()?;
    let result_status = get_return_status(&resp_text)?;
    assert!(result_status == RES_STATUS_SUCCESS);

    Ok(resp_text)
}

pub fn pmvd(client: &Client, hbs: &Handlebars, prj_name: &str, verbatim_text: &str) -> ResT<String> {
    let verb_text: String = escape_str_attribute(verbatim_text).to_string();
    let req_data = PmvdData { prj_name: prj_name.to_string(), verbatim_text: verb_text };
    let req_body = hbs.render("pmvd", &req_data)?;
    // println!("pmvd request body:\n {}", req_body);

    let mut resp = post_wsdl(client, "realtime")
        .body(req_body)
        .send()?;

    assert!(resp.status() == StatusCode::OK);
    let resp_text = resp.text()?;
    let result_status = get_return_status(&resp_text)?;
    assert!(result_status == RES_STATUS_SUCCESS);

    Ok(resp_text)
}
