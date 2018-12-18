// `error_chain!` can recurse deeply
#![recursion_limit = "1024"]

#[macro_use]
extern crate error_chain;
#[macro_use]
extern crate serde_derive;

mod args;
mod errors;

use docopt::Docopt;
use crate::args::{Args, USAGE};
use handlebars::Handlebars;
use reqwest::{Client, StatusCode};
use std::time::Duration;
use crate::errors::ResT;

// define some data
#[derive(Serialize)]
pub struct PrjCreateData {
    lang_id: String,
    prj_name: String,
}

pub fn reg_templates(hbs: & mut Handlebars) -> ResT<()> {
    hbs.register_template_file("prjCreate", "./src/templates/prjCreate.hbs")?;
    Ok(())
}

fn main() -> ResT<()> {
    let args_res = Docopt::new(USAGE)
        ?.options_first(true).deserialize();

    let args: Args = args_res?;
    // println!("{:?}", args),

    let mut hbs: Handlebars = Handlebars::new();
    reg_templates(& mut hbs)?;

    let client = Client::builder()
        .timeout(Duration::from_secs(5 * 60))
        .build()?;

    match args.arg_command {
        args::Command::Prj =>
        {
            let req_data = PrjCreateData { lang_id: "bn".to_string(), prj_name: "bn1".to_string(), };
            let req_body = hbs.render("prjCreate", &req_data)?;
            println!("req_body: {}", req_body);

            // auto rs = rq.post(url, data, "text/xml");
            let mut resp = client.post("http://localhost:18080/cbapi/project?wsdl")
                .basic_auth("admin", Some("admin"))
                .body(req_body)
                .send()?;

            assert!(resp.status() == StatusCode::OK);

            let resp_body = resp.text()?;
            println!("resp_body: {}", resp_body);
        },
        args::Command::Db  =>
            println!("db"),
    }

    Ok(())
}
