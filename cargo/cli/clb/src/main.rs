// `error_chain!` can recurse deeply
#![recursion_limit = "1024"]

#[macro_use]
extern crate error_chain;
#[macro_use]
extern crate serde_derive;

mod args;
mod errors;
mod hbs;
mod rest;

use crate::args::{Args, USAGE};
use crate::hbs::reg_templates;
use crate::rest::{get_client, pmvd, prj_create};
use docopt::Docopt;
use handlebars::Handlebars;
use crate::errors::ResT;

fn main() -> ResT<()> {
    let args_res = Docopt::new(USAGE)
        ?.options_first(true).deserialize();

    let args: Args = args_res?;
    // println!("{:?}", args),

    let mut hbs: Handlebars = Handlebars::new();
    reg_templates(& mut hbs)?;

    let client = get_client()?;

    match args.arg_command {
        args::Command::Prj =>
        {
            let resp_body = prj_create(&client, &hbs, "bn", "bn1")?;
            println!("resp_body: {}", resp_body);
        },
        args::Command::Realtime  =>
        {
            let resp_body = pmvd(&client, &hbs, "bn1", "abc def")?;
            println!("resp_body: {}", resp_body);
        },
    }

    Ok(())
}
