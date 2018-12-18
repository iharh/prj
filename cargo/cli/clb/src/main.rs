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
use handlebars::{Handlebars, };
use crate::errors::ResT;

// define some data
#[derive(Serialize)]
pub struct PrjCreate {
    lang_id: String,
    prj_name: String,
}

fn main() -> ResT<()> {
    let args_res = Docopt::new(USAGE)
        ?.options_first(true).deserialize();

    let args: Args = args_res?;
    // println!("{:?}", args),

    let mut hbs = Handlebars::new();
    hbs.register_template_file("prjCreate", "./src/templates/prjCreate.hbs")?;
    let prj_create_data = PrjCreate { lang_id: "bn".to_string(), prj_name: "bn1".to_string(), };
    println!("{}", hbs.render("prjCreate", &prj_create_data)?);

    match args.arg_command {
        args::Command::Prj =>
            println!("prj"),
        args::Command::Db  =>
            println!("db"),
    }

    Ok(())
}
