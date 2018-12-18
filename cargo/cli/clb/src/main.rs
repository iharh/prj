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
use crate::errors::ResT;

fn main() -> ResT<()> {
    let args_res = Docopt::new(USAGE)
        ?.options_first(true).deserialize();

    let args: Args = args_res?;
    // println!("{:?}", args),

    match args.arg_command {
        args::Command::Prj =>
            println!("prj"),
        args::Command::Db  =>
            println!("db"),
    }

    Ok(())
}
